package com.sanxmon.ceki.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Golden — light, warm yellow/gold with high contrast dark text. Bright and
 * playful, while keeping text readable (dark warm text on cream surfaces).
 */
object GoldenTheme {
    const val ID = "golden"
    const val DISPLAY_NAME = "Golden"

    val colors = ThemeColors(
        isDark = false,
        background = Color(0xFFFFF6DC),
        surface = Color(0xFFFFFDF5),
        surfaceElevated = Color(0xFFFFEFBF),
        surfacePressed = Color(0xFFFFD98A),
        overlay = Color(0xFFF3E3B3),
        primary = Color(0xFFF0A500),
        onPrimary = Color(0xFF241A00),
        secondary = Color(0xFFE08700),
        accent = Color(0xFFFFD54A),
        onAccent = Color(0xFF3A2A00),
        text = Color(0xFF211B10),
        textMuted = Color(0xFF6B6048),
        textFaint = Color(0xFF8A7D5E),
        divider = Color(0xFFE8D9A8),
        inputField = Color(0xFFFFFFFF),
        inputBorder = Color(0xFFE3D08F),
        success = Color(0xFF2E7D32),
        warning = Color(0xFFB26A00),
        error = Color(0xFFC62828),
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
