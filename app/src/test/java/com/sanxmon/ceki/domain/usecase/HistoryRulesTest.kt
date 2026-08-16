package com.sanxmon.ceki.domain.usecase

import com.sanxmon.ceki.domain.model.HistoryLog
import com.sanxmon.ceki.domain.model.HistoryType
import com.sanxmon.ceki.domain.model.NameChangeExtra
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HistoryRulesTest {

    @Test
    fun newEntry_createsEntryWithDefaults() {
        val entry = HistoryRules.newEntry("Player 1", 25, HistoryType.PLUS)

        assertEquals("Player 1", entry.name)
        assertEquals(25, entry.amount)
        assertEquals(HistoryType.PLUS, entry.type)
        assertTrue(entry.id > 0)
        assertTrue(entry.timestamp.matches(Regex("\\d{2}:\\d{2}")))
    }

    @Test
    fun newEntry_carriesNameChangeExtra() {
        val entry = HistoryRules.newEntry(
            name = "Lama",
            amount = 0,
            type = HistoryType.NAME_CHANGE,
            extra = NameChangeExtra("Lama", "Baru"),
        )

        assertEquals("Lama", entry.extra?.oldName)
        assertEquals("Baru", entry.extra?.newName)
    }

    @Test
    fun prepend_addsNewestFirst() {
        val first = HistoryRules.newEntry("A", 5, HistoryType.PLUS)
        val second = HistoryRules.newEntry("B", 10, HistoryType.PLUS)

        val result = HistoryRules.prepend(listOf(first), second)

        assertEquals(listOf(second, first), result)
    }

    @Test
    fun prepend_capsAtFiftyEntries() {
        var history = emptyList<HistoryLog>()
        repeat(60) { index ->
            history = HistoryRules.prepend(
                history,
                HistoryRules.newEntry("Player", index + 1, HistoryType.PLUS),
            )
        }

        assertEquals(HistoryRules.MAX_ENTRIES, history.size)
        // Newest entry (the last added) is first.
        assertEquals(60, history.first().amount)
        // Oldest entries were dropped.
        assertEquals(11, history.last().amount)
    }
}
