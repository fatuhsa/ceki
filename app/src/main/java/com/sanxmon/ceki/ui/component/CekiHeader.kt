package com.sanxmon.ceki.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanxmon.ceki.domain.model.ViewMode
import com.sanxmon.ceki.ui.theme.CekiColors
import kotlinx.coroutines.delay

/**
 * Header mirroring `ceki-header.tsx`: title (double-tap arms "GAME BARU?" for 3s),
 * view-mode toggle and history button.
 */
@Composable
fun CekiHeader(
    viewMode: ViewMode,
    onToggleView: () -> Unit,
    onToggleHistory: () -> Unit,
    onNewGame: () -> Unit,
) {
    var armed by remember { mutableStateOf(false) }

    LaunchedEffect(armed) {
        if (armed) {
            delay(3000)
            armed = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CekiColors.Mantle)
            .drawBehind {
                drawLine(
                    color = CekiColors.Surface0,
                    start = Offset(0f, size.height - 1.dp.toPx()),
                    end = Offset(size.width, size.height - 1.dp.toPx()),
                    strokeWidth = 1.dp.toPx(),
                )
            }
            .padding(horizontal = 24.dp, vertical = 16.dp),
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
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                ),
                color = if (armed) CekiColors.Red else CekiColors.Primary,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CekiPressable(
                onClick = onToggleView,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
            ) {
                Icon(
                    imageVector = if (viewMode == ViewMode.GRID) Icons.Filled.ViewList else Icons.Filled.ViewModule,
                    contentDescription = "Ubah tampilan",
                    tint = CekiColors.Subtext0,
                    modifier = Modifier.size(24.dp),
                )
            }
            CekiPressable(
                onClick = onToggleHistory,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = "Riwayat",
                    tint = CekiColors.Subtext0,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}
