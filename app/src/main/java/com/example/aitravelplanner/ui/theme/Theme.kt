package com.example.aitravelplanner.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary          = Blue900,
    onPrimary        = White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue900,
    secondary        = Orange500,
    onSecondary      = White,
    secondaryContainer = Orange50,
    onSecondaryContainer = Orange700,
    tertiary         = Green600,
    onTertiary       = White,
    tertiaryContainer = Green50,
    onTertiaryContainer = Green800,
    background       = LightGray,
    onBackground     = DarkNavy,
    surface          = White,
    onSurface        = DarkNavy,
    surfaceVariant   = Blue50,
    outline          = MidGray
)

private val DarkColorScheme = darkColorScheme(
    primary          = Blue500,
    onPrimary        = DarkNavy,
    primaryContainer = Blue900,
    onPrimaryContainer = Blue100,
    secondary        = Orange200,
    onSecondary      = DarkNavy,
    secondaryContainer = Orange700,
    onSecondaryContainer = Orange50,
    tertiary         = Green200,
    onTertiary       = DarkNavy,
    tertiaryContainer = Green800,
    onTertiaryContainer = Green50,
    background       = DarkBackground,
    onBackground     = White,
    surface          = DarkSurface,
    onSurface        = White,
    surfaceVariant   = DarkCard,
    outline          = MidGray
)

@Composable
fun AITravelPlannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
