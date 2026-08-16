package com.sanxmon.ceki.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Typography tokens for one theme. Components reference these instead of
 * hardcoding text styles, so a theme can re-style the whole app.
 */
data class ThemeTypography(
    val title: TextStyle,
    val heading: TextStyle,
    val body: TextStyle,
    val bodySmall: TextStyle,
    val label: TextStyle,
    val caption: TextStyle,
    val button: TextStyle,
    val keypad: TextStyle,
    val scoreLarge: TextStyle,
    val score: TextStyle,
    val scoreSmall: TextStyle,
) {
    fun toTypography(): Typography = Typography(
        headlineLarge = title,
        titleLarge = heading,
        titleMedium = button,
        bodyLarge = body,
        bodyMedium = bodySmall,
        labelLarge = label,
        labelMedium = caption,
        labelSmall = caption,
    )
}

/** Baseline typography shared by all themes (themes may override). */
val DefaultCekiTypography = ThemeTypography(
    title = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.5).sp),
    heading = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.3).sp),
    body = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal),
    label = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp),
    caption = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
    button = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.5.sp),
    keypad = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold),
    scoreLarge = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp, fontFeatureSettings = "tnum"),
    score = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp, fontFeatureSettings = "tnum"),
    scoreSmall = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Black, fontFeatureSettings = "tnum"),
)
