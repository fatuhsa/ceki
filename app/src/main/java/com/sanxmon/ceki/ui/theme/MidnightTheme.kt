package com.sanxmon.ceki.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Midnight — dark, deep blue background with a cool cyan accent. Modern
 * urban/night aesthetic with clear surface hierarchy.
 */
object MidnightTheme {
    const val ID = "midnight"
    const val DISPLAY_NAME = "Midnight"

    val colors = ThemeColors(
        isDark = true,
        background = Color(0xFF001736),
        surface = Color(0xFF001129),
        surfaceElevated = Color(0xFF0B2445),
        surfacePressed = Color(0xFF1E3F70),
        overlay = Color(0xFF000D20),
        primary = Color(0xFF00BBFA),
        onPrimary = Color(0xFF001736),
        secondary = Color(0xFF89B4FA),
        accent = Color(0xFF7DCFFF),
        accentPressed = Color(0xFF4FA8DE),
        onAccent = Color(0xFF001736),
        text = Color(0xFFCDD6F4),
        textMuted = Color(0xFFB3BCE0),
        textFaint = Color(0xFFC9D1EB),
        divider = Color(0xFF14305C),
        inputField = Color(0xFF0B2445),
        inputBorder = Color(0xFF14305C),
        success = Color(0xFFA6E3A1),
        warning = Color(0xFFF9E2AF),
        error = Color(0xFFF38BA8),
        onError = Color(0xFF001736),
    )

    val theme = AppTheme(
        id = ID,
        displayName = DISPLAY_NAME,
        colors = colors,
        typography = DefaultCekiTypography,
        shapes = DefaultCekiShapes,
    )
}
