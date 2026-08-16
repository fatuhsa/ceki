package com.sanxmon.ceki.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Central color tokens for one theme. Every screen and component derives its
 * colors from these tokens — no color is hardcoded outside this package.
 */
data class ThemeColors(
    val isDark: Boolean,
    /** App background behind all content. */
    val background: Color,
    /** Panels: header, bottom bar, dialogs, drawers, sheets. */
    val surface: Color,
    /** Elevated surfaces: cards, keys, buttons, inputs. */
    val surfaceElevated: Color,
    /** Pressed/highlighted surface state. */
    val surfacePressed: Color,
    /** Full-screen backdrop for modals, drawers and scrims. */
    val overlay: Color,
    /** Main brand/action color. */
    val primary: Color,
    val onPrimary: Color,
    /** Secondary/neutral brand color. */
    val secondary: Color,
    /** Accent color for highlights and emphasis. */
    val accent: Color,
    val onAccent: Color,
    /** Primary text. */
    val text: Color,
    /** Secondary/muted text (labels, icons, hints). */
    val textMuted: Color,
    /** Faint text (timestamps, placeholders). */
    val textFaint: Color,
    /** Dividers and hairlines. */
    val divider: Color,
    /** Input field background. */
    val inputField: Color,
    /** Input field border. */
    val inputBorder: Color,
    /** Positive status (plus, success). */
    val success: Color,
    /** Warning status. */
    val warning: Color,
    /** Error/danger status. */
    val error: Color,
    val onError: Color,
)
