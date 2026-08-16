package com.sanxmon.ceki.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sanxmon.ceki.domain.model.ViewMode
import com.sanxmon.ceki.ui.CekiUiState
import com.sanxmon.ceki.ui.CekiViewModel
import com.sanxmon.ceki.ui.component.CekiHeader
import com.sanxmon.ceki.ui.component.CekiPressable
import com.sanxmon.ceki.ui.component.ConfirmDialog
import com.sanxmon.ceki.ui.component.EditNameDialog
import com.sanxmon.ceki.ui.component.HistoryDrawer
import com.sanxmon.ceki.ui.component.Keypad
import com.sanxmon.ceki.ui.component.PlayerActionsSheet
import com.sanxmon.ceki.ui.component.PlayerCard
import com.sanxmon.ceki.ui.component.ThemeSelectorSheet
import com.sanxmon.ceki.ui.theme.appColors
import com.sanxmon.ceki.ui.theme.appShapes
import com.sanxmon.ceki.ui.theme.appTypography

private const val CONTENT_BOTTOM_PADDING = 300

/** Root composable: wires the ViewModel to the screen. */
@Composable
fun CekiApp() {
    val viewModel: CekiViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()
    CekiScreen(state = state, viewModel = viewModel)
}

/**
 * Main game screen mirroring `app/index.tsx`: header, player cards, floating
 * bottom bar with score controls and keypad, plus modal overlays. All colors
 * come from the active theme.
 */
@Composable
fun CekiScreen(
    state: CekiUiState,
    viewModel: CekiViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            CekiHeader(
                viewMode = state.viewMode,
                onToggleView = viewModel::toggleViewMode,
                onToggleHistory = viewModel::openHistory,
                onOpenAppearance = viewModel::openAppearance,
                onNewGame = {
                    viewModel.showConfirm(
                        title = "MULAI GAME BARU?",
                        message = "Seluruh skor dan history akan dihapus selamanya. Pastikan permainan sudah benar-benar selesai.",
                        onConfirm = viewModel::newGame,
                    )
                },
            )

            if (state.viewMode == ViewMode.GRID) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .padding(bottom = CONTENT_BOTTOM_PADDING.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    state.players.chunked(2).forEach { rowPlayers ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            rowPlayers.forEach { player ->
                                PlayerCardRowItem(
                                    playerName = player.name,
                                    playerScore = player.score,
                                    isSelected = state.selectedPlayerIndex == state.players.indexOf(player),
                                    modifier = Modifier.weight(1f),
                                    onSelect = { viewModel.selectPlayer(state.players.indexOf(player)) },
                                    onLongPress = { viewModel.openActions(state.players.indexOf(player)) },
                                )
                            }
                            if (rowPlayers.size == 1) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .padding(bottom = CONTENT_BOTTOM_PADDING.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    state.players.forEachIndexed { index, player ->
                        PlayerCardRowItem(
                            playerName = player.name,
                            playerScore = player.score,
                            isSelected = state.selectedPlayerIndex == index,
                            modifier = Modifier.fillMaxWidth(),
                            onSelect = { viewModel.selectPlayer(index) },
                            onLongPress = { viewModel.openActions(index) },
                        )
                    }
                }
            }
        }

        // Floating bottom bar: error line, +/- controls, score display, keypad.
        val bottomBarDividerColor = MaterialTheme.colorScheme.outline
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(MaterialTheme.appShapes.sheetTop)
                .background(MaterialTheme.colorScheme.surface)
                .drawBehind {
                    drawLine(
                        color = bottomBarDividerColor,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx(),
                    )
                }
                .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets(bottom = 12.dp)))
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            if (state.error != null) {
                Text(
                    text = state.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    style = MaterialTheme.appTypography.label,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ControlButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Kurangi",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(30.dp),
                        )
                    },
                    enabled = state.hasSelection,
                    onClick = { viewModel.handleScoreUpdate(isAddition = false) },
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-14).dp)
                            .fillMaxWidth(0.8f),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (state.hasSelection) {
                            Text(
                                text = state.selectedPlayer?.name.orEmpty(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(horizontal = 12.dp, vertical = 3.dp),
                                style = MaterialTheme.appTypography.label,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.appShapes.card)
                            .background(MaterialTheme.colorScheme.background)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.appShapes.card),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = if (state.scoreInput.isEmpty()) "0" else state.scoreInput,
                            style = MaterialTheme.appTypography.scoreSmall,
                            color = if (state.scoreInput.isEmpty()) {
                                MaterialTheme.appColors.textFaint
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )
                    }
                }

                ControlButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Tambah",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(30.dp),
                        )
                    },
                    enabled = state.hasSelection,
                    onClick = { viewModel.handleScoreUpdate(isAddition = true) },
                )
            }

            Spacer(Modifier.height(14.dp))

            Keypad(
                onDigit = viewModel::appendDigit,
                onBackspace = viewModel::backspace,
            )
        }

        // Modal overlays (rendered last so they sit on top).
        state.confirm?.let { confirm ->
            ConfirmDialog(
                confirm = confirm,
                onClose = viewModel::closeConfirm,
            )
        }

        if (state.isEditing) {
            EditNameDialog(
                nama = state.newNama,
                error = state.error,
                onNamaChange = viewModel::updateNewNama,
                onClose = viewModel::closeEdit,
                onEdit = { viewModel.submitEdit() },
            )
        }

        HistoryDrawer(
            isOpen = state.isHistoryOpen,
            history = state.history,
            onClose = viewModel::closeHistory,
        )
    }

    val actionPlayer = state.actionPlayerIndex?.let { state.players.getOrNull(it) }
    if (actionPlayer != null) {
        PlayerActionsSheet(
            playerName = actionPlayer.name,
            playerScore = actionPlayer.score,
            onClose = viewModel::closeActions,
            onEdit = {
                state.actionPlayerIndex?.let { viewModel.openEdit(it, actionPlayer.name) }
                viewModel.closeActions()
            },
            onReset = {
                val index = state.actionPlayerIndex
                viewModel.closeActions()
                viewModel.showConfirm(
                    title = "RESET SKOR?",
                    message = "Ingin meriset skor ${actionPlayer.name} menjadi 0? Aktivitas ini akan dicatat di log history.",
                    onConfirm = { index?.let(viewModel::resetPlayerScore) },
                )
            },
        )
    }

    if (state.isAppearanceOpen) {
        ThemeSelectorSheet(
            onClose = viewModel::closeAppearance,
        )
    }
}

@Composable
private fun PlayerCardRowItem(
    playerName: String,
    playerScore: Int,
    isSelected: Boolean,
    modifier: Modifier,
    onSelect: () -> Unit,
    onLongPress: () -> Unit,
) {
    PlayerCard(
        nama = playerName,
        skor = playerScore,
        isSelected = isSelected,
        modifier = modifier,
        onSelect = onSelect,
        onLongPress = onLongPress,
    )
}

@Composable
private fun ControlButton(
    icon: @Composable () -> Unit,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    CekiPressable(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            icon()
        }
    }
}
