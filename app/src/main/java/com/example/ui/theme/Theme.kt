package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NexoraColorScheme = darkColorScheme(
    primary = NexoraCyanNeon,
    onPrimary = NexoraNavyBackground,
    primaryContainer = NexoraCyanNeon,
    onPrimaryContainer = NexoraNavyBackground,
    secondary = NexoraGoldSecondary,
    onSecondary = NexoraNavyBackground,
    secondaryContainer = NexoraGoldContainer,
    onSecondaryContainer = NexoraNavyBackground,
    background = NexoraNavyBackground,
    onBackground = NexoraOnSurface,
    surface = NexoraSurface,
    onSurface = NexoraOnSurface,
    surfaceVariant = NexoraSurfaceCard,
    onSurfaceVariant = NexoraOnSurfaceVariant,
    outline = NexoraOutline,
    outlineVariant = NexoraOutlineVariant,
    error = NexoraError
)

@Composable
fun NexoraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NexoraColorScheme,
        typography = Typography,
        content = content
    )
}

// Backward-compatible alias
@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    NexoraTheme(content = content)
}
