package com.sanxmon.ceki.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanxmon.ceki.ui.ConfirmState
import com.sanxmon.ceki.ui.theme.DiagonalCut
import com.sanxmon.ceki.ui.theme.appColors
import com.sanxmon.ceki.ui.theme.appShapes
import com.sanxmon.ceki.ui.theme.appTypography
import com.sanxmon.ceki.ui.theme.blockShadow

/**
 * Centered confirm dialog mirroring `confirm-modal.tsx`. The most critical state
 * in the app (destructive action): full 3dp danger border, square warning badge,
 * solid danger "YA" and a larger, brighter outline "BATAL" placed on the right
 * (thumb-reachable) so the safe default is the easy one.
 */
@Composable
fun ConfirmDialog(
    confirm: ConfirmState,
    onClose: () -> Unit,
) {
    BackHandler(onBack = onClose)
    val dialogShape = MaterialTheme.appShapes.dialog

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .blockShadow()
                .border(3.dp, MaterialTheme.colorScheme.error, dialogShape)
                .background(MaterialTheme.colorScheme.surface, dialogShape)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.error),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.size(30.dp),
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = confirm.title,
                style = MaterialTheme.appTypography.title,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = confirm.message,
                style = MaterialTheme.appTypography.body,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DialogButton(
                    text = "YA, LANJUT",
                    textColor = MaterialTheme.colorScheme.onError,
                    background = MaterialTheme.colorScheme.error,
                    pressedBackground = MaterialTheme.colorScheme.error.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Black,
                    weight = Modifier.weight(1f),
                    onClick = {
                        confirm.onConfirm()
                        onClose()
                    },
                )
                DialogButton(
                    text = "BATAL",
                    textColor = MaterialTheme.colorScheme.onSurface,
                    background = Color.Transparent,
                    pressedBackground = MaterialTheme.appColors.surfacePressed,
                    borderColor = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    weight = Modifier.weight(1.25f),
                    onClick = onClose,
                )
            }
        }
    }
}

@Composable
internal fun DialogButton(
    text: String,
    textColor: Color,
    background: Color,
    weight: Modifier,
    onClick: () -> Unit,
    fontWeight: FontWeight = FontWeight.Bold,
    borderColor: Color? = null,
    diagonal: Boolean = false,
    pressedBackground: Color? = null,
) {
    val shape = if (diagonal) DiagonalCut else MaterialTheme.appShapes.button

    CekiPressable(
        onClick = onClick,
        modifier = weight
            .height(48.dp)
            .then(if (borderColor == null) Modifier.blockShadow() else Modifier),
    ) { pressed ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (pressed) pressedBackground ?: background else background,
                    shape = shape,
                )
                .then(
                    borderColor?.let { Modifier.border(2.dp, it, shape) } ?: Modifier,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = MaterialTheme.appTypography.button,
                fontWeight = fontWeight,
                color = textColor,
            )
        }
    }
}
