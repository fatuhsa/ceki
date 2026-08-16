package com.sanxmon.ceki.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sanxmon.ceki.ui.theme.appColors
import com.sanxmon.ceki.ui.theme.appShapes
import com.sanxmon.ceki.ui.theme.appTypography
import com.sanxmon.ceki.ui.theme.blockShadow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.floorMod

/** Subtle deterministic tilts for player cards (1-2°, never more). */
private val CARD_TILTS = listOf(-2f, -1f, 1f, 2f)

/**
 * Player scorecard mirroring `player-card.tsx`: tap to select, long-press opens
 * the actions sheet (with a 150ms accent border flash). Sharp corners, thick
 * border; selected cards get a 3dp accent border and tinted background.
 */
@Composable
fun PlayerCard(
    nama: String,
    skor: Int,
    isSelected: Boolean,
    largeScore: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
    onLongPress: () -> Unit,
) {
    val shape = MaterialTheme.appShapes.card
    val scope = rememberCoroutineScope()
    var flash by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            flash || isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.outlineVariant
        },
        animationSpec = tween(120),
        label = "playerCardBorder",
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
        } else {
            MaterialTheme.colorScheme.surfaceElevated
        },
        animationSpec = tween(120),
        label = "playerCardBg",
    )
    val borderWidth = if (isSelected || flash) 3.dp else 2.dp
    val rotation = remember(nama) { CARD_TILTS[floorMod(nama.hashCode(), CARD_TILTS.size)] }

    CekiPressable(
        onClick = onSelect,
        onLongClick = {
            flash = true
            scope.launch {
                delay(150)
                flash = false
                onLongPress()
            }
        },
        modifier = modifier
            .graphicsLayer { rotationZ = rotation }
            .blockShadow()
            .border(borderWidth, borderColor, shape)
            .background(bgColor, shape)
            .padding(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = nama.uppercase(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.appTypography.label,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.appColors.textMuted
                },
            )
            Text(
                text = "$skor",
                style = if (largeScore) {
                    MaterialTheme.appTypography.scoreLarge
                } else {
                    MaterialTheme.appTypography.score
                },
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            )
        }
    }
}
