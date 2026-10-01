package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.net.URLEncoder

object WhatsAppHelper {
    const val WHATSAPP_PHONE = "5493416621389"
    const val DEFAULT_MESSAGE = "Hola, quiero probar la tienda 30 días con mi catálogo"

    fun openWhatsApp(context: Context, customMessage: String? = null) {
        val message = customMessage ?: DEFAULT_MESSAGE
        val encodedMessage = try {
            URLEncoder.encode(message, "UTF-8")
        } catch (_: Exception) {
            message
        }
        val url = "https://wa.me/$WHATSAPP_PHONE?text=$encodedMessage"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            try {
                // Fallback direct browser intent
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(browserIntent)
            } catch (_: Exception) {
                Toast.makeText(context, "No se pudo abrir WhatsApp ($WHATSAPP_PHONE)", Toast.LENGTH_LONG).show()
            }
        }
    }
}
