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
  primary = PrimaryIndigoLight,
  onPrimary = Slate950,
  primaryContainer = PrimaryIndigoDark,
  onPrimaryContainer = Color.White,
  secondary = AccentCyan,
  onSecondary = Slate950,
  secondaryContainer = Color(0xFF0E3A52),
  onSecondaryContainer = AccentCyanLight,
  tertiary = AccentTeal,
  background = Slate950,
  onBackground = Slate100,
  surface = Slate900,
  onSurface = Slate100,
  surfaceVariant = Slate800,
  onSurfaceVariant = Slate300,
  outline = Slate600
)

private val LightColorScheme = lightColorScheme(
  primary = PrimaryIndigo,
  onPrimary = Color.White,
  primaryContainer = PrimaryIndigo50,
  onPrimaryContainer = PrimaryIndigoDark,
  secondary = AccentCyan,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFE0F2FE),
  onSecondaryContainer = Color(0xFF0369A1),
  tertiary = AccentTeal,
  background = PolishCanvas,
  onBackground = PolishTextPrimary,
  surface = Color.White,
  onSurface = PolishTextPrimary,
  surfaceVariant = Slate100,
  onSurfaceVariant = Slate700,
  outline = Slate200
)

@Composable
fun SmartQueueTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  SmartQueueTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
