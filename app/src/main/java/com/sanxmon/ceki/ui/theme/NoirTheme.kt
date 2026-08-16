package com.sanxmon.ceki.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Noir — near-black background, white text and a strong red accent. Sharp,
 * high-contrast and bold with minimal decoration.
 */
object NoirTheme {
    const val ID = "noir"
    const val DISPLAY_NAME = "Noir"

    val colors = ThemeColors(
        isDark = true,
        background = Color(0xFF0A0A0A),
        surface = Color(0xFF141414),
        surfaceElevated = Color(0xFF1E1E1E),
        surfacePressed = Color(0xFF2E2E2E),
        overlay = Color(0xFF000000),
        primary = Color(0xFFFFFFFF),
        onPrimary = Color(0xFF000000),
        secondary = Color(0xFFB0B0B0),
        accent = Color(0xFFE53935),
        accentPressed = Color(0xFFB71C1C),
        onAccent = Color(0xFFFFFFFF),
        text = Color(0xFFFFFFFF),
        textMuted = Color(0xFFB3B3B3),
        textFaint = Color(0xFF8A8A8A),
        divider = Color(0xFF2A2A2A),
        inputField = Color(0xFF1E1E1E),
        inputBorder = Color(0xFF3A3A3A),
        success = Color(0xFF4CAF50),
        warning = Color(0xFFFFC107),
        error = Color(0xFFE53935),
        onError = Color(0xFFFFFFFF),
    )

    val theme = AppTheme(
        id = ID,
        displayName = DISPLAY_NAME,
        colors = colors,
        typography = DefaultCekiTypography,
        shapes = DefaultCekiShapes,
    )
}
