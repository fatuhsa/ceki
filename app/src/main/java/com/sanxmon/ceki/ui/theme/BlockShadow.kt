package com.sanxmon.ceki.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Hard offset shadow (no blur) that replaces Material elevation: a solid block
 * drawn offset down-right behind the component. Apply before the component's
 * background/border so only the offset edge peeks out.
 */
fun Modifier.blockShadow(
    offset: Dp = 6.dp,
    color: Color = Color.Black.copy(alpha = 0.5f),
): Modifier = drawBehind {
    val offsetPx = offset.toPx()
    drawRect(
        color = color,
        topLeft = Offset(offsetPx, offsetPx),
        size = size,
    )
}
