package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppHelper

@Composable
fun PricingScreen(
    innerPadding: PaddingValues
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

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
                text = "⚡ TRANSPARENCIA TOTAL // SIN LETRA CHICA",
                dotColor = NexoraGoldSecondary,
                textColor = NexoraGoldSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "0% COMISIÓN POR VENTA",
                color = NexoraOnSurface,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "PARA SIEMPRE.",
                color = NexoraCyanNeon,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "No nos quedamos con un solo peso de lo que vendés. Tu esfuerzo y tu margen quedan 100% en tu bolsillo.",
                color = NexoraOnSurfaceVariant,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 360.dp)
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Main Pilot Card
            CyberGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                borderColor = NexoraGoldSecondary,
                backgroundColor = Color(0xFF131826),
                showCornerReticles = true
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NexoraGoldSecondary.copy(alpha = 0.15f))
                            .border(1.dp, NexoraGoldSecondary.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Piloto",
                            tint = NexoraGoldSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PROGRAMA PILOTO 30 DÍAS",
                            color = NexoraGoldSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Probalá con tu catálogo real, 30 días",
                        color = NexoraOnSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Armamos TU tienda con TUS productos. La ves andando, la compartís, recibís pedidos de verdad. Recién cuando decidís quedarte, se paga la puesta en marcha. Si no te sirve, no debés nada.",
                        color = NexoraOnSurfaceVariant,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ChecklistRow(text = "Tu catálogo cargado por nosotros")
                        ChecklistRow(text = "Link listo para la bio de Instagram")
                        ChecklistRow(text = "Soporte mano a mano por WhatsApp")
                        ChecklistRow(text = "Sin tarjeta de crédito ni compromisos")
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    WhatsAppActionButton(
                        text = "Quiero probar 30 días",
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "pricing_piloto_btn"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "ATENCIÓN DIRECTA DESDE ROSARIO, SANTA FE 🇦🇷",
                        color = NexoraOutline,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Explanation Card
            CyberGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                borderColor = Color(0x3300F0FF),
                backgroundColor = Color(0xFF151B2B)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "¿CÓMO FUNCIONA EL PAGO?",
                        color = NexoraCyanNeon,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "El piloto de 30 días no cuesta nada; pagás recién si te quedás.\n\nDespués: puesta en marcha única + mensual simple en pesos argentinos. 0% comisión por venta, siempre.\n\nEscribinos por WhatsApp y te pasamos el número al toque.",
                        color = NexoraOnSurface,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF191F2F))
                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                            .clickable {
                                WhatsAppHelper.openWhatsApp(context)
                            }
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = "Consultar",
                                tint = NexoraCyanNeon,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Consultar valor exacto en pesos",
                                    color = NexoraOnSurface,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Respondemos al toque por WhatsApp",
                                    color = NexoraCyanNeon,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
