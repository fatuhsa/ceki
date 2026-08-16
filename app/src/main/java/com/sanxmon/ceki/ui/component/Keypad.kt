package com.sanxmon.ceki.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sanxmon.ceki.ui.theme.appShapes
import com.sanxmon.ceki.ui.theme.appTypography

private val ROWS = listOf(
    listOf("1", "2", "3"),
    listOf("4", "5", "6"),
    listOf("7", "8", "9"),
)

/**
 * In-app numeric keypad mirroring `keypad.tsx`: rows 1-9 plus a 0/backspace row.
 * The OS keyboard never appears.
 */
@Composable
fun Keypad(
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ROWS.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { digit ->
                    KeypadKey(text = digit, modifier = Modifier.weight(1f)) { onDigit(digit) }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KeypadKey(text = "0", modifier = Modifier.weight(1f)) { onDigit("0") }
            CekiPressable(
                onClick = onBackspace,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(MaterialTheme.appShapes.key)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = "Hapus",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun KeypadKey(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    CekiPressable(
        onClick = onClick,
        modifier = modifier
            .height(44.dp)
            .clip(MaterialTheme.appShapes.key)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.appTypography.keypad,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
