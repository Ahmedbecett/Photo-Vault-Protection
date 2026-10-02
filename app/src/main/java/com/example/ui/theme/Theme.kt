package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VaultColorScheme = darkColorScheme(
    primary = VaultEmerald,
    onPrimary = Color.Black,
    secondary = VaultCyan,
    onSecondary = Color.Black,
    tertiary = VaultAmber,
    onTertiary = Color.Black,
    background = VaultBlack,
    onBackground = Color.White,
    surface = VaultDarkSurface,
    onSurface = Color.White,
    surfaceVariant = VaultCardBg,
    onSurfaceVariant = VaultTextMuted,
    outline = VaultBorder,
    error = VaultRed
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = VaultColorScheme,
        typography = Typography,
        content = content
    )
}
