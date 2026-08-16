package com.sanxmon.ceki.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanxmon.ceki.ui.theme.appTypography
import com.sanxmon.ceki.ui.theme.blockShadow

private val ROWS = listOf(
    listOf("1", "2", "3"),
    listOf("4", "5", "6"),
    listOf("7", "8", "9"),
)

/**
 * In-app numeric keypad mirroring `keypad.tsx`: rows 1-9 plus a 0/backspace row.
 * One tight unit with hard grid lines between keys; pressed keys invert to the
 * accent instantly and fade back in 100ms. The OS keyboard never appears.
 */
@Composable
fun Keypad(
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .blockShadow()
            .border(2.dp, MaterialTheme.colorScheme.outline)
            .background(MaterialTheme.colorScheme.outline)
            .padding(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        ROWS.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                row.forEach { digit ->
                    KeypadKey(text = digit, modifier = Modifier.weight(1f)) { onDigit(digit) }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
            KeypadKey(text = "0", modifier = Modifier.weight(1f)) { onDigit("0") }
            BackspaceKey(modifier = Modifier.weight(1f), onBackspace = onBackspace)
        }
    }
}

@Composable
private fun KeypadKey(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val accent = MaterialTheme.colorScheme.accent
    val onAccent = MaterialTheme.colorScheme.onAccent
    val onSurface = MaterialTheme.colorScheme.onSurface
    val normalBg = MaterialTheme.colorScheme.surfaceElevated

    val bg = remember { Animatable(normalBg) }

    CekiPressable(
        onClick = onClick,
        scaleOnPress = false,
        dimOnPress = false,
        modifier = modifier.height(48.dp),
    ) { pressed ->
        LaunchedEffect(pressed, accent, normalBg) {
            if (pressed) {
                bg.snapTo(accent)
            } else {
                bg.animateTo(normalBg, animationSpec = tween(100))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg.value),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = MaterialTheme.appTypography.keypad,
                color = if (pressed) onAccent else onSurface,
            )
        }
    }
}

@Composable
private fun BackspaceKey(
    modifier: Modifier,
    onBackspace: () -> Unit,
) {
    val error = MaterialTheme.colorScheme.error
    val surfacePressed = MaterialTheme.colorScheme.surfacePressed
    val surfaceElevated = MaterialTheme.colorScheme.surfaceElevated

    CekiPressable(
        onClick = onBackspace,
        scaleOnPress = false,
        dimOnPress = false,
        modifier = modifier.height(48.dp),
    ) { pressed ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (pressed) surfacePressed else surfaceElevated),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "Hapus",
                tint = error.copy(alpha = 0.75f),
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
