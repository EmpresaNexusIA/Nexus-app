package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.SendToMobile
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.CyberGlassCard
import com.example.ui.components.CyberGridCanvas
import com.example.ui.components.HudPillBadge
import com.example.ui.components.NexoraLogo
import com.example.ui.components.ProductGrid
import com.example.ui.components.WhatsAppActionButton
import com.example.ui.theme.NexoraCyanNeon
import com.example.ui.theme.NexoraGoldAccent
import com.example.ui.theme.NexoraGoldSecondary
import com.example.ui.theme.NexoraNavyBackground
import com.example.ui.theme.NexoraOnSurface
import com.example.ui.theme.NexoraOnSurfaceVariant
import com.example.ui.theme.NexoraOutline
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceHigh
import com.example.ui.theme.NexoraSurfaceLow
import com.example.ui.theme.NexoraSurfaceLowest
import com.example.ui.theme.WhatsAppBubbleIn
import com.example.ui.theme.WhatsAppBubbleOut
import com.example.ui.theme.WhatsAppChatBg
import com.example.ui.theme.WhatsAppCheckBlue
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppTextPrimary
import com.example.ui.theme.WhatsAppTextSecondary
import com.example.util.WhatsAppHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LandingScreen(
    innerPadding: PaddingValues,
    onNavigateToDemo: () -> Unit,
    onNavigateToAssistant: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Category Selector State
    var selectedCategory by remember { mutableStateOf("ropa") }

    // Category message mapping
    val categoryMessages = remember {
        mapOf(
            "ropa" to "Para talles, colores y stock al instante sin andar mandando fotos sueltas.",
            "comida" to "Ideal para rotiserías y delivery: te eligen guarnición y te llega el pedido limpio.",
            "skincare" to "Organizá rutinas por tipo de piel y combos con descuento automático.",
            "accesorios" to "Mostrá variantes de color, grabados o medidas sin mensajes cruzados.",
            "verduleria" to "Precios por kilo o por bolsón que actualizás en un minuto cada mañana.",
            "pasteleria" to "Reservas por fecha con anticipación y detalle exacto de rellenos.",
            "kiosco" to "El vecino pide golosinas o bebidas frías y pasa a retirar sin hacer cola.",
            "ferreteria" to "Tornillos, herramientas y pinturas catalogadas por medida para responder rápido.",
            "general" to "Cualquier producto físico con precio que quieras mostrar sin vueltas."
        )
    }

    // FAQ Accordion State (open question indices)
    var openFaqIndex by remember { mutableIntStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NexoraNavyBackground)
    ) {
        // Futuristic Cyber Grid Canvas overlay
        CyberGridCanvas(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ==========================================
            // 1. FIXED HEADER
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D1322).copy(alpha = 0.95f))
                    .border(
                        width = 1.dp,
                        color = Color(0x2000F0FF),
                        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Logo + Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NexoraLogo(fontSize = 20)
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NexoraGoldSecondary.copy(alpha = 0.15f))
                                .border(1.dp, NexoraGoldSecondary.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "⚡ 0% COMISIÓN",
                                color = NexoraGoldSecondary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    // Header Avatar
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF191F2F))
                            .border(1.dp, NexoraCyanNeon.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = NexoraCyanNeon,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // 2. HERO SECTION
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Pill with Glow
                HudPillBadge(
                    text = "⚡ 0% COMISIÓN POR VENTA — PARA SIEMPRE",
                    dotColor = NexoraCyanNeon,
                    textColor = NexoraCyanNeon
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Giant Title
                Text(
                    text = "TU TIENDA ONLINE, LISTA HOY.",
                    color = NexoraOnSurface,
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = (-0.5).sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "PEDIDOS DIRECTO A TU WHATSAPP.",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        brush = Brush.horizontalGradient(
                            listOf(NexoraCyanNeon, NexoraGoldSecondary)
                        ),
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        lineHeight = 30.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Subtitle
                Text(
                    text = "Poné el link en tu bio de Instagram. Tus clientes eligen productos, aplican sus descuentos, y vos recibís el pedido ordenado — con caja, clientes VIP y todo desde el celu.",
                    color = NexoraOnSurfaceVariant,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 380.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // CTA Buttons
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 340.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WhatsAppActionButton(
                        text = "Probar 30 días con mi catálogo",
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "hero_whatsapp_cta"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF191F2F))
                            .border(1.dp, Color(0x3300F0FF), RoundedCornerShape(8.dp))
                            .clickable {
                                scope.launch {
                                    scrollState.animateScrollTo(1350)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "VER CÓMO FUNCIONA",
                                color = NexoraCyanNeon,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "Scroll down",
                                tint = NexoraCyanNeon,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Lo armamos nosotros · Sin pagar nada por adelantado",
                        color = NexoraOutline,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Pilot Strip with Checks
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 360.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PilotCheckItem(text = "30 días con tu catálogo real")
                    PilotCheckItem(text = "0% comisión, para siempre")
                    PilotCheckItem(text = "Hecho en Rosario, te atendemos en persona")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // STAR PIECE: DUAL PHONE MOCKUP (HUD)
                DualPhoneHudMockup(onTryLiveDemo = onNavigateToDemo)

                Spacer(modifier = Modifier.height(14.dp))

                // Asistente IA + Google Maps Card
                CyberGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 380.dp)
                        .clickable { onNavigateToAssistant() },
                    borderColor = Color(0x6600F0FF),
                    backgroundColor = Color(0xFF151B2B)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NexoraCyanNeon.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Asistente IA",
                                tint = NexoraCyanNeon,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "NEXORA ASISTENTE IA 📍",
                                color = NexoraCyanNeon,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Consultá ideas y zonas de Rosario con Maps",
                                color = NexoraOnSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Abrir",
                            tint = NexoraGoldSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ==========================================
            // CATÁLOGO DIGITAL RESPONSIVO (PRODUCT GRID)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "// VIDRIERA ONLINE EN VIVO",
                    color = NexoraCyanNeon,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "CATÁLOGO DE PRODUCTOS",
                    color = NexoraOnSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tus productos organizados en una grilla ágil y moderna. Tu cliente elige y pide directo por WhatsApp.",
                    color = NexoraOnSurfaceVariant,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 360.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ProductGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 480.dp),
                    minColumnWidth = 156.dp,
                    gridHeight = 540.dp
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ==========================================
            // 3. CÓMO FUNCIONA
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "// PASO A PASO",
                    color = NexoraCyanNeon,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "CÓMO FUNCIONA",
                    color = NexoraOnSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Sin vueltas ni configuraciones raras. En tres momentos ya estás vendiendo ordenado.",
                    color = NexoraOnSurfaceVariant,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 340.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3 Cards: 01, 02, 03
                StepCard(
                    number = "01",
                    icon = Icons.AutoMirrored.Filled.Chat,
                    title = "Una charla y tu catálogo",
                    description = "Nos pasás fotos, precios y lo que vendés. Nosotros armamos tu tienda completa sin que toques una sola línea de código."
                )

                Spacer(modifier = Modifier.height(12.dp))

                StepCard(
                    number = "02",
                    icon = Icons.Default.Link,
                    title = "El link va a tu bio",
                    description = "Tus clientes entran desde Instagram como a cualquier link. Sin app, sin cuenta nueva, ni registros molestos.",
                    numberColor = NexoraGoldSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                StepCard(
                    number = "03",
                    icon = Icons.Default.CheckCircle,
                    title = "El pedido entra ordenado",
                    description = "Te llega armado producto por producto, con el total exacto y los descuentos aplicados. Listo para cobrar y despachar."
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ==========================================
            // 4. LO QUE TENÉS (6 Tarjetas Glass)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF080E1D).copy(alpha = 0.6f))
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "// HERRAMIENTAS REALES",
                    color = NexoraGoldSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "LO QUE TENÉS",
                    color = NexoraOnSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Hecho a la medida del que está detrás del mostrador todos los días.",
                    color = NexoraOnSurfaceVariant,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 340.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                val features = listOf(
                    FeatureItem(
                        icon = Icons.Default.AutoAwesome,
                        title = "Link mágico para tu bio",
                        desc = "Tienda profesional en días, no semanas. Sin app rara que tus clientes tengan que descargar.",
                        isCyan = true
                    ),
                    FeatureItem(
                        icon = Icons.AutoMirrored.Filled.Chat,
                        title = "Pedidos perfectos por WhatsApp",
                        desc = "Se termina el \"pasame el precio\" y los mensajes que se pierden entre historias y audios.",
                        isCyan = false
                    ),
                    FeatureItem(
                        icon = Icons.Default.LocalAtm,
                        title = "Descuentos pro + VIP",
                        desc = "Automáticos: por medio de pago (como efectivo o transferencia) o especiales para tus clientes de siempre.",
                        isCyan = true
                    ),
                    FeatureItem(
                        icon = Icons.Default.Store,
                        title = "Caja en un toque",
                        desc = "Sabés al instante qué cobraste y qué falta entregar. El cierre del día, simple y sin dolores de cabeza.",
                        isCyan = false
                    ),
                    FeatureItem(
                        icon = Icons.Default.Groups,
                        title = "Tus clientes, registrados",
                        desc = "Quién compra seguido, quién merece el descuento de siempre. Todo anotado automáticamente sin cuaderno.",
                        isCyan = true
                    ),
                    FeatureItem(
                        icon = Icons.Default.Sync,
                        title = "Cambio un precio, cambia al instante",
                        desc = "La tienda lee en vivo desde tu panel: no hay precio viejo ni desactualizado en ningún lado.",
                        isCyan = false
                    )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    features.forEach { item ->
                        CyberGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            borderColor = if (item.isCyan) Color(0x3300F0FF) else Color(0x33FABC4D)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (item.isCyan) NexoraCyanNeon.copy(alpha = 0.12f)
                                            else NexoraGoldSecondary.copy(alpha = 0.12f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title,
                                        tint = if (item.isCyan) NexoraCyanNeon else NexoraGoldSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = item.title.uppercase(),
                                        color = NexoraOnSurface,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.SansSerif,
                                        letterSpacing = 0.2.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.desc,
                                        color = NexoraOnSurfaceVariant,
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ==========================================
            // 5. TABLA COMPARATIVA
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "// BALANZA DE SOLUCIONES",
                    color = NexoraCyanNeon,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "COMPARÁ VOS MISMO",
                    color = NexoraOnSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Deslizá para ver por qué Nexora es diferente a lidiar con mensajes sueltos o comisiones caras.",
                    color = NexoraOnSurfaceVariant,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 340.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ComparisonTable()
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ==========================================
            // 6. PERFECTO PARA (Interactive Chips)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "// RUBROS QUE YA VENDEN",
                    color = NexoraGoldSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "PERFECTO PARA",
                    color = NexoraOnSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tengas 5 productos o 400, Nexora se adapta a tu manera de trabajar.",
                    color = NexoraOnSurfaceVariant,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                val categories = listOf(
                    NicheCategory("ropa", "Ropa", Icons.Default.Checkroom, true),
                    NicheCategory("comida", "Comida casera", Icons.Default.LunchDining, false),
                    NicheCategory("skincare", "Makeup y skincare", Icons.Default.Face, true),
                    NicheCategory("accesorios", "Accesorios", Icons.Default.Watch, false),
                    NicheCategory("verduleria", "Verdulería", Icons.Default.Eco, true),
                    NicheCategory("pasteleria", "Pastelería", Icons.Default.Cake, false),
                    NicheCategory("kiosco", "Kiosco", Icons.Default.Store, true),
                    NicheCategory("ferreteria", "Ferretería", Icons.Default.Construction, false),
                    NicheCategory("general", "Lo que vendas", Icons.Default.Category, true)
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 420.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = selectedCategory == cat.id
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(100.dp))
                                .background(
                                    if (isSelected) Color(0xFF242A3A) else Color(0xFF191F2F)
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) NexoraCyanNeon else Color(0x33FFFFFF),
                                    shape = RoundedCornerShape(100.dp)
                                )
                                .clickable {
                                    selectedCategory = cat.id
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = cat.icon,
                                    contentDescription = cat.name,
                                    tint = if (cat.isCyan) NexoraCyanNeon else NexoraGoldSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = cat.name.uppercase(),
                                    color = if (isSelected) NexoraCyanNeon else NexoraOnSurface,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive Dynamic Feedback Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 400.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF151B2B))
                        .border(1.dp, Color(0x3300F0FF), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Rubro",
                                tint = NexoraCyanNeon,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PARA ESTE RUBRO:",
                                color = NexoraCyanNeon,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = categoryMessages[selectedCategory]
                                ?: "Adaptado a tu manera de cobrar y despachar.",
                            color = NexoraOnSurface,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Notice Callout
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 400.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NexoraCyanNeon.copy(alpha = 0.06f))
                        .border(1.dp, NexoraCyanNeon.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = NexoraCyanNeon,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("¿Tenés tu lista en un cuaderno o Excel? ")
                            }
                            withStyle(style = SpanStyle(color = NexoraOnSurfaceVariant)) {
                                append("Nos la mandás por WhatsApp y te la convertimos en catálogo interactivo sin esfuerzo.")
                            }
                        },
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ==========================================
            // 7. EL PILOTO (Bloque con Glow Dorado)
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
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
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Badge Piloto
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
                                text = "PROGRAMA PILOTO ROSARIO",
                                color = NexoraGoldSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Probalá con tu catálogo real, 30 días",
                            color = NexoraOnSurface,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.SansSerif
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

                        // Checklist
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
                            testTag = "piloto_whatsapp_cta"
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
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ==========================================
            // 8. FAQ (Acordeón con 8 preguntas exactas)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "// PREGUNTAS FRECUENTES",
                    color = NexoraCyanNeon,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "DUDAS CLARAS",
                    color = NexoraOnSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Respuestas honestas, sin vueltas ni letras chicas.",
                    color = NexoraOnSurfaceVariant,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                val faqs = listOf(
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    faqs.forEachIndexed { index, faq ->
                        val isOpen = openFaqIndex == index
                        val rotation by animateFloatAsState(
                            targetValue = if (isOpen) 180f else 0f,
                            label = "faq_arrow"
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
                                        openFaqIndex = if (isOpen) -1 else index
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
                                        contentDescription = "Expandir",
                                        tint = if (isOpen) NexoraCyanNeon else NexoraOutline,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .rotate(rotation)
                                    )
                                }

                                AnimatedVisibility(visible = isOpen) {
                                    Column {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        HorizontalDivider(
                                            thickness = 1.dp,
                                            color = Color(0x2000F0FF)
                                        )
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

            Spacer(modifier = Modifier.height(36.dp))

            // ==========================================
            // 9. CTA FINAL
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CyberGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 420.dp),
                    borderColor = Color(0x6600F0FF),
                    backgroundColor = Color(0xFF151B2B)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NexoraCyanNeon.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.SendToMobile,
                                contentDescription = "Enviar",
                                tint = NexoraCyanNeon,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Tu próximo pedido puede llegar ordenado.",
                            color = NexoraOnSurface,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Escribinos ahora, contanos qué vendés y te dejamos la tienda armada para que la pruebes con tus clientes.",
                            color = NexoraOnSurfaceVariant,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        WhatsAppActionButton(
                            text = "Probar 30 días con mi catálogo",
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "final_cta_button"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "RESPONDEMOS DE LUNES A SÁBADO DESDE ROSARIO",
                            color = NexoraOutline,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ==========================================
            // 10. FOOTER
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NexoraSurfaceLowest)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NexoraLogo(fontSize = 22)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "HECHO EN ROSARIO, ARGENTINA 🇦🇷",
                    color = NexoraGoldSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tiendas online para instagramers y comercios con checkout directo a WhatsApp y 0% comisión por venta.",
                    color = NexoraOnSurfaceVariant,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 320.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Términos",
                        color = NexoraOnSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
                            WhatsAppHelper.openWhatsApp(context, "Hola, tengo una consulta sobre los Términos de Nexora")
                        }
                    )
                    Text(
                        text = "  •  ",
                        color = NexoraOutline
                    )
                    Text(
                        text = "Privacidad",
                        color = NexoraOnSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
                            WhatsAppHelper.openWhatsApp(context, "Hola, tengo una consulta sobre la Política de Privacidad de Nexora")
                        }
                    )
                    Text(
                        text = "  •  ",
                        color = NexoraOutline
                    )
                    Text(
                        text = "WhatsApp Soporte",
                        color = NexoraCyanNeon,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            WhatsAppHelper.openWhatsApp(context, "Hola, tengo una consulta sobre Nexora")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "© 2025 NEXORA LABS. TODOS LOS DERECHOS RESERVADOS.",
                    color = NexoraOutline,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// HELPER COMPOSABLES FOR LANDING
// -------------------------------------------------------------

@Composable
fun PilotCheckItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF151B2B).copy(alpha = 0.7f))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Check",
            tint = WhatsAppGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = NexoraOnSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StepCard(
    number: String,
    icon: ImageVector,
    title: String,
    description: String,
    numberColor: Color = NexoraCyanNeon
) {
    CyberGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 380.dp),
        borderColor = Color(0x3000F0FF)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = number,
                    color = numberColor,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black
                )
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = NexoraOutline,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title.uppercase(),
                color = NexoraOnSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 0.2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                color = NexoraOnSurfaceVariant,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun ChecklistRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Listo",
            tint = WhatsAppGreen,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = NexoraOnSurface,
            fontSize = 13.sp
        )
    }
}

