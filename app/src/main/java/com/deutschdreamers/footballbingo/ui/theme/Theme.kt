package com.deutschdreamers.footballbingo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green20,
    secondary = Amber40,
    onSecondary = Color(0xFF1A1000),
    secondaryContainer = Amber90,
    onSecondaryContainer = Amber20,
    surfaceVariant = Green90,
    onSurfaceVariant = Green20,
)

private val DarkColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green40,
    onPrimaryContainer = Green90,
    secondary = Amber80,
    onSecondary = Amber20,
    secondaryContainer = Amber40,
    onSecondaryContainer = Amber90,
    surfaceVariant = Color(0xFF1E3320),
    onSurfaceVariant = Green80,
)

@Composable
fun FootballBingoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
