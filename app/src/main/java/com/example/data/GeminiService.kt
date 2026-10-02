package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: String = "Ahora",
    val mapPlaces: List<MapPlaceInfo> = emptyList()
)

data class MapPlaceInfo(
    val title: String,
    val address: String? = null,
    val uri: String? = null
)

object GeminiService {
    private const val TAG = "GeminiService"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val SYSTEM_INSTRUCTION =
        "Sos el asesor de Nexora para comerciantes de Rosario. Respondé en 2 a 4 frases, directo al hueso, en voseo. Una sola idea por respuesta. Sin introducciones, sin resúmenes, sin despedidas. Máximo 50 palabras, salvo que te pidan expresamente más detalle. Si te falta un dato, preguntá UNA sola cosa. Escribí como un mensaje de WhatsApp de un asesor que respeta el tiempo del comerciante."

    suspend fun sendMessage(
        history: List<ChatMessage>,
        userMessage: String
    ): Result<ChatMessage> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            val localResponse = generateLocalMerchantAdvice(userMessage)
            return@withContext Result.success(
                ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    text = localResponse.first,
                    isUser = false,
                    mapPlaces = localResponse.second
                )
            )
        }

        try {
            val endpoint = "$BASE_URL/$MODEL_NAME:generateContent?key=$apiKey"

            val contentsArray = JSONArray()
            val recentHistory = history.takeLast(6)
            for (msg in recentHistory) {
                val turn = JSONObject().apply {
                    put("role", if (msg.isUser) "user" else "model")
                    put("parts", JSONArray().put(JSONObject().put("text", msg.text)))
                }
                contentsArray.put(turn)
            }

            contentsArray.put(
                JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().put(JSONObject().put("text", userMessage)))
                }
            )

            val lower = userMessage.lowercase()
            val isExplicitRosarioLocation = lower.contains("dirección") ||
                lower.contains("direccion") ||
                lower.contains("dónde queda") ||
                lower.contains("donde queda") ||
                lower.contains("zona comercial") ||
                lower.contains("mercado de productores") ||
                lower.contains("peatonal") ||
                lower.contains("pichincha") ||
                (lower.contains("proveedor") && lower.contains("rosario")) ||
                (lower.contains("mayorista") && lower.contains("rosario"))

            val rootJson = JSONObject().apply {
                put("contents", contentsArray)
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", SYSTEM_INSTRUCTION)))
                })
                put("generationConfig", JSONObject().apply {
                    put("maxOutputTokens", 150)
                    put("temperature", 0.4)
                })
                if (isExplicitRosarioLocation) {
                    put("tools", JSONArray().apply {
                        put(JSONObject().put("googleMaps", JSONObject()))
                    })
                }
            }

            val requestBody = rootJson.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.w(TAG, "Gemini call failed (${response.code}), retrying without tools: $responseBody")
                return@withContext retryWithoutTools(apiKey, contentsArray)
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")

            val replyBuilder = StringBuilder()
            val places = mutableListOf<MapPlaceInfo>()

            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val part = parts.optJSONObject(i)
                    val text = part?.optString("text")
                    if (!text.isNullOrBlank()) {
                        replyBuilder.append(text)
                    }
                }
            }

            val groundingMetadata = firstCandidate?.optJSONObject("groundingMetadata")
            if (groundingMetadata != null) {
                val searchChunks = groundingMetadata.optJSONArray("groundingChunks")
                if (searchChunks != null) {
                    for (j in 0 until searchChunks.length()) {
                        val chunk = searchChunks.optJSONObject(j)
                        val web = chunk?.optJSONObject("web")
                        val title = web?.optString("title")
                        val uri = web?.optString("uri")
                        if (!title.isNullOrBlank()) {
                            places.add(MapPlaceInfo(title = title, uri = uri))
                        }
                    }
                }
            }

            val resultText = if (replyBuilder.isNotBlank()) {
                replyBuilder.toString().trim()
            } else {
                "Con Nexora armás tu catálogo online en días y recibís los pedidos directo a tu WhatsApp. ¿Qué rubro vendés?"
            }

            Result.success(
                ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    text = resultText,
                    isUser = false,
                    mapPlaces = places
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error contacting Gemini API", e)
            val fallback = generateLocalMerchantAdvice(userMessage)
            Result.success(
                ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    text = fallback.first,
                    isUser = false,
                    mapPlaces = fallback.second
                )
            )
        }
    }

    private fun retryWithoutTools(apiKey: String, contentsArray: JSONArray): Result<ChatMessage> {
        return try {
            val endpoint = "$BASE_URL/$MODEL_NAME:generateContent?key=$apiKey"
            val rootJson = JSONObject().apply {
                put("contents", contentsArray)
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", SYSTEM_INSTRUCTION)))
                })
                put("generationConfig", JSONObject().apply {
                    put("maxOutputTokens", 150)
                    put("temperature", 0.4)
                })
            }

            val request = Request.Builder()
                .url(endpoint)
                .post(rootJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (response.isSuccessful) {
                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                val text = candidates?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text")

                if (!text.isNullOrBlank()) {
                    return Result.success(
                        ChatMessage(
                            id = System.currentTimeMillis().toString(),
                            text = text.trim(),
                            isUser = false
                        )
                    )
                }
            }
            Result.success(
                ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    text = "Subís tus productos con foto y precio, y tus clientes te piden directo al WhatsApp. ¿Qué rubro vendés?",
                    isUser = false
                )
            )
        } catch (_: Exception) {
            Result.success(
                ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    text = "En Nexora no pagás comisión por venta y tus pedidos llegan ordenados por WhatsApp. ¿Querés probar 30 días?",
                    isUser = false
                )
            )
        }
    }

    private fun generateLocalMerchantAdvice(userMessage: String): Pair<String, List<MapPlaceInfo>> {
        val lower = userMessage.lowercase()
        return when {
            lower.contains("rosario") || lower.contains("mapa") || lower.contains("proveedor") || lower.contains("mayorista") || lower.contains("donde") || lower.contains("dónde") -> {
                Pair(
                    "Para mayoristas de alimentos tenés el Mercado de Productores en 27 de Febrero y San Nicolás. Para indumentaria y bazar, la Peatonal San Martín concentra gran variedad. ¿Qué rubro puntual buscás abastecer?",
                    listOf(
                        MapPlaceInfo("Mercado de Productores de Rosario", "27 de Febrero y San Nicolás, Rosario", "https://maps.google.com/?q=Mercado+de+Productores+Rosario"),
                        MapPlaceInfo("Peatonal San Martín", "Rosario Centro, Santa Fe", "https://maps.google.com/?q=Peatonal+San+Martin+Rosario")
                    )
                )
            }
            lower.contains("precio") || lower.contains("comision") || lower.contains("comisión") || lower.contains("costo") || lower.contains("cuanto") || lower.contains("cuánto") -> {
                Pair(
                    "La comisión por venta es 0% para siempre. Tenés 30 días de prueba gratuita y después una cuota mensual fija en pesos. ¿Querés que armemos tu catálogo de prueba?",
                    emptyList()
                )
            }
            lower.contains("como funciona") || lower.contains("cómo funciona") || lower.contains("empezar") || lower.contains("catalogo") || lower.contains("catálogo") -> {
                Pair(
                    "Nos mandás fotos y precios por WhatsApp y te armamos la tienda con tu propio link. Tus clientes eligen y te llega el pedido limpio y sumado. ¿Qué productos vendés hoy?",
                    emptyList()
                )
            }
            else -> {
                Pair(
                    "Con Nexora ponés tu link en Instagram y recibís los pedidos ya sumados por WhatsApp, sin pagar comisión. Lo probás 30 días gratis con tu catálogo real. ¿Te armamos la demo?",
                    emptyList()
                )
            }
        }
    }
}
