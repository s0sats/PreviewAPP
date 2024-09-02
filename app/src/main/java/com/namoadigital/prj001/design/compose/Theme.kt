package com.namoadigital.prj001.design.compose

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LightColorScheme = lightColorScheme(
    primary = m3_namoa_primary,
    onPrimary = m3_namoa_onPrimary,
    primaryContainer = m3_namoa_primaryContainer,
    onPrimaryContainer = m3_namoa_onPrimaryContainer,
    secondary = m3_namoa_secondary,
    onSecondary = m3_namoa_onSecondary,
    secondaryContainer = m3_namoa_secondaryContainer,
    onSecondaryContainer = m3_namoa_onSecondaryContainer,
    tertiary = m3_namoa_tertiary,
    onTertiary = m3_namoa_onTertiary,
    tertiaryContainer = m3_namoa_tertiaryContainer,
    onTertiaryContainer = m3_namoa_onTertiaryContainer,
    error = m3_namoa_error,
    onError = m3_namoa_onError,
    errorContainer = m3_namoa_errorContainer,
    onErrorContainer = m3_namoa_onErrorContainer,
    background = m3_namoa_background,
    onBackground = m3_namoa_onBackground,
    surface = m3_namoa_surface,
    onSurface = m3_namoa_onSurface,
    surfaceVariant = m3_namoa_surfaceVariant,
    onSurfaceVariant = m3_namoa_onSurfaceVariant,
    outline = m3_namoa_outline,
    inverseOnSurface = m3_namoa_inverseOnSurface,
    inverseSurface = m3_namoa_inverseSurface,
    inversePrimary = m3_namoa_inversePrimary,
    surfaceTint = m3_namoa_surfaceTint
)


@Composable
fun NamoaTheme(
    content: @Composable() () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}