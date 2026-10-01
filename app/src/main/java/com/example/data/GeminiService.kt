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

    private const val SYSTEM_INSTRUCTION = """
Sos el Asistente Inteligente de Nexora para comerciantes y vendedores de Rosario y Argentina.
Ayudás a comerciantes reales (kiosqueros, verduleros, pasteleras, rotiserías, tiendas de ropa en Instagram):
1. A entender cómo funciona su catálogo online y pedidos por WhatsApp con 0% de comisión.
2. A planificar combos, promociones y mensajes atractivos para sus clientes.
3. A encontrar proveedores, mayoristas, centros comerciales y zonas clave de Rosario utilizando datos actualizados de Google Maps.
Hablá siempre en español rioplatense cálido con voseo ("poné", "fijate", "mirá", "escribile"), sin jerga técnica de programación.
"""

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
            // Intelligent local fallback when API key is not configured in secrets
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
            // Add previous conversation turns
            val recentHistory = history.takeLast(8)
            for (msg in recentHistory) {
                val turn = JSONObject().apply {
                    put("role", if (msg.isUser) "user" else "model")
                    put("parts", JSONArray().put(JSONObject().put("text", msg.text)))
                }
                contentsArray.put(turn)
            }

            // Add current message
            contentsArray.put(
                JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().put(JSONObject().put("text", userMessage)))
                }
            )

            val rootJson = JSONObject().apply {
                put("contents", contentsArray)
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", SYSTEM_INSTRUCTION)))
                })
                // Request Google Maps tool grounding
                put("tools", JSONArray().apply {
                    put(JSONObject().put("googleMaps", JSONObject()))
                })
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
                Log.w(TAG, "Gemini call with googleMaps failed (${response.code}), retrying without tool: $responseBody")
                // Retry without googleMaps tool in case the key doesn't have Maps tool enabled
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

            // Parse grounding metadata if Google Maps data was returned
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
                replyBuilder.toString()
            } else {
                "¡De una! Te recomiendo organizar tu catálogo con 5 a 10 productos estrella para que tus clientes puedan pedir directo por WhatsApp sin perderse."
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
                            text = text,
                            isUser = false
                        )
                    )
                }
            }
            Result.success(
                ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    text = "¡Hola! Para tu negocio te sugiero subir tus fotos con buena luz natural y poner el link de Nexora directo en tu bio de Instagram. ¿Qué rubro vendés?",
                    isUser = false
                )
            )
        } catch (_: Exception) {
            Result.success(
                ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    text = "¡Excelente consulta! Con Nexora no pagás ninguna comisión por venta y tus pedidos llegan ordenados directo a WhatsApp.",
                    isUser = false
                )
            )
        }
    }

    private fun generateLocalMerchantAdvice(userMessage: String): Pair<String, List<MapPlaceInfo>> {
        val lower = userMessage.lowercase()
        return when {
            lower.contains("rosario") || lower.contains("mapa") || lower.contains("proveedor") || lower.contains("mayorista") || lower.contains("donde") -> {
                Pair(
                    "¡Fijate en estas zonas comerciales y centros clave de Rosario para abastecer tu negocio y atraer más clientes locales!\n\n" +
                    "• **Peatonal Córdoba y Peatonal San Martín**: el corazón comercial de Rosario Centro con mayor tránsito peatonal para retiro y entregas.\n" +
                    "• **Mercado de Productores de Rosario (27 de Febrero y San Nicolás)**: ideal para abastecimiento mayorista de verdulerías, frutas y almacenes.\n" +
                    "• **Zona Pichincha (Bv. Oroño y Jujuy)**: polo gastronómico y de cafeterías artesanales para delivery rápido por WhatsApp.",
                    listOf(
                        MapPlaceInfo("Mercado de Productores de Rosario", "27 de Febrero y San Nicolás, Rosario", "https://maps.google.com/?q=Mercado+de+Productores+Rosario"),
                        MapPlaceInfo("Peatonal Córdoba", "Centro de Rosario, Santa Fe", "https://maps.google.com/?q=Peatonal+Cordoba+Rosario"),
                        MapPlaceInfo("Distrito Pichincha", "Bv. Oroño y Alrededores, Rosario", "https://maps.google.com/?q=Pichincha+Rosario")
                    )
                )
            }
            lower.contains("precio") || lower.contains("comision") || lower.contains("costo") -> {
                Pair(
                    "En Nexora la regla es clara: **0% de comisión por venta, para siempre**.\n\n" +
                    "Tenés 30 días de prueba gratuita con tu catálogo real cargado por nosotros. Recién cuando ves que te sirve y decidís quedarte, abonás una puesta en marcha única y un mantenimiento mensual accesible en pesos argentinos. ¡El 100% de tu margen de ganancia es tuyo!",
                    emptyList()
                )
            }
            lower.contains("como funciona") || lower.contains("empezar") || lower.contains("catalogo") -> {
                Pair(
                    "¡Es facilísimo y no tenés que saber nada de computación!\n\n" +
                    "1. **Nos mandás tus fotos y precios**: por WhatsApp, cuaderno o Excel.\n" +
                    "2. **Te armamos la tienda online**: lista con tu nombre y link para la bio de Instagram.\n" +
                    "3. **Tus clientes eligen y te llega el pedido limpio**: con cantidades, total calculado y medio de pago directo a tu WhatsApp.\n\n" +
                    "¿Querés que armemos el borrador de tu catálogo hoy?",
                    emptyList()
                )
            }
            else -> {
                Pair(
                    "¡Qué buena idea para tu negocio! Con Nexora podés poner tu catálogo en la bio de Instagram y recibir cada pedido con el detalle exacto: productos, cantidades y total listo para cobrar.\n\n" +
                    "Además, si estás en Rosario o alrededores, te atendemos de forma directa y personalizada. ¿Qué productos vendés hoy?",
                    emptyList()
                )
            }
        }
    }
}
