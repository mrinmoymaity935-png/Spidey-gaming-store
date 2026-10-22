package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SpideyRedPrimary,
    onPrimary = Color.White,
    primaryContainer = SpideyDarkSurface,
    onPrimaryContainer = SpideyRedGlow,
    secondary = SpideyElectric,
    onSecondary = Color.White,
    background = SpideyDarkBg,
    onBackground = Color(0xFFF1F5F9),
    surface = SpideyDarkSurface,
    onSurface = Color(0xFFF1F5F9),
    outline = Color(0xFF334155),
    error = SpideyError
)

private val LightColorScheme = lightColorScheme(
    primary = SpideyRedPrimary,
    onPrimary = Color.White,
    primaryContainer = SpideyRedLight,
    onPrimaryContainer = SpideyRedDark,
    secondary = SpideyElectric,
    onSecondary = Color.White,
    secondaryContainer = SpideyBlueLight,
    onSecondaryContainer = SpideyBlueDark,
    background = SpideyBgLight,
    onBackground = SpideyTextPrimary,
    surface = SpideyCardBg,
    onSurface = SpideyTextPrimary,
    surfaceVariant = Color(0xFFFFF7F8),
    onSurfaceVariant = SpideyTextSecondary,
    outline = SpideyCardBorder,
    outlineVariant = Color(0xFFFDDDE0),
    error = SpideyError,
    onError = Color.White
)

@Composable
fun SpideyTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
