package com.sanxmon.ceki.ui.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sanxmon.ceki.domain.model.ViewMode
import com.sanxmon.ceki.ui.AppliedFeedback
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
import com.sanxmon.ceki.ui.theme.blockShadow

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

            // Player area fills the space between header and bottom bar; the
            // fixed 4 cards stretch to fit, so there is no dead gap above the
            // keypad and no scrolling is needed.
            if (state.viewMode == ViewMode.GRID) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    state.players.chunked(2).forEach { rowPlayers ->
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            rowPlayers.forEach { player ->
                                PlayerCardRowItem(
                                    playerName = player.name,
                                    playerScore = player.score,
                                    isSelected = state.selectedPlayerIndex == state.players.indexOf(player),
                                    largeScore = false,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    onSelect = { viewModel.selectPlayer(state.players.indexOf(player)) },
                                    onLongPress = { viewModel.openActions(state.players.indexOf(player)) },
                                )
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    state.players.forEachIndexed { index, player ->
                        PlayerCardRowItem(
                            playerName = player.name,
                            playerScore = player.score,
                            isSelected = state.selectedPlayerIndex == index,
                            largeScore = true,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
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
                .background(MaterialTheme.colorScheme.surface)
                .drawBehind {
                    drawLine(
                        color = bottomBarDividerColor,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 2.dp.toPx(),
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

            val pendingLabel = state.scoreInput.takeIf { it.isNotEmpty() }?.let { "-$it" }
            val addLabel = state.scoreInput.takeIf { it.isNotEmpty() }?.let { "+$it" }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ControlButton(
                    icon = { pressed ->
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Kurangi",
                            tint = if (pressed) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            modifier = Modifier.size(30.dp),
                        )
                    },
                    label = pendingLabel,
                    enabled = state.hasSelection,
                    onClick = { viewModel.handleScoreUpdate(isAddition = false) },
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                ) {
                    ScoreDisplay(
                        input = state.scoreInput,
                        applied = state.applied,
                        hasSelection = state.hasSelection,
                    )

                    // Drawn after (on top of) the display so the opaque display
                    // background can never cut the chip in half again.
                    if (state.hasSelection) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = (-12).dp)
                                .fillMaxWidth(0.8f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = state.selectedPlayer?.name.orEmpty(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.appTypography.label,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }

                ControlButton(
                    icon = { pressed ->
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Tambah",
                            tint = if (pressed) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            modifier = Modifier.size(30.dp),
                        )
                    },
                    label = addLabel,
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

/**
 * Score input display: hint when empty, digits + blinking cursor while typing,
 * signed value briefly after apply (e.g. "+50"). Sharp 0dp border.
 */
@Composable
private fun ScoreDisplay(
    input: String,
    applied: AppliedFeedback?,
    hasSelection: Boolean,
) {
    val cursorAlpha by rememberInfiniteTransition().animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "scoreCursor",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background, MaterialTheme.appShapes.card)
            .border(2.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.appShapes.card),
        contentAlignment = Alignment.Center,
    ) {
        when {
            applied != null -> {
                // Solid primary chip keeps the signed value high-contrast in
                // every theme (dark gold-on-cream would fail in the light one).
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = applied.signedLabel,
                        style = MaterialTheme.appTypography.scoreSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }

            input.isNotEmpty() -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = input,
                        style = MaterialTheme.appTypography.scoreSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .width(2.dp)
                            .height(28.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = cursorAlpha)),
                    )
                }
            }

            else -> {
                // caption (13sp) keeps each hint line narrow enough to fit the
                // display on small screens; maxLines guards against wrapping.
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (hasSelection) "KETIK NILAI" else "PILIH PLAYER",
                        style = MaterialTheme.appTypography.caption,
                        color = MaterialTheme.appColors.textFaint,
                        maxLines = 1,
                    )
                    Text(
                        text = if (hasSelection) "LALU +/−" else "LALU KETIK NILAI",
                        style = MaterialTheme.appTypography.caption,
                        color = MaterialTheme.appColors.textFaint,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerCardRowItem(
    playerName: String,
    playerScore: Int,
    isSelected: Boolean,
    largeScore: Boolean,
    modifier: Modifier,
    onSelect: () -> Unit,
    onLongPress: () -> Unit,
) {
    PlayerCard(
        nama = playerName,
        skor = playerScore,
        isSelected = isSelected,
        largeScore = largeScore,
        modifier = modifier,
        onSelect = onSelect,
        onLongPress = onLongPress,
    )
}

/**
 * Square +/- control: hard border, inverts to the primary color when pressed.
 * While digits are being typed, [label] shows the pending signed value (e.g.
 * "+50") so the interaction is self-explanatory; otherwise the icon shows.
 */
@Composable
private fun ControlButton(
    icon: @Composable (pressed: Boolean) -> Unit,
    label: String? = null,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    CekiPressable(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .size(52.dp)
            .blockShadow()
            .border(2.dp, MaterialTheme.colorScheme.outlineVariant)
            .background(MaterialTheme.appColors.surfaceElevated),
    ) { pressed ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (pressed && enabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (label != null) {
                Text(
                    text = label,
                    style = MaterialTheme.appTypography.button,
                    color = if (pressed && enabled) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                )
            } else {
                icon(pressed && enabled)
            }
        }
    }
}
