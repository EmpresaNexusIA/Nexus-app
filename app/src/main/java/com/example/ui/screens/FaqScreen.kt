package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.util.WhatsAppHelper

@Composable
fun FaqScreen(
    innerPadding: PaddingValues
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var openIndex by remember { mutableIntStateOf(0) }

    val allFaqs = remember {
        listOf(
            FaqItem(
                q = "¿Es gratis?",
                a = "El piloto de 30 días no cuesta nada; pagás recién si te quedás. Después: puesta en marcha única + mensual simple en pesos. 0% comisión por venta, siempre."
            ),
            FaqItem(
                q = "¿Reemplaza mi Instagram o mi WhatsApp?",
                a = "No, los usa. Instagram es la vidriera; esto es el mostrador."
            ),
            FaqItem(
                q = "¿Tengo que saber de computadoras?",
                a = "Si sabés usar WhatsApp, ya sabés el 90%. La parte difícil la hacemos nosotros."
            ),
            FaqItem(
                q = "¿Mis clientes y mis datos son míos?",
                a = "Tuyos, exportables cuando quieras. Tu dominio a tu nombre desde el día uno."
            ),
            FaqItem(
                q = "¿Y si no me sirve?",
                a = "Para eso el piloto: decidís con la tienda andando. Si no te sirve, no pagás nada."
            ),
            FaqItem(
                q = "¿Cuánto cuesta?",
                a = "0% comisión. Puesta en marcha única + mensual en pesos: escribime y te paso el número al toque."
            ),
            FaqItem(
                q = "¿Se puede caer?",
                a = "Como todo sistema. Respaldo diario, no se pierde ningún dato. No prometemos magia — prometemos que no perdés nada."
            ),
            FaqItem(
                q = "¿Cuánto tardo en tener mi tienda?",
                a = "La primera versión con tu catálogo, en días."
            )
        )
    }

    val filteredFaqs = remember(searchQuery) {
        if (searchQuery.isBlank()) allFaqs
        else allFaqs.filter {
            it.q.contains(searchQuery, ignoreCase = true) || it.a.contains(searchQuery, ignoreCase = true)
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
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HudPillBadge(
                text = "⚡ PREGUNTAS FRECUENTES // SOPORTE",
                dotColor = NexoraCyanNeon,
                textColor = NexoraCyanNeon
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "DUDAS CLARAS",
                color = NexoraOnSurface,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Respuestas honestas, sin vueltas ni letras chicas.",
                color = NexoraOnSurfaceVariant,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 340.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Buscar duda o palabra clave...",
                        color = NexoraOutline,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = NexoraCyanNeon,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpiar",
                            tint = NexoraOutline,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { searchQuery = "" }
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NexoraCyanNeon,
                    unfocusedBorderColor = Color(0x3300F0FF),
                    focusedContainerColor = Color(0xFF151B2B),
                    unfocusedContainerColor = Color(0xFF151B2B),
                    focusedTextColor = NexoraOnSurface,
                    unfocusedTextColor = NexoraOnSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Accordion List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (filteredFaqs.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF151B2B))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron respuestas para tu búsqueda. ¡Escribinos directamente al WhatsApp!",
                            color = NexoraOnSurfaceVariant,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    filteredFaqs.forEachIndexed { index, faq ->
                        val isOpen = openIndex == index
                        val rotation by animateFloatAsState(
                            targetValue = if (isOpen) 180f else 0f,
                            label = "faq_rotation"
                        )

                        CyberGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            borderColor = if (isOpen) Color(0x6600F0FF) else Color(0x2000F0FF),
                            backgroundColor = Color(0xFF191F2F)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        openIndex = if (isOpen) -1 else index
                                    }
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = faq.q,
                                        color = if (isOpen) NexoraCyanNeon else NexoraOnSurface,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ExpandMore,
                                        contentDescription = "Abrir",
                                        tint = if (isOpen) NexoraCyanNeon else NexoraOutline,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .rotate(rotation)
                                    )
                                }

                                AnimatedVisibility(visible = isOpen) {
                                    Column {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        HorizontalDivider(thickness = 1.dp, color = Color(0x2000F0FF))
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = faq.a,
                                            color = NexoraOnSurfaceVariant,
                                            fontSize = 13.sp,
                                            lineHeight = 19.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Direct Contact Card
            CyberGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                borderColor = Color(0x4000F0FF),
                backgroundColor = Color(0xFF151B2B)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = "Soporte",
                        tint = NexoraCyanNeon,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¿Tenés otra pregunta?",
                        color = NexoraOnSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Escribinos por WhatsApp y te respondemos de persona a persona al instante.",
                        color = NexoraOnSurfaceVariant,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    WhatsAppActionButton(
                        text = "Probar 30 días con mi catálogo",
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "faq_screen_cta"
                    )
                }
            }
        }
    }
}
