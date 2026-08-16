package com.sanxmon.ceki.ui.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sanxmon.ceki.ui.theme.appShapes
import com.sanxmon.ceki.ui.theme.appTypography

/**
 * Bottom sheet mirroring `player-actions-modal.tsx`: shows the tapped player and
 * offers GANTI NAMA / RESET SKOR.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerActionsSheet(
    playerName: String,
    playerScore: Int,
    onClose: () -> Unit,
    onEdit: () -> Unit,
    onReset: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onClose,
        containerColor = MaterialTheme.colorScheme.surface,
        scrimColor = MaterialTheme.colorScheme.scrim,
        dragHandle = {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
        ) {
            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
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

            CekiPressable(
                onClick = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(MaterialTheme.appShapes.button)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                SheetActionRow(icon = {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                }) {
                    Text(
                        text = "GANTI NAMA",
                        style = MaterialTheme.appTypography.button,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            CekiPressable(
                onClick = onReset,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(MaterialTheme.appShapes.button)
                    .background(MaterialTheme.colorScheme.error),
            ) {
                SheetActionRow(icon = {
                    Icon(
                        Icons.Filled.RestartAlt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(20.dp),
                    )
                }) {
                    Text(
                        text = "RESET SKOR",
                        style = MaterialTheme.appTypography.button,
                        color = MaterialTheme.colorScheme.onError,
                    )
                }
            }
        }
    }
}

@Composable
private fun SheetActionRow(
    icon: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Spacer(Modifier.width(10.dp))
        content()
    }
}
