package com.example.restcountriesapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AtlasNavy,
    onPrimary = AtlasWhite,
    secondary = AtlasTeal,
    onSecondary = AtlasWhite,
    tertiary = AtlasTerracotta,
    onTertiary = AtlasWhite,
    background = AtlasIvory,
    onBackground = AtlasMainText,
    surface = AtlasWhite,
    onSurface = AtlasMainText,
    surfaceVariant = AtlasSurfaceVariant,
    onSurfaceVariant = AtlasSecondaryText,
    outline = AtlasSubtleOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = AtlasPrimaryDark,
    onPrimary = AtlasBgDark,
    secondary = AtlasSecondaryDark,
    onSecondary = AtlasBgDark,
    tertiary = AtlasTerracottaDark,
    onTertiary = AtlasBgDark,
    background = AtlasBgDark,
    onBackground = AtlasMainTextDark,
    surface = AtlasSurfaceDark,
    onSurface = AtlasMainTextDark,
    surfaceVariant = AtlasSurfaceVariantDark,
    onSurfaceVariant = AtlasSecondaryTextDark,
    outline = AtlasOutlineDark
)

@Composable
fun RestCountriesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
