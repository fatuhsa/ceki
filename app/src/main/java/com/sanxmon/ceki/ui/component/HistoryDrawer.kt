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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanxmon.ceki.domain.model.HistoryLog
import com.sanxmon.ceki.domain.model.HistoryType
import com.sanxmon.ceki.ui.theme.appColors
import com.sanxmon.ceki.ui.theme.appShapes
import com.sanxmon.ceki.ui.theme.appTypography

/**
 * Right-side history drawer mirroring `history-drawer.tsx`: overlay tap to close,
 * square badge per entry type, name changes rendered old → new. Entries read as
 * a log: thin dividers between rows, no per-entry cards.
 */
@Composable
fun HistoryDrawer(
    isOpen: Boolean,
    history: List<HistoryLog>,
    onClose: () -> Unit,
) {
    BackHandler(enabled = isOpen, onBack = onClose)
    val drawerDividerColor = MaterialTheme.colorScheme.outline

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
            enter = slideInHorizontally(tween(200)) { it },
            exit = slideOutHorizontally(tween(200)) { it },
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
                            color = drawerDividerColor,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 2.dp.toPx(),
                        )
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Log History",
                        style = MaterialTheme.appTypography.title,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    CekiPressable(
                        onClick = onClose,
                        modifier = Modifier
                            .size(32.dp)
                            .border(2.dp, MaterialTheme.colorScheme.outlineVariant)
                            .background(MaterialTheme.colorScheme.surfaceElevated),
                    ) { pressed ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    if (pressed) {
                                        MaterialTheme.colorScheme.surfacePressed
                                    } else {
                                        Color.Transparent
                                    },
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
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
                        Text(
                            text = "Belum ada aktivitas",
                            style = MaterialTheme.appTypography.handwriting,
                            color = MaterialTheme.appColors.textMuted,
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
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
    val dividerColor = MaterialTheme.colorScheme.outline
    val badge = badgeFor(log.type)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = dividerColor,
                    start = Offset(0f, size.height - 1.dp.toPx()),
                    end = Offset(size.width, size.height - 1.dp.toPx()),
                    strokeWidth = 1.dp.toPx(),
                )
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HistoryBadge(log.type)
            Text(
                text = log.timestamp,
                style = MaterialTheme.appTypography.label,
                color = MaterialTheme.appColors.textFaint,
            )
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

private data class BadgeStyle(
    val color: Color,
    val text: String? = null,
    val icon: ImageVector? = null,
)

@Composable
private fun badgeFor(type: HistoryType): BadgeStyle = when (type) {
    HistoryType.PLUS -> BadgeStyle(color = MaterialTheme.appColors.success, text = "+")
    HistoryType.MINUS -> BadgeStyle(color = MaterialTheme.colorScheme.error, text = "−")
    HistoryType.RESET -> BadgeStyle(color = MaterialTheme.colorScheme.error, icon = Icons.Filled.RestartAlt)
    HistoryType.NAME_CHANGE -> BadgeStyle(color = MaterialTheme.appColors.textMuted, icon = Icons.Filled.Edit)
}

/** Square badge per entry type — sharp corners, dim fill, no rounded pills. */
@Composable
private fun HistoryBadge(type: HistoryType) {
    val badge = badgeFor(type)

    Box(
        modifier = Modifier
            .size(22.dp)
            .border(1.dp, badge.color, MaterialTheme.appShapes.badge)
            .background(badge.color.copy(alpha = 0.15f), MaterialTheme.appShapes.badge),
        contentAlignment = Alignment.Center,
    ) {
        if (badge.icon != null) {
            Icon(
                imageVector = badge.icon,
                contentDescription = null,
                tint = badge.color,
                modifier = Modifier.size(13.dp),
            )
        } else {
            Text(
                text = badge.text.orEmpty(),
                style = MaterialTheme.appTypography.label.copy(fontSize = 13.sp, letterSpacing = 0.sp),
                color = badge.color,
            )
        }
    }
}
