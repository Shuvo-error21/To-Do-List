package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LavenderPrimary,
    onPrimary = CharcoalDark,
    primaryContainer = LavenderContainer,
    onPrimaryContainer = LavenderLight,
    secondary = IceBlueAccent,
    onSecondary = CharcoalDark,
    secondaryContainer = Color(0xFF1E293B),
    onSecondaryContainer = IceBlueAccent,
    tertiary = AmberStreak,
    onTertiary = CharcoalDark,
    tertiaryContainer = Color(0xFF382510),
    onTertiaryContainer = AmberStreak,
    background = ElegantDarkBackground,
    onBackground = ElegantTextPrimary,
    surface = ElegantDarkSurface,
    onSurface = ElegantTextPrimary,
    surfaceVariant = ElegantDarkSurfaceVariant,
    onSurfaceVariant = ElegantTextSecondary,
    outline = ElegantDarkBorder,
    outlineVariant = ElegantDarkBorderSubtle,
    error = RosePriority,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = LavenderPrimary,
    onPrimary = CharcoalDark,
    primaryContainer = Color(0xFFEDE7F6),
    onPrimaryContainer = LavenderDark,
    secondary = IceBlueAccent,
    onSecondary = CharcoalDark,
    secondaryContainer = Color(0xFFE3F2FD),
    onSecondaryContainer = Color(0xFF0D47A1),
    tertiary = AmberStreak,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFFF3E0),
    onTertiaryContainer = Color(0xFFE65100),
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightCardBorder,
    error = RosePriority,
    onError = Color.White
)

@Composable
fun GoalAITheme(
    darkTheme: Boolean = true, // Default to Elegant Dark experience
    dynamicColor: Boolean = false, // false by default for signature brand aesthetics
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

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) = GoalAITheme(darkTheme, dynamicColor, content)

