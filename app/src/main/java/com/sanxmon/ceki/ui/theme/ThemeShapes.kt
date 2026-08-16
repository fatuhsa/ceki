package com.sanxmon.ceki.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * The single diagonal cut used across the design system: a straight edge that
 * trims 12% off the bottom-right corner. Reused on primary buttons and header
 * accents; player cards keep sharp straight corners for fast re-reading.
 */
val DiagonalCut: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height * 0.88f)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}

/**
 * Shape tokens for one theme. The editorial style uses 0dp radius everywhere —
 * sharp corners, 2dp borders, thick selected borders.
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
    card = RoundedCornerShape(0.dp),
    dialog = RoundedCornerShape(0.dp),
    button = RoundedCornerShape(0.dp),
    key = RoundedCornerShape(0.dp),
    field = RoundedCornerShape(0.dp),
    badge = RoundedCornerShape(0.dp),
    sheetTop = RoundedCornerShape(0.dp),
)
