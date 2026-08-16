package com.sanxmon.ceki.data.local

import com.sanxmon.ceki.domain.model.HistoryLog
import com.sanxmon.ceki.domain.model.HistoryType
import com.sanxmon.ceki.domain.model.NameChangeExtra
import com.sanxmon.ceki.domain.model.Player
import com.sanxmon.ceki.domain.model.ViewMode
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class GameRepositoryImplTest {

    private class InMemoryKeyValueStore : KeyValueStore {
        private val map = mutableMapOf<String, String>()
        override fun getString(key: String): String? = map[key]
        override fun putString(key: String, value: String) {
            map[key] = value
        }

        override fun remove(key: String) {
            map.remove(key)
        }
    }

    private val store = InMemoryKeyValueStore()
    private val repository = GameRepositoryImpl(store)

    private val players = listOf(
        Player("A", 0),
        Player("B", 25),
        Player("C", 50),
        Player("D", 75),
    )

    private val history = listOf(
        HistoryLog(
            id = 1L,
            name = "B",
            amount = 25,
            type = HistoryType.PLUS,
            timestamp = "10:00",
        ),
        HistoryLog(
            id = 2L,
            name = "Lama",
            amount = 0,
            type = HistoryType.NAME_CHANGE,
            extra = NameChangeExtra("Lama", "Baru"),
            timestamp = "10:05",
        ),
    )

    @Test
    fun roundTrip_savesAndLoadsPlayers() = runBlocking {
        repository.savePlayers(players)
        assertEquals(players, repository.loadPlayers())
    }

    @Test
    fun roundTrip_savesAndLoadsHistory() = runBlocking {
        repository.saveHistory(history)
        assertEquals(history, repository.loadHistory())
    }

    @Test
    fun roundTrip_savesAndLoadsViewMode() = runBlocking {
        repository.saveViewMode(ViewMode.LIST)
        assertEquals(ViewMode.LIST, repository.loadViewMode())
    }

    @Test
    fun loadPlayers_returnsNullWhenMissing() = runBlocking {
        assertNull(repository.loadPlayers())
    }

    @Test
    fun loadPlayers_returnsNullWhenRosterIsNotFour() = runBlocking {
        store.putString(GameRepositoryImpl.KEY_PLAYERS, "[{\"name\":\"A\",\"score\":0}]")
        assertNull(repository.loadPlayers())
    }

    @Test
    fun loadPlayers_returnsNullWhenCorrupt() = runBlocking {
        store.putString(GameRepositoryImpl.KEY_PLAYERS, "not json")
        assertNull(repository.loadPlayers())
    }

    @Test
    fun loadHistory_returnsNullWhenCorrupt() = runBlocking {
        store.putString(GameRepositoryImpl.KEY_HISTORY, "not json")
        assertNull(repository.loadHistory())
    }

    @Test
    fun loadViewMode_returnsNullForUnknownValue() = runBlocking {
        store.putString(GameRepositoryImpl.KEY_VIEW_MODE, "weird")
        assertNull(repository.loadViewMode())
    }

    @Test
    fun clearGame_removesPlayersAndHistory() = runBlocking {
        repository.savePlayers(players)
        repository.saveHistory(history)
        repository.saveViewMode(ViewMode.GRID)

        repository.clearGame()

        assertNull(repository.loadPlayers())
        assertNull(repository.loadHistory())
        // View mode is intentionally preserved (matches the original app).
        assertNotNull(repository.loadViewMode())
    }
}
