package com.sanxmon.ceki.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sanxmon.ceki.ui.theme.appShapes
import com.sanxmon.ceki.ui.theme.appTypography

/**
 * Player scorecard mirroring `player-card.tsx`: tap to select, long-press to open
 * the actions sheet. Selected cards invert to the primary color.
 */
@Composable
fun PlayerCard(
    nama: String,
    skor: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
    onLongPress: () -> Unit,
) {
    val shape = MaterialTheme.appShapes.card

    CekiPressable(
        onClick = onSelect,
        onLongClick = onLongPress,
        modifier = modifier
            .border(
                width = 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                shape = shape,
            )
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                shape = shape,
            )
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            ) {
                Text(
                    text = "Pemain",
                    style = MaterialTheme.appTypography.label,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
                Text(
                    text = nama,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.appTypography.heading,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                )
            }
            Text(
                text = "$skor",
                style = MaterialTheme.appTypography.score,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
