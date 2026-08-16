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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanxmon.ceki.ui.ConfirmState
import com.sanxmon.ceki.ui.theme.appShapes
import com.sanxmon.ceki.ui.theme.appTypography

/**
 * Centered confirm dialog mirroring `confirm-modal.tsx`: warning icon, error
 * border, BATAL / YA, LANJUT actions.
 */
@Composable
fun ConfirmDialog(
    confirm: ConfirmState,
    onClose: () -> Unit,
) {
    BackHandler(onBack = onClose)

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
                .border(2.dp, MaterialTheme.colorScheme.error, MaterialTheme.appShapes.dialog)
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.appShapes.dialog)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.size(32.dp),
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
            ) {
                DialogButton(
                    text = "BATAL",
                    textColor = MaterialTheme.colorScheme.onSurface,
                    background = MaterialTheme.colorScheme.surfaceVariant,
                    fontWeight = FontWeight.Bold,
                    weight = Modifier.weight(1f),
                    onClick = onClose,
                )
                DialogButton(
                    text = "YA, LANJUT",
                    textColor = MaterialTheme.colorScheme.onError,
                    background = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Black,
                    weight = Modifier.weight(1f),
                    onClick = {
                        confirm.onConfirm()
                        onClose()
                    },
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
) {
    CekiPressable(
        onClick = onClick,
        modifier = weight
            .height(48.dp)
            .clip(MaterialTheme.appShapes.button)
            .background(background),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.appTypography.button,
                fontWeight = fontWeight,
                color = textColor,
            )
        }
    }
}
