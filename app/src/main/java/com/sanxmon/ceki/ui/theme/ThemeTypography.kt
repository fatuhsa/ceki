package com.sanxmon.ceki.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sanxmon.ceki.R

/**
 * Display font for headlines and scores: Archivo Black (single weight, Black).
 */
private val DisplayFont = FontFamily(
    Font(R.font.archivo_black, weight = FontWeight.Black),
)

/**
 * Handwritten accent font, used sparingly: Caveat Bold (variable wght axis).
 */
private val AccentFont = FontFamily(
    Font(R.font.caveat, weight = FontWeight.Bold),
)

/**
 * Body font: Inter (variable wght 400/600/700). Android applies the wght axis
 * from the declared weight; numeric styles also request tabular figures
 * (`tnum`) so digits never shift width while scores change.
 */
private val BodyFont = FontFamily(
    Font(R.font.inter, weight = FontWeight.Normal),
    Font(R.font.inter, weight = FontWeight.SemiBold),
    Font(R.font.inter, weight = FontWeight.Bold),
)

/**
 * Typography tokens for one theme. Components reference these instead of
 * hardcoding text styles, so a theme can re-style the whole app.
 */
data class ThemeTypography(
    /** App title / dialog titles — display font, large. */
    val title: TextStyle,
    /** Section headings — body font, bold. */
    val heading: TextStyle,
    /** Body copy (16sp, Inter). */
    val body: TextStyle,
    /** Small body copy (14sp, Inter). */
    val bodySmall: TextStyle,
    /** Eyebrow / badge labels — caps, wide tracking. */
    val label: TextStyle,
    /** Captions and hints. */
    val caption: TextStyle,
    /** Button labels. */
    val button: TextStyle,
    /** Keypad digits. */
    val keypad: TextStyle,
    /** Player score, list layout — display font 56sp. */
    val scoreLarge: TextStyle,
    /** Player score, grid layout — display font 40sp. */
    val score: TextStyle,
    /** Score input display — display font 30sp. */
    val scoreSmall: TextStyle,
    /** Handwritten accent text, used sparingly (empty states). */
    val handwriting: TextStyle,
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
    title = TextStyle(fontFamily = DisplayFont, fontSize = 28.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
    heading = TextStyle(fontFamily = BodyFont, fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.3).sp),
    body = TextStyle(fontFamily = BodyFont, fontSize = 16.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontFamily = BodyFont, fontSize = 14.sp, fontWeight = FontWeight.Normal),
    label = TextStyle(fontFamily = BodyFont, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp),
    caption = TextStyle(fontFamily = BodyFont, fontSize = 13.sp, fontWeight = FontWeight.Bold),
    button = TextStyle(fontFamily = BodyFont, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
    keypad = TextStyle(fontFamily = BodyFont, fontSize = 22.sp, fontWeight = FontWeight.Bold),
    scoreLarge = TextStyle(
        fontFamily = DisplayFont,
        fontSize = 56.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = (-1).sp,
        fontFeatureSettings = "tnum",
    ),
    score = TextStyle(
        fontFamily = DisplayFont,
        fontSize = 40.sp,
        lineHeight = 46.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = (-1).sp,
        fontFeatureSettings = "tnum",
    ),
    scoreSmall = TextStyle(
        fontFamily = DisplayFont,
        fontSize = 30.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.Black,
        fontFeatureSettings = "tnum",
    ),
    handwriting = TextStyle(fontFamily = AccentFont, fontSize = 22.sp, fontWeight = FontWeight.Bold),
)
