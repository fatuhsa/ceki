package com.sanxmon.ceki.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sanxmon.ceki.data.local.GameRepositoryImpl
import com.sanxmon.ceki.data.local.SharedPrefsKeyValueStore
import com.sanxmon.ceki.domain.model.HistoryLog
import com.sanxmon.ceki.domain.model.HistoryType
import com.sanxmon.ceki.domain.model.NameChangeExtra
import com.sanxmon.ceki.domain.model.Player
import com.sanxmon.ceki.domain.model.ViewMode
import com.sanxmon.ceki.domain.model.defaultPlayers
import com.sanxmon.ceki.domain.repository.GameRepository
import com.sanxmon.ceki.domain.usecase.HistoryRules
import com.sanxmon.ceki.domain.usecase.NameRules
import com.sanxmon.ceki.domain.usecase.ScoreRules
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConfirmState(
    val title: String,
    val message: String,
    val onConfirm: () -> Unit,
)

/** Short-lived feedback shown after a score was applied (e.g. "+50" / "-50"). */
data class AppliedFeedback(
    val amount: Int,
    val playerName: String,
    val isAddition: Boolean,
) {
    /** Label like "+50" or "-50", ASCII signs for font safety. */
    val signedLabel: String get() = (if (isAddition) "+" else "-") + amount
}

data class CekiUiState(
    val players: List<Player> = defaultPlayers(),
    val history: List<HistoryLog> = emptyList(),
    val loaded: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false,
    val editIndex: Int? = null,
    val newNama: String = "",
    val selectedPlayerIndex: Int? = null,
    val scoreInput: String = "",
    val isHistoryOpen: Boolean = false,
    val viewMode: ViewMode = ViewMode.GRID,
    val confirm: ConfirmState? = null,
    val actionPlayerIndex: Int? = null,
    val isAppearanceOpen: Boolean = false,
    val applied: AppliedFeedback? = null,
) {
    val hasSelection: Boolean get() = selectedPlayerIndex != null
    val selectedPlayer: Player? get() = selectedPlayerIndex?.let { players.getOrNull(it) }
}

