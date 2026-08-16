package com.sanxmon.ceki.data.local

import com.sanxmon.ceki.domain.model.HistoryLog
import com.sanxmon.ceki.domain.model.Player
import com.sanxmon.ceki.domain.model.ViewMode
import com.sanxmon.ceki.domain.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** [GameRepository] backed by a [KeyValueStore]. Storage keys match the original app. */
class GameRepositoryImpl(private val store: KeyValueStore) : GameRepository {

    override suspend fun loadPlayers(): List<Player>? = withContext(Dispatchers.IO) {
        store.getString(KEY_PLAYERS)?.let(GameJson::playersFromJson)
    }

    override suspend fun loadHistory(): List<HistoryLog>? = withContext(Dispatchers.IO) {
        store.getString(KEY_HISTORY)?.let(GameJson::historyFromJson)
    }

    override suspend fun loadViewMode(): ViewMode? = withContext(Dispatchers.IO) {
        store.getString(KEY_VIEW_MODE)?.let(GameJson::viewModeFromJson)
    }

    override suspend fun savePlayers(players: List<Player>) = withContext(Dispatchers.IO) {
        store.putString(KEY_PLAYERS, GameJson.playersToJson(players))
    }

    override suspend fun saveHistory(history: List<HistoryLog>) = withContext(Dispatchers.IO) {
        store.putString(KEY_HISTORY, GameJson.historyToJson(history))
    }

    override suspend fun saveViewMode(viewMode: ViewMode) = withContext(Dispatchers.IO) {
        store.putString(KEY_VIEW_MODE, when (viewMode) {
            ViewMode.GRID -> "grid"
            ViewMode.LIST -> "list"
        })
    }

    override suspend fun clearGame() = withContext(Dispatchers.IO) {
        store.remove(KEY_PLAYERS)
        store.remove(KEY_HISTORY)
    }

    companion object {
        const val KEY_PLAYERS = "@ceki:players"
        const val KEY_HISTORY = "@ceki:score-history"
        const val KEY_VIEW_MODE = "@ceki:view-mode"
    }
}
