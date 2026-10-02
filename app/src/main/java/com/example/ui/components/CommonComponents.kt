package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NexoraCyanNeon
import com.example.ui.theme.NexoraGoldSecondary
import com.example.ui.theme.NexoraNavyBackground
import com.example.ui.theme.NexoraOnSurface
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppHelper

@Composable
fun NexoraLogo(
    modifier: Modifier = Modifier,
    fontSize: Int = 22
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = NexoraOnSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize.sp,
                    letterSpacing = (-0.5).sp
                )
            ) {
                append("nex")
            }
            withStyle(
                style = SpanStyle(
                    color = NexoraCyanNeon,
                    fontWeight = FontWeight.Black,
                    fontSize = fontSize.sp,
                    letterSpacing = (-0.5).sp
                )
            ) {
                append("o")
            }
            withStyle(
                style = SpanStyle(
                    color = NexoraOnSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize.sp,
                    letterSpacing = (-0.5).sp
                )
            ) {
                append("ra")
            }
        },
        modifier = modifier
    )
}

@Composable
fun WhatsAppActionButton(
    text: String,
    modifier: Modifier = Modifier,
    customMessage: String? = null,
    testTag: String = "whatsapp_cta_button",
    minHeight: Dp = 48.dp
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = minHeight)
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(WhatsAppGreen, Color(0xFF20BA5A))
                )
            )
            .border(
                width = 1.dp,
                color = Color(0x66FFFFFF),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                WhatsAppHelper.openWhatsApp(context, customMessage)
            }
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Chat,
                contentDescription = "WhatsApp",
                tint = Color(0xFF050811),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text.uppercase(),
                color = Color(0xFF050811),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
fun HudPillBadge(
    text: String,
    modifier: Modifier = Modifier,
    dotColor: Color = NexoraCyanNeon,
    textColor: Color = NexoraCyanNeon,
    borderColor: Color = Color(0x4000F0FF)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hud_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(Color(0xFF151B2B).copy(alpha = 0.85f))
            .border(1.dp, borderColor.copy(alpha = alpha), RoundedCornerShape(100.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor.copy(alpha = alpha))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = textColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.8.sp
            )
        }
    }
}

@Composable
fun CyberGlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = Color(0x3300F0FF),
    backgroundColor: Color = NexoraSurfaceCard,
    cornerRadius: Dp = 12.dp,
    showCornerReticles: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor.copy(alpha = 0.92f))
            .border(1.dp, borderColor, RoundedCornerShape(cornerRadius))
            .drawBehind {
                if (showCornerReticles) {
                    val reticleLen = 12.dp.toPx()
                    val strokeW = 1.5.dp.toPx()
                    val reticleColor = borderColor.copy(alpha = 0.6f)

                    // Top-Left corner reticle
                    drawLine(reticleColor, Offset(4f, 4f), Offset(4f + reticleLen, 4f), strokeW)
                    drawLine(reticleColor, Offset(4f, 4f), Offset(4f, 4f + reticleLen), strokeW)

                    // Top-Right corner reticle
                    drawLine(reticleColor, Offset(size.width - 4f, 4f), Offset(size.width - 4f - reticleLen, 4f), strokeW)
                    drawLine(reticleColor, Offset(size.width - 4f, 4f), Offset(size.width - 4f, 4f + reticleLen), strokeW)

                    // Bottom-Left corner reticle
                    drawLine(reticleColor, Offset(4f, size.height - 4f), Offset(4f + reticleLen, size.height - 4f), strokeW)
                    drawLine(reticleColor, Offset(4f, size.height - 4f), Offset(4f, size.height - 4f - reticleLen), strokeW)

                    // Bottom-Right corner reticle
                    drawLine(reticleColor, Offset(size.width - 4f, size.height - 4f), Offset(size.width - 4f - reticleLen, size.height - 4f), strokeW)
                    drawLine(reticleColor, Offset(size.width - 4f, size.height - 4f), Offset(size.width - 4f, size.height - 4f - reticleLen), strokeW)
                }
            }
    ) {
        content()
    }
}

@Composable
fun CyberGridCanvas(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val step = 32.dp.toPx()
        val dotRadius = 1.2.dp.toPx()
        val gridColor = Color(0x1500F0FF)

        var x = 0f
        while (x <= size.width) {
            var y = 0f
            while (y <= size.height) {
                drawCircle(
                    color = gridColor,
                    radius = dotRadius,
                    center = Offset(x, y)
                )
                y += step
            }
            x += step
        }
    }
}
