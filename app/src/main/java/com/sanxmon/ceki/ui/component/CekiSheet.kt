package com.sanxmon.ceki.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.sanxmon.ceki.ui.theme.appShapes

/**
 * Shared bottom-sheet chrome: flat top (0dp radius), accent drag handle line and
 * a 3dp accent border across the top edge — the "this is the selected context"
 * accent of the editorial design system.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CekiSheet(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val accent = MaterialTheme.colorScheme.primary

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surface,
        scrimColor = MaterialTheme.colorScheme.scrim,
        shape = MaterialTheme.appShapes.sheetTop,
        dragHandle = {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .background(accent),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRect(
                        color = accent,
                        topLeft = Offset(0f, 0f),
                        size = Size(size.width, 3.dp.toPx()),
                    )
                }
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            content = content,
        )
    }
}