// -------------------------------------------------------------
// DUAL PHONE HUD MOCKUP (STAR PIECE)
// -------------------------------------------------------------

@Composable
fun DualPhoneHudMockup(
    onTryLiveDemo: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 380.dp)
    ) {
        // Top HUD Marker Tag
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "[SYS.HUD // v4.2]",
                color = NexoraOutline,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(NexoraCyanNeon)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "TELEMETRÍA // PEDIDO ENTRANTE",
                    color = NexoraCyanNeon,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Phone 1: Store View
        CyberGlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = Color(0x4000F0FF),
            backgroundColor = Color(0xFF151B2B),
            showCornerReticles = true
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Store Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF191F2F))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(NexoraGoldSecondary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "R",
                                color = NexoraGoldSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Almacén Doña Rosa",
                                color = NexoraOnSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Abierto · Rosario Centro",
                                color = NexoraCyanNeon,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = NexoraOutline,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2x2 Products Grid
                val products = listOf(
                    ProductItem(
                        name = "Pan casero",
                        price = "$1.200",
                        imageRes = R.drawable.img_pan_frances
                    ),
                    ProductItem(
                        name = "Queso cremoso",
                        price = "$1.800",
                        imageRes = R.drawable.img_queso
                    ),
                    ProductItem(
                        name = "Medialunas ×6",
                        price = "$1.400",
                        imageRes = R.drawable.img_medialunas
                    ),
                    ProductItem(
                        name = "Café 1/2 kg",
                        price = "$2.900",
                        imageRes = R.drawable.img_cafe
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProductMockupCard(product = products[0], modifier = Modifier.weight(1f))
                    ProductMockupCard(product = products[1], modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProductMockupCard(product = products[2], modifier = Modifier.weight(1f))
                    ProductMockupCard(product = products[3], modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Floating Cart Bar inside phone
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF242A3A))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "CARRITO ACTIVO",
                            color = NexoraOnSurfaceVariant,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "🛒 3 productos — $5.600",
                            color = NexoraCyanNeon,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(WhatsAppGreen)
                            .clickable {
                                WhatsAppHelper.openWhatsApp(context)
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "PEDIR POR WHATSAPP",
                            color = Color(0xFF050811),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }

        // Phone 2: Overlapping WhatsApp Real Chat Bubble (Superposition)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .align(Alignment.End)
                .padding(top = 10.dp)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(WhatsAppChatBg)
                .border(1.dp, Color(0x3300F0FF), RoundedCornerShape(12.dp))
                .padding(10.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header Bar of WhatsApp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF151B2B).copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Seguro",
                            tint = WhatsAppGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Chat seguro",
                            color = WhatsAppGreen,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Text(
                        text = "11:42 AM",
                        color = NexoraOutline,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Inbound Customer Order Bubble
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 8.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
                        .background(WhatsAppBubbleIn)
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "¡Hola! Quiero pedir:",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "🍞 Pan casero ×2 — $2.400\n🧀 Queso cremoso ×1 — $1.800\n🥐 Medialunas ×6 — $1.400",
                            color = WhatsAppTextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        HorizontalDivider(thickness = 0.5.dp, color = Color(0x33FFFFFF))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Total: $5.600",
                            color = NexoraCyanNeon,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Pago con transferencia",
                            color = WhatsAppTextSecondary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "11:42 ✓✓",
                            color = WhatsAppTextSecondary,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Outbound Merchant Bubble
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .align(Alignment.End)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 0.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
                        .background(WhatsAppBubbleOut)
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "¡Anotado! Sale hoy antes de las 18 🙌",
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "11:43 ",
                                color = WhatsAppTextSecondary,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "✓✓",
                                color = WhatsAppCheckBlue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to open Interactive Live Demo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF191F2F))
                .border(1.dp, Color(0x3300F0FF), RoundedCornerShape(6.dp))
                .clickable { onTryLiveDemo() }
                .padding(vertical = 8.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PhoneIphone,
                    contentDescription = "Demo en vivo",
                    tint = NexoraCyanNeon,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "PROBAR DEMO INTERACTIVA EN VIVO",
                    color = NexoraCyanNeon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun ProductMockupCard(
    product: ProductItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF191F2F))
            .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(6.dp))
            .padding(6.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(4.dp))
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
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.name,
                color = NexoraOnSurface,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Text(
                text = product.price,
                color = NexoraGoldSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

// -------------------------------------------------------------
// COMPARISON TABLE
// -------------------------------------------------------------

@Composable
fun ComparisonTable() {
    val scrollState = rememberScrollState()

    CyberGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp),
        borderColor = Color(0x3000F0FF)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                Column(modifier = Modifier.width(520.dp)) {
                    // Header Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF242A3A))
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CARACTERÍSTICA",
                            color = NexoraOnSurface,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.width(150.dp)
                        )
                        Text(
                            text = "DMs solos",
                            color = NexoraOutline,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(80.dp)
                        )
                        Text(
                            text = "Linktree",
                            color = NexoraOutline,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(80.dp)
                        )
                        Text(
                            text = "Otras apps",
                            color = NexoraOutline,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(90.dp)
                        )
                        Box(
                            modifier = Modifier
                                .width(110.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(NexoraCyanNeon.copy(alpha = 0.2f))
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "NEXORA",
                                color = NexoraCyanNeon,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    // Table Rows
                    val rows = listOf(
                        TableRowData("Pedido ordenado", "❌", "❌", "✅", "✅", true),
                        TableRowData("Catálogo con precios", "❌", "⚠️", "✅", "✅", false),
                        TableRowData("Caja y cierre del día", "❌", "❌", "⚠️", "✅", true),
                        TableRowData("Clientes VIP auto", "❌", "❌", "❌", "✅", false),
                        TableRowData("Comisión por venta", "0%", "0%", "% comisión", "0% SIEMPRE", true),
                        TableRowData("Te lo arman en persona", "❌", "❌", "❌", "✅ (Rosario)", false)
                    )

                    rows.forEach { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (row.isAlt) Color(0xFF151B2B) else Color(0xFF191F2F))
                                .padding(vertical = 9.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = row.feature,
                                color = NexoraOnSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.width(150.dp)
                            )
                            Text(
                                text = row.dm,
                                color = if (row.dm == "❌") Color(0xFFFFB4AB) else NexoraOnSurfaceVariant,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(80.dp)
                            )
                            Text(
                                text = row.linktree,
                                color = if (row.linktree == "❌") Color(0xFFFFB4AB) else if (row.linktree == "⚠️") NexoraGoldSecondary else NexoraOnSurfaceVariant,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(80.dp)
                            )
                            Text(
                                text = row.others,
                                color = if (row.others == "% comisión") Color(0xFFFFB4AB) else if (row.others == "⚠️") NexoraGoldSecondary else NexoraCyanNeon,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(90.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .width(110.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NexoraCyanNeon.copy(alpha = 0.1f))
                                    .padding(vertical = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = row.nexora,
                                    color = if (row.nexora.contains("Rosario")) NexoraGoldSecondary else NexoraCyanNeon,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Swipe Hint
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF242A3A).copy(alpha = 0.5f))
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Swipe,
                    contentDescription = "Deslizar",
                    tint = NexoraOutline,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Deslizá hacia los lados para ver todo",
                    color = NexoraOutline,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

// -------------------------------------------------------------
// MODELS
// -------------------------------------------------------------

data class ProductItem(
    val name: String,
    val price: String,
    val imageRes: Int
)

data class FeatureItem(
    val icon: ImageVector,
    val title: String,
    val desc: String,
    val isCyan: Boolean
)

data class NicheCategory(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val isCyan: Boolean
)

data class FaqItem(
    val q: String,
    val a: String
)

data class TableRowData(
    val feature: String,
    val dm: String,
    val linktree: String,
    val others: String,
    val nexora: String,
    val isAlt: Boolean
)
