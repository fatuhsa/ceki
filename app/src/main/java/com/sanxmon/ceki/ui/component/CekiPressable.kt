package com.sanxmon.ceki.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

/**
 * Pressable wrapper mirroring the original `CekiPressable.tsx`: light haptic on
 * press, long-press haptic, subtle scale/alpha feedback and a [pressed] flag
 * passed to content so components can swap colors instantly (e.g. keypad keys).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CekiPressable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onLongClick: (() -> Unit)? = null,
    scaleOnPress: Boolean = true,
    dimOnPress: Boolean = true,
    content: @Composable (pressed: Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .graphicsLayer {
                val pressedActive = pressed && enabled
                scaleX = if (pressedActive && scaleOnPress) 0.97f else 1f
                scaleY = if (pressedActive && scaleOnPress) 0.97f else 1f
                alpha = when {
                    !enabled -> 0.3f
                    pressedActive && dimOnPress -> 0.8f
                    else -> 1f
                }
            }
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                },
                onLongClick = onLongClick?.let { longClick ->
                    {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        longClick()
                    }
                },
            ),
    ) {
        content(pressed && enabled)
    }
}
