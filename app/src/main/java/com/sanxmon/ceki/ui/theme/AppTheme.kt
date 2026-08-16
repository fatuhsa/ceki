package com.sanxmon.ceki.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * A complete, self-contained theme: id + display name plus color, typography
 * and shape tokens. Adding a new theme is just a new [AppTheme] in the
 * [ThemeManager] registry.
 */
data class AppTheme(
    val id: String,
    val displayName: String,
    val colors: ThemeColors,
    val typography: ThemeTypography,
    val shapes: ThemeShapes,
) {
    val isDark: Boolean get() = colors.isDark
}

/** Maps app color tokens onto the Material3 [ColorScheme] handed to Compose. */
fun ThemeColors.toColorScheme(): ColorScheme {
    val base = if (isDark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = primary,
        onPrimary = onPrimary,
        secondary = secondary,
        onSecondary = onPrimary,
        background = background,
        onBackground = text,
        surface = surface,
        onSurface = text,
        surfaceVariant = surfaceElevated,
        onSurfaceVariant = textMuted,
        surfaceContainer = surfaceElevated,
        surfaceContainerHigh = surfacePressed,
        surfaceContainerHighest = surfacePressed,
        outline = divider,
        outlineVariant = inputBorder,
        scrim = overlay,
        error = error,
        onError = onError,
    )
}

// Convenience accessors so components can read app tokens through MaterialTheme.
val MaterialTheme.appColors: ThemeColors
    @Composable get() = LocalAppTheme.current.colors

val MaterialTheme.appTypography: ThemeTypography
    @Composable get() = LocalAppTheme.current.typography

val MaterialTheme.appShapes: ThemeShapes
    @Composable get() = LocalAppTheme.current.shapes
