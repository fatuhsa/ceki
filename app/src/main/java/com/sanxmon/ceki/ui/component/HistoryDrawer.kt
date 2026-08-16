package com.sanxmon.ceki.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sanxmon.ceki.domain.model.HistoryLog
import com.sanxmon.ceki.domain.model.HistoryType
import com.sanxmon.ceki.ui.theme.appColors
import com.sanxmon.ceki.ui.theme.appShapes
import com.sanxmon.ceki.ui.theme.appTypography

private data class BadgeStyle(val label: String, val color: Color)

@Composable
private fun badgeFor(type: HistoryType): BadgeStyle = when (type) {
    HistoryType.PLUS -> BadgeStyle("Plus", MaterialTheme.appColors.success)
    HistoryType.MINUS -> BadgeStyle("Minus", MaterialTheme.colorScheme.error)
    HistoryType.RESET -> BadgeStyle("Reset", MaterialTheme.appColors.warning)
    HistoryType.NAME_CHANGE -> BadgeStyle("Nama", MaterialTheme.colorScheme.secondary)
}

/**
 * Right-side history drawer mirroring `history-drawer.tsx`: overlay tap to close,
 * badge per entry type, name changes rendered old → new.
 */
@Composable
fun HistoryDrawer(
    isOpen: Boolean,
    history: List<HistoryLog>,
    onClose: () -> Unit,
) {
    BackHandler(enabled = isOpen, onBack = onClose)

    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isOpen,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200)),
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClose,
                    ),
            )
        }

        AnimatedVisibility(
            visible = isOpen,
            enter = slideInHorizontally(tween(250)) { it } + fadeIn(tween(250)),
            exit = slideOutHorizontally(tween(250)) { it } + fadeOut(tween(250)),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .widthIn(max = 340.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
                    .drawBehind {
                        drawLine(
                            color = MaterialTheme.colorScheme.outline,
                            start = Offset(1.dp.toPx(), 0f),
                            end = Offset(1.dp.toPx(), size.height),
                            strokeWidth = 1.dp.toPx(),
                        )
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Log History",
                        style = MaterialTheme.appTypography.title.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    CekiPressable(
                        onClick = onClose,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Tutup",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                }

                if (history.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(40.dp),
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Belum ada aktivitas",
                                style = MaterialTheme.appTypography.body,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.appColors.textFaint,
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        history.forEach { log ->
                            HistoryLogItem(log)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryLogItem(log: HistoryLog) {
    val badge = badgeFor(log.type)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.appShapes.card)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .drawBehind {
                drawLine(
                    color = MaterialTheme.colorScheme.primary,
                    start = Offset(2.dp.toPx(), 0f),
                    end = Offset(2.dp.toPx(), size.height),
                    strokeWidth = 4.dp.toPx(),
                )
            }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = log.timestamp,
                style = MaterialTheme.appTypography.label,
                color = MaterialTheme.appColors.textFaint,
            )
            Row(
                modifier = Modifier
                    .border(1.dp, badge.color, MaterialTheme.appShapes.badge)
                    .clip(MaterialTheme.appShapes.badge)
                    .background(badge.color.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
            ) {
                Text(
                    text = badge.label,
                    style = MaterialTheme.appTypography.label,
                    color = badge.color,
                )
            }
        }

        if (log.type == HistoryType.NAME_CHANGE) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = log.extra?.oldName.orEmpty(),
                    style = MaterialTheme.appTypography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.LineThrough,
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = log.extra?.newName.orEmpty(),
                    style = MaterialTheme.appTypography.bodySmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.appColors.success,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = log.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    style = MaterialTheme.appTypography.body,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = when (log.type) {
                        HistoryType.PLUS -> "+${log.amount}"
                        HistoryType.MINUS -> "-${log.amount}"
                        else -> "${log.amount}"
                    },
                    style = MaterialTheme.appTypography.score.copy(fontSize = MaterialTheme.appTypography.score.fontSize * 0.6f),
                    color = when (log.type) {
                        HistoryType.PLUS -> MaterialTheme.appColors.success
                        HistoryType.MINUS -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.appColors.warning
                    },
                )
            }
        }
    }
}
