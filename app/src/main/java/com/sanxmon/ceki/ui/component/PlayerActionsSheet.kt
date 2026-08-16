package com.sanxmon.ceki.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sanxmon.ceki.ui.theme.appColors
import com.sanxmon.ceki.ui.theme.appTypography

/**
 * Bottom sheet mirroring `player-actions-modal.tsx`: shows the tapped player and
 * offers GANTI NAMA / RESET SKOR as list items separated by thin dividers.
 */
@Composable
fun PlayerActionsSheet(
    playerName: String,
    playerScore: Int,
    onClose: () -> Unit,
    onEdit: () -> Unit,
    onReset: () -> Unit,
) {
    CekiSheet(onDismissRequest = onClose) {
        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
            ) {
                Text(
                    text = "Pemain",
                    style = MaterialTheme.appTypography.label,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = playerName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.appTypography.heading,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Text(
                text = "$playerScore",
                style = MaterialTheme.appTypography.scoreLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        SheetActionItem(
            icon = Icons.Filled.Edit,
            text = "GANTI NAMA",
            tint = MaterialTheme.colorScheme.primary,
            onClick = onEdit,
        )
        SheetActionItem(
            icon = Icons.Filled.RestartAlt,
            text = "RESET SKOR",
            tint = MaterialTheme.colorScheme.error,
            onClick = onReset,
        )
    }
}

/** List item with a thin bottom divider — not a Material ListItem. */
@Composable
private fun SheetActionItem(
    icon: ImageVector,
    text: String,
    tint: Color,
    onClick: () -> Unit,
) {
    CekiPressable(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .drawBehind {
                drawLine(
                    color = tint.copy(alpha = 0.35f),
                    start = Offset(0f, size.height - 1.dp.toPx()),
                    end = Offset(size.width, size.height - 1.dp.toPx()),
                    strokeWidth = 1.dp.toPx(),
                )
            },
    ) { pressed ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(if (pressed) MaterialTheme.appColors.surfacePressed else Color.Transparent)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = text,
                style = MaterialTheme.appTypography.button,
                color = tint,
            )
        }
    }
}
