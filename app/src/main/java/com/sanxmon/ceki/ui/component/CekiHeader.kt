package com.sanxmon.ceki.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sanxmon.ceki.domain.model.ViewMode
import com.sanxmon.ceki.ui.theme.appTypography
import kotlinx.coroutines.delay

/**
 * Header mirroring `ceki-header.tsx`: title (double-tap arms "GAME BARU?" for 3s),
 * view-mode toggle, history button and theme/appearance button. Solid background,
 * 2dp bottom border, slightly skewed title, square bordered icon buttons.
 */
@Composable
fun CekiHeader(
    viewMode: ViewMode,
    onToggleView: () -> Unit,
    onToggleHistory: () -> Unit,
    onOpenAppearance: () -> Unit,
    onNewGame: () -> Unit,
) {
    var armed by remember { mutableStateOf(false) }
    val dividerColor = MaterialTheme.colorScheme.outline

    LaunchedEffect(armed) {
        if (armed) {
            delay(3000)
            armed = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .drawBehind {
                drawLine(
                    color = dividerColor,
                    start = Offset(0f, size.height - 2.dp.toPx()),
                    end = Offset(size.width, size.height - 2.dp.toPx()),
                    strokeWidth = 2.dp.toPx(),
                )
            }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        CekiPressable(
            onClick = {
                if (armed) {
                    armed = false
                    onNewGame()
                } else {
                    armed = true
                }
            },
        ) {
            Text(
                text = if (armed) "KLIK: GAME BARU?" else "Ceki",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.appTypography.title,
                color = if (armed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                modifier = Modifier.graphicsLayer { rotationZ = -3f },
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HeaderIconButton(
                icon = if (viewMode == ViewMode.GRID) Icons.Filled.ViewList else Icons.Filled.ViewModule,
                contentDescription = "Ubah tampilan",
                onClick = onToggleView,
            )
            HeaderIconButton(
                icon = Icons.Filled.History,
                contentDescription = "Riwayat",
                onClick = onToggleHistory,
            )
            HeaderIconButton(
                icon = Icons.Filled.Palette,
                contentDescription = "Tema",
                onClick = onOpenAppearance,
            )
        }
    }
}

/** Square icon button: 2dp border, no rounding — not a Material IconButton. */
@Composable
private fun HeaderIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    CekiPressable(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .border(2.dp, MaterialTheme.colorScheme.outlineVariant)
            .background(MaterialTheme.colorScheme.surfaceElevated),
    ) { pressed ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (pressed) MaterialTheme.colorScheme.surfacePressed else Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
