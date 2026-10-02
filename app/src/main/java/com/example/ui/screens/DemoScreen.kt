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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
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
import com.example.ui.theme.WhatsAppBubbleIn
import com.example.ui.theme.WhatsAppChatBg
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppTextPrimary
import com.example.ui.theme.WhatsAppTextSecondary

data class DemoProduct(
    val id: String,
    val name: String,
    val emoji: String,
    val price: Int,
    val imageRes: Int
)

@Composable
fun DemoScreen(
    innerPadding: PaddingValues
) {
    val scrollState = rememberScrollState()

    val demoProducts = remember {
        listOf(
            DemoProduct(
                id = "pan",
                name = "Pan casero",
                emoji = "🍞",
                price = 1200,
                imageRes = R.drawable.img_pan_frances
            ),
            DemoProduct(
                id = "queso",
                name = "Queso cremoso",
                emoji = "🧀",
                price = 1800,
                imageRes = R.drawable.img_queso
            ),
            DemoProduct(
                id = "medialunas",
                name = "Medialunas ×6",
                emoji = "🥐",
                price = 1400,
                imageRes = R.drawable.img_medialunas
            ),
            DemoProduct(
                id = "cafe",
                name = "Café 1/2 kg",
                emoji = "☕",
                price = 2900,
                imageRes = R.drawable.img_cafe
            ),
            DemoProduct(
                id = "mermelada",
                name = "Mermelada casera",
                emoji = "🍯",
                price = 1500,
                imageRes = R.drawable.img_mermelada
            ),
            DemoProduct(
                id = "yerba",
                name = "Yerba orgánica 1kg",
                emoji = "🧉",
                price = 2200,
                imageRes = R.drawable.img_yerba
            )
        )
    }

    // Default cart contains the exact mockup items:
    // Pan casero x2, Queso cremoso x1, Medialunas x1
    val cartCounts = remember {
        mutableStateMapOf(
            "pan" to 2,
            "queso" to 1,
            "medialunas" to 1,
            "cafe" to 0,
            "mermelada" to 0,
            "yerba" to 0
        )
    }

    var vipDiscountEnabled by remember { mutableStateOf(false) }
    var cashDiscountEnabled by remember { mutableStateOf(true) }

    // Computations
    val subtotal = demoProducts.sumOf { (cartCounts[it.id] ?: 0) * it.price }
    val totalItemsCount = cartCounts.values.sum()
    val discountPercent = (if (vipDiscountEnabled) 10 else 0) + (if (cashDiscountEnabled) 5 else 0)
    val discountAmount = if (discountPercent > 0) (subtotal * discountPercent) / 100 else 0
    val totalFinal = subtotal - discountAmount

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
                text = "⚡ SIMULADOR INTERACTIVO // DEMO EN VIVO",
                dotColor = NexoraCyanNeon,
                textColor = NexoraCyanNeon
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "ALMACÉN DOÑA ROSA",
                color = NexoraOnSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Así ven tus clientes tu tienda. Agregá productos y mirá abajo cómo se genera el mensaje de WhatsApp al instante.",
                color = NexoraOnSurfaceVariant,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 360.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Products List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                demoProducts.forEach { product ->
                    val qty = cartCounts[product.id] ?: 0
                    CyberGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = if (qty > 0) NexoraCyanNeon.copy(alpha = 0.5f) else Color(0x2000F0FF),
                        backgroundColor = Color(0xFF151B2B)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF242A3A)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = product.imageRes,
                                    contentDescription = product.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.name,
                                    color = NexoraOnSurface,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$${product.price}",
                                    color = NexoraGoldSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            // Counter Controls
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF191F2F))
                                    .border(1.dp, Color(0x3300F0FF), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clickable {
                                            if (qty > 0) {
                                                cartCounts[product.id] = qty - 1
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Quitar",
                                        tint = if (qty > 0) NexoraCyanNeon else NexoraOutline,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Text(
                                    text = "$qty",
                                    color = if (qty > 0) NexoraCyanNeon else NexoraOnSurfaceVariant,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clickable {
                                            cartCounts[product.id] = qty + 1
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Agregar",
                                        tint = NexoraCyanNeon,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Discounts & Rules Box
            CyberGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                borderColor = Color(0x33FABC4D)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "REGLAS DE DESCUENTO AUTOMÁTICO",
                        color = NexoraGoldSecondary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Descuento VIP de cliente frecuente (-10%)",
                                color = NexoraOnSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Se aplica solo a los compradores habituales",
                                color = NexoraOnSurfaceVariant,
                                fontSize = 10.sp
                            )
                        }
                        Switch(
                            checked = vipDiscountEnabled,
                            onCheckedChange = { vipDiscountEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NexoraCyanNeon,
                                checkedTrackColor = NexoraCyanNeon.copy(alpha = 0.3f)
                            )
                        )
                    }

                    HorizontalDivider(thickness = 0.5.dp, color = Color(0x20FFFFFF), modifier = Modifier.padding(vertical = 6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Pago contado / transferencia (-5%)",
                                color = NexoraOnSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Incentiva el cobro directo sin intermediarios",
                                color = NexoraOnSurfaceVariant,
                                fontSize = 10.sp
                            )
                        }
                        Switch(
                            checked = cashDiscountEnabled,
                            onCheckedChange = { cashDiscountEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NexoraGoldSecondary,
                                checkedTrackColor = NexoraGoldSecondary.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // LIVE WHATSAPP RECEIPT PREVIEW
            CyberGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                borderColor = Color(0x4000F0FF),
                backgroundColor = WhatsAppChatBg
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ASÍ TE LLEGA EL MENSAJE",
                            color = WhatsAppGreen,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "WHATSAPP DIRECTO",
                            color = NexoraOutline,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(WhatsAppBubbleIn)
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "¡Hola! Quiero pedir:",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            if (totalItemsCount == 0) {
                                Text(
                                    text = "(Seleccioná al menos un producto arriba para ver el detalle)",
                                    color = NexoraOutline,
                                    fontSize = 12.sp
                                )
                            } else {
                                demoProducts.filter { (cartCounts[it.id] ?: 0) > 0 }.forEach { p ->
                                    val count = cartCounts[p.id] ?: 0
                                    val lineTotal = count * p.price
                                    Text(
                                        text = "${p.emoji} ${p.name} ×$count — $$lineTotal",
                                        color = WhatsAppTextPrimary,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }

                            if (discountAmount > 0) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Descuento aplicado ($discountPercent%): -$$discountAmount",
                                    color = NexoraGoldSecondary,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0x33FFFFFF))
                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Total: $$totalFinal",
                                color = NexoraCyanNeon,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = if (cashDiscountEnabled) "Pago con transferencia / efectivo" else "Pago a convenir",
                                color = WhatsAppTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    WhatsAppActionButton(
                        text = "Probar 30 días con mi catálogo",
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "demo_whatsapp_cta"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Lo armamos nosotros · Sin pagar nada por adelantado",
                        color = NexoraOutline,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
