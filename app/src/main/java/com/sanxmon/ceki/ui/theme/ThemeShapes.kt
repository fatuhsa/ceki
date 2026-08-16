package com.sanxmon.ceki.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Shape tokens for one theme. Components reference these instead of
 * hardcoding corner radii.
 */
data class ThemeShapes(
    val card: CornerBasedShape,
    val dialog: CornerBasedShape,
    val button: CornerBasedShape,
    val key: CornerBasedShape,
    val field: CornerBasedShape,
    val badge: CornerBasedShape,
    val sheetTop: CornerBasedShape,
) {
    fun toShapes(): Shapes = Shapes(
        small = badge,
        medium = key,
        large = field,
        extraLarge = dialog,
    )
}

/** Baseline shapes shared by all themes (themes may override). */
val DefaultCekiShapes = ThemeShapes(
    card = RoundedCornerShape(16.dp),
    dialog = RoundedCornerShape(24.dp),
    button = RoundedCornerShape(16.dp),
    key = RoundedCornerShape(12.dp),
    field = RoundedCornerShape(14.dp),
    badge = RoundedCornerShape(6.dp),
    sheetTop = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
)
