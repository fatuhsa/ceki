package com.sanxmon.ceki.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Midnight — near-black base with the signature yellow accent and red danger
 * color, exactly per the editorial design system: Base #0D0D0D, Surface
 * #171717, SurfaceRaised #212121, Accent #FFD600, Danger #FF3B30. High
 * contrast and fast to read mid-game; yellow is the only accent, red is
 * reserved for danger/reset.
 */
object MidnightTheme {
    const val ID = "midnight"
    const val DISPLAY_NAME = "Midnight"

    val colors = ThemeColors(
        isDark = true,
        background = Color(0xFF0D0D0D),
        surface = Color(0xFF171717),
        surfaceElevated = Color(0xFF212121),
        surfacePressed = Color(0xFF2E2E2E),
        overlay = Color(0xFF000000),
        primary = Color(0xFFFFD600),
        onPrimary = Color(0xFF0D0D0D),
        secondary = Color(0xFF9A9A94),
        accent = Color(0xFFFFD600),
        accentPressed = Color(0xFF8A7500),
        onAccent = Color(0xFF0D0D0D),
        text = Color(0xFFF5F5F0),
        textMuted = Color(0xFF9A9A94),
        textFaint = Color(0xFF6E6E68),
        divider = Color(0xFF2A2A2A),
        inputField = Color(0xFF212121),
        inputBorder = Color(0xFF2A2A2A),
        success = Color(0xFF4E9B5C),
        warning = Color(0xFFE0A84C),
        error = Color(0xFFFF3B30),
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
