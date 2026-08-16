package com.sanxmon.ceki.domain.repository

import com.sanxmon.ceki.domain.model.HistoryLog
import com.sanxmon.ceki.domain.model.Player
import com.sanxmon.ceki.domain.model.ViewMode

/**
 * Persistence boundary for the game. Load methods return null when the stored
 * value is missing or unreadable, in which case the caller falls back to defaults.
 */
interface GameRepository {
    suspend fun loadPlayers(): List<Player>?
    suspend fun loadHistory(): List<HistoryLog>?
    suspend fun loadViewMode(): ViewMode?

    suspend fun savePlayers(players: List<Player>)
    suspend fun saveHistory(history: List<HistoryLog>)
    suspend fun saveViewMode(viewMode: ViewMode)

    /** Removes players and history storage (used by "new game"). */
    suspend fun clearGame()
}
