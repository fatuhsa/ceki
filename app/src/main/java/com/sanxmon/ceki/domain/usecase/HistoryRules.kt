package com.sanxmon.ceki.domain.usecase

import com.sanxmon.ceki.domain.model.HistoryLog
import com.sanxmon.ceki.domain.model.HistoryType
import com.sanxmon.ceki.domain.model.NameChangeExtra
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/** History log rules: newest first, capped at 50 entries. */
object HistoryRules {
    const val MAX_ENTRIES = 50

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun timestamp(): String = LocalTime.now().format(timeFormatter)

    fun newEntry(
        name: String,
        amount: Int,
        type: HistoryType,
        extra: NameChangeExtra? = null,
    ): HistoryLog = HistoryLog(
        id = System.currentTimeMillis(),
        name = name,
        amount = amount,
        type = type,
        extra = extra,
        timestamp = timestamp(),
    )

    fun prepend(history: List<HistoryLog>, entry: HistoryLog): List<HistoryLog> =
        (listOf(entry) + history).take(MAX_ENTRIES)
}