/** State holder and actions for the Ceki game, mirroring the original `useCeki()` hook. */
class CekiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GameRepository =
        GameRepositoryImpl(SharedPrefsKeyValueStore(application))

    private val _uiState = MutableStateFlow(CekiUiState())
    val uiState: StateFlow<CekiUiState> = _uiState.asStateFlow()

    /** Cancels the auto-hide timer for the applied-score feedback. */
    private var appliedClearJob: Job? = null

    init {
        viewModelScope.launch {
            val players = repository.loadPlayers()
            val history = repository.loadHistory()
            val viewMode = repository.loadViewMode()
            _uiState.update { state ->
                state.copy(
                    players = players ?: state.players,
                    history = history ?: state.history,
                    viewMode = viewMode ?: state.viewMode,
                    loaded = true,
                )
            }
        }
    }

    // --- Score input ---

    fun appendDigit(digit: String) {
        if (digit.isEmpty()) return
        _uiState.update {
            it.copy(scoreInput = ScoreRules.appendDigit(it.scoreInput, digit.first()), applied = null)
        }
    }

    fun backspace() {
        _uiState.update { it.copy(scoreInput = ScoreRules.backspace(it.scoreInput), applied = null) }
    }

    // --- Player selection ---

    fun selectPlayer(index: Int) {
        _uiState.update { state ->
            state.copy(
                selectedPlayerIndex = if (state.selectedPlayerIndex == index) null else index,
                error = null,
                applied = null,
            )
        }
    }

    // --- Score updates ---

    fun handleScoreUpdate(isAddition: Boolean) {
        val state = _uiState.value
        if (state.selectedPlayerIndex == null) {
            _uiState.update { it.copy(error = ScoreRules.ERROR_NO_SELECTION) }
            return
        }
        val value = state.scoreInput.toIntOrNull()
        if (value == null || value == 0) {
            _uiState.update { it.copy(error = ScoreRules.ERROR_INVALID_NUMBER) }
            return
        }
        val error = ScoreRules.errorFor(state.scoreInput)
        if (error != null) {
            _uiState.update { it.copy(error = error) }
            return
        }
        val amount = if (isAddition) value else -value
        val playerIndex = state.selectedPlayerIndex
        val player = state.players[playerIndex]
        val entry = HistoryRules.newEntry(
            name = player.name,
            amount = value,
            type = if (isAddition) HistoryType.PLUS else HistoryType.MINUS,
        )
        _uiState.update { it ->
            it.copy(
                players = it.players.mapIndexed { index, p ->
                    if (index == playerIndex) p.copy(score = p.score + amount) else p
                },
                history = HistoryRules.prepend(it.history, entry),
                scoreInput = "",
                applied = AppliedFeedback(value, player.name, isAddition),
                error = null,
            )
        }
        scheduleAppliedClear()
        persist()
    }

    /** Auto-hides the applied-score feedback after 1.4s. */
    private fun scheduleAppliedClear() {
        appliedClearJob?.cancel()
        appliedClearJob = viewModelScope.launch {
            delay(1400)
            _uiState.update { it.copy(applied = null) }
        }
    }

    fun resetPlayerScore(index: Int) {
        val state = _uiState.value
        val player = state.players.getOrNull(index) ?: return
        val entry = HistoryRules.newEntry(player.name, player.score, HistoryType.RESET)
        _uiState.update { it ->
            it.copy(
                players = it.players.mapIndexed { i, p -> if (i == index) p.copy(score = 0) else p },
                history = HistoryRules.prepend(it.history, entry),
            )
        }
        persist()
    }

    // --- Rename ---

    fun openEdit(index: Int, name: String) {
        _uiState.update { it.copy(isEditing = true, editIndex = index, newNama = name, error = null) }
    }

    fun closeEdit() {
        _uiState.update { it.copy(isEditing = false, editIndex = null, newNama = "", error = null) }
    }

    fun updateNewNama(name: String) {
        _uiState.update { it.copy(newNama = name) }
    }

    fun submitEdit() {
        val state = _uiState.value
        val index = state.editIndex ?: return
        val name = state.newNama

        val error = NameRules.errorFor(name)
        if (error != null) {
            _uiState.update { it.copy(error = error) }
            return
        }

        val oldName = state.players[index].name
        _uiState.update { it ->
            var history = it.history
            if (oldName != name) {
                history = HistoryRules.prepend(
                    history,
                    HistoryRules.newEntry(
                        name = oldName,
                        amount = 0,
                        type = HistoryType.NAME_CHANGE,
                        extra = NameChangeExtra(oldName = oldName, newName = name),
                    ),
                )
            }
            it.copy(
                players = it.players.mapIndexed { i, p -> if (i == index) p.copy(name = name) else p },
                history = history,
                isEditing = false,
                editIndex = null,
                newNama = "",
                error = null,
            )
        }
        persist()
    }

    // --- New game ---

    fun newGame() {
        _uiState.update {
            it.copy(
                players = defaultPlayers(),
                history = emptyList(),
                selectedPlayerIndex = null,
                scoreInput = "",
                applied = null,
                error = null,
            )
        }
        viewModelScope.launch { repository.clearGame() }
    }

    // --- Confirm dialog ---

    fun showConfirm(title: String, message: String, onConfirm: () -> Unit) {
        _uiState.update { it.copy(confirm = ConfirmState(title, message, onConfirm)) }
    }

    fun closeConfirm() {
        _uiState.update { it.copy(confirm = null) }
    }

    // --- Player actions sheet ---

    fun openActions(index: Int) {
        _uiState.update { it.copy(actionPlayerIndex = index) }
    }

    fun closeActions() {
        _uiState.update { it.copy(actionPlayerIndex = null) }
    }

    // --- Appearance (theme selector) ---

    fun openAppearance() {
        _uiState.update { it.copy(isAppearanceOpen = true) }
    }

    fun closeAppearance() {
        _uiState.update { it.copy(isAppearanceOpen = false) }
    }

    // --- View mode / history drawer ---

    fun toggleViewMode() {
        _uiState.update { it.copy(viewMode = if (it.viewMode == ViewMode.GRID) ViewMode.LIST else ViewMode.GRID) }
        persist()
    }

    fun openHistory() {
        _uiState.update { it.copy(isHistoryOpen = true) }
    }

    fun closeHistory() {
        _uiState.update { it.copy(isHistoryOpen = false) }
    }

    // --- Persistence ---

    private fun persist() {
        val state = _uiState.value
        if (!state.loaded) return
        viewModelScope.launch {
            repository.savePlayers(state.players)
            repository.saveHistory(state.history)
            repository.saveViewMode(state.viewMode)
        }
    }
}
