package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.data.GeminiService
import com.example.data.MapPlaceInfo
import com.example.ui.components.CyberGlassCard
import com.example.ui.components.CyberGridCanvas
import com.example.ui.components.HudPillBadge
import com.example.ui.components.WhatsAppActionButton
import com.example.ui.theme.NexoraCyanNeon
import com.example.ui.theme.NexoraGoldSecondary
import com.example.ui.theme.NexoraNavyBackground
import com.example.ui.theme.NexoraOnSurface
import com.example.ui.theme.NexoraOnSurfaceVariant
import com.example.ui.theme.NexoraOutline
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceHigh
import com.example.ui.theme.NexoraSurfaceLow
import com.example.ui.theme.WhatsAppGreen
import kotlinx.coroutines.launch

@Composable
fun AssistantScreen(
    innerPadding: PaddingValues
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = "1",
                text = "¡Hola! Soy tu asistente de Nexora con inteligencia Gemini y datos locales de Google Maps 📍.\n\nContame qué vendés (ropa, panificados, comidas, verdulería) o consultame sobre zonas de Rosario, ideas de promociones y cómo armar tu catálogo online con 0% de comisión.",
                isUser = false
            )
        )
    }

    val quickPrompts = listOf(
        "¿Cómo funciona la tienda?",
        "Zonas comerciales y mayoristas en Rosario 📍",
        "¿Cómo calculo mis promociones?",
        "0% comisión: ¿cómo se aplica?"
    )

    fun sendUserMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isBlank() || isLoading) return

        val userMsg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            text = trimmed,
            isUser = true
        )
        messages.add(userMsg)
        inputText = ""
        isLoading = true

        scope.launch {
            listState.animateScrollToItem(messages.size - 1)
            val result = GeminiService.sendMessage(
                history = messages.toList(),
                userMessage = trimmed
            )
            result.onSuccess { reply ->
                messages.add(reply)
            }.onFailure { err ->
                messages.add(
                    ChatMessage(
                        id = System.currentTimeMillis().toString(),
                        text = "¡Ups! Ocurrió un detalle de conexión, pero podés consultarnos directamente por WhatsApp al 5493416621389.",
                        isUser = false
                    )
                )
            }
            isLoading = false
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NexoraNavyBackground)
    ) {
        CyberGridCanvas(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF151B2B).copy(alpha = 0.95f))
                    .border(1.dp, Color(0x3000F0FF), RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(NexoraCyanNeon.copy(alpha = 0.15f))
                                .border(1.dp, NexoraCyanNeon, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "IA",
                                tint = NexoraCyanNeon,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "NEXORA ASISTENTE IA",
                                color = NexoraOnSurface,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Gemini 3.5 Flash · Google Maps Grounding",
                                color = NexoraCyanNeon,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    HudPillBadge(
                        text = "ONLINE",
                        dotColor = NexoraCyanNeon,
                        textColor = NexoraCyanNeon
                    )
                }
            }

            // Quick Prompt Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickPrompts.take(2).forEach { prompt ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF191F2F))
                            .border(1.dp, Color(0x3300F0FF), RoundedCornerShape(6.dp))
                            .clickable { sendUserMessage(prompt) }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = prompt,
                            color = NexoraOnSurfaceVariant,
                            fontSize = 10.sp,
                            maxLines = 1,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    ChatBubbleItem(message = msg, onPlaceClicked = { uri ->
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    })
                }

                if (isLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF191F2F))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            CircularProgressIndicator(
                                color = NexoraCyanNeon,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pensando respuesta con Gemini...",
                                color = NexoraCyanNeon,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // Bottom Input Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF151B2B))
                    .border(1.dp, Color(0x3000F0FF), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                text = "Escribí tu consulta...",
                                color = NexoraOutline,
                                fontSize = 13.sp
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexoraCyanNeon,
                            unfocusedBorderColor = Color(0x3300F0FF),
                            focusedContainerColor = Color(0xFF191F2F),
                            unfocusedContainerColor = Color(0xFF191F2F),
                            focusedTextColor = NexoraOnSurface,
                            unfocusedTextColor = NexoraOnSurface
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("gemini_chat_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { sendUserMessage(inputText) },
                        enabled = inputText.isNotBlank() && !isLoading,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (inputText.isNotBlank() && !isLoading) NexoraCyanNeon else Color(0xFF242A3A)
                            )
                            .testTag("gemini_chat_send_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar",
                            tint = if (inputText.isNotBlank() && !isLoading) Color(0xFF050811) else NexoraOutline,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(
    message: ChatMessage,
    onPlaceClicked: (String) -> Unit
) {
    val isUser = message.isUser

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 2.dp)
        ) {
            Icon(
                imageVector = if (isUser) Icons.Default.Person else Icons.Default.AutoAwesome,
                contentDescription = if (isUser) "Vos" else "Nexora IA",
                tint = if (isUser) NexoraGoldSecondary else NexoraCyanNeon,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isUser) "Comerciante" else "Nexora IA (Rosario)",
                color = if (isUser) NexoraGoldSecondary else NexoraCyanNeon,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = if (isUser) 12.dp else 2.dp,
                        topEnd = if (isUser) 2.dp else 12.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    )
                )
                .background(
                    if (isUser) Color(0xFF242A3A) else Color(0xFF191F2F)
                )
                .border(
                    width = 1.dp,
                    color = if (isUser) Color(0x33FABC4D) else Color(0x3300F0FF),
                    shape = RoundedCornerShape(
                        topStart = if (isUser) 12.dp else 2.dp,
                        topEnd = if (isUser) 2.dp else 12.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    )
                )
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    color = NexoraOnSurface,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                // Google Maps Grounding Places
                if (message.mapPlaces.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "LUGARES EN GOOGLE MAPS 📍",
                        color = NexoraGoldSecondary,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        message.mapPlaces.forEach { place ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF151B2B))
                                    .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(4.dp))
                                    .clickable {
                                        val uri = place.uri ?: "https://maps.google.com/?q=${Uri.encode(place.title)}"
                                        onPlaceClicked(uri)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = "Mapa",
                                    tint = NexoraCyanNeon,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = place.title,
                                        color = NexoraCyanNeon,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (!place.address.isNullOrBlank()) {
                                        Text(
                                            text = place.address,
                                            color = NexoraOnSurfaceVariant,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.NearMe,
                                    contentDescription = "Ir",
                                    tint = NexoraGoldSecondary,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
