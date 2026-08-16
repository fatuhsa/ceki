package com.sanxmon.ceki.ui.component

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView

/**
 * Pressable wrapper mirroring the original `CekiPressable.tsx`: haptic tick on
 * press, subtle scale/alpha feedback, disabled state dimming.
 */
@Composable
fun CekiPressable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onLongClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val view = LocalView.current

    Box(
        modifier = modifier
            .graphicsLayer {
                val pressedActive = pressed && enabled
                scaleX = if (pressedActive) 0.97f else 1f
                scaleY = if (pressedActive) 0.97f else 1f
                alpha = when {
                    !enabled -> 0.3f
                    pressedActive -> 0.8f
                    else -> 1f
                }
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = {
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    onClick()
                },
                onLongClick = onLongClick?.let { longClick ->
                    {
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        longClick()
                    }
                },
            ),
    ) {
        content()
    }
}
