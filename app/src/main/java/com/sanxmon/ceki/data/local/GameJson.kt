package com.sanxmon.ceki.data.local

import com.sanxmon.ceki.domain.model.HistoryLog
import com.sanxmon.ceki.domain.model.HistoryType
import com.sanxmon.ceki.domain.model.NameChangeExtra
import com.sanxmon.ceki.domain.model.Player
import com.sanxmon.ceki.domain.model.ViewMode
import org.json.JSONArray
import org.json.JSONObject

/**
 * JSON serialization for persisted game data. Type strings match the original
 * React Native storage ("plus"/"minus"/"reset"/"nameChange", "grid"/"list").
 */
object GameJson {

    private const val KEY_NAME = "name"
    private const val KEY_SCORE = "score"
    private const val KEY_ID = "id"
    private const val KEY_AMOUNT = "amount"
    private const val KEY_TYPE = "type"
    private const val KEY_EXTRA = "extra"
    private const val KEY_OLD_NAME = "oldName"
    private const val KEY_NEW_NAME = "newName"
    private const val KEY_TIMESTAMP = "timestamp"

    // --- Players ---

    fun playersToJson(players: List<Player>): String = JSONArray().apply {
        players.forEach { player ->
            put(JSONObject()
                .put(KEY_NAME, player.name)
                .put(KEY_SCORE, player.score))
        }
    }.toString()

    /** Parses players; returns null when unreadable or the roster is not exactly 4. */
    fun playersFromJson(raw: String): List<Player>? = try {
        val array = JSONArray(raw)
        if (array.length() != 4) {
            null
        } else {
            (0 until array.length()).map { index ->
                val obj = array.getJSONObject(index)
                Player(name = obj.getString(KEY_NAME), score = obj.getInt(KEY_SCORE))
            }
        }
    } catch (e: Exception) {
        null
    }

    // --- History ---

    fun historyToJson(history: List<HistoryLog>): String = JSONArray().apply {
        history.forEach { log ->
            val obj = JSONObject()
                .put(KEY_ID, log.id)
                .put(KEY_NAME, log.name)
                .put(KEY_AMOUNT, log.amount)
                .put(KEY_TYPE, typeToString(log.type))
                .put(KEY_TIMESTAMP, log.timestamp)
            log.extra?.let { extra ->
                obj.put(
                    KEY_EXTRA,
                    JSONObject()
                        .put(KEY_OLD_NAME, extra.oldName)
                        .put(KEY_NEW_NAME, extra.newName),
                )
            }
            put(obj)
        }
    }.toString()

    fun historyFromJson(raw: String): List<HistoryLog>? = try {
        val array = JSONArray(raw)
        (0 until array.length()).map { index ->
            val obj = array.getJSONObject(index)
            val extra = if (obj.has(KEY_EXTRA)) {
                val extraObj = obj.getJSONObject(KEY_EXTRA)
                NameChangeExtra(
                    oldName = extraObj.getString(KEY_OLD_NAME),
                    newName = extraObj.getString(KEY_NEW_NAME),
                )
            } else {
                null
            }
            HistoryLog(
                id = obj.getLong(KEY_ID),
                name = obj.getString(KEY_NAME),
                amount = obj.getInt(KEY_AMOUNT),
                type = typeFromString(obj.getString(KEY_TYPE)),
                extra = extra,
                timestamp = obj.getString(KEY_TIMESTAMP),
            )
        }
    } catch (e: Exception) {
        null
    }

    // --- View mode ---

    fun viewModeFromJson(raw: String): ViewMode? = when (raw) {
        "grid" -> ViewMode.GRID
        "list" -> ViewMode.LIST
        else -> null
    }

    private fun typeToString(type: HistoryType): String = when (type) {
        HistoryType.PLUS -> "plus"
        HistoryType.MINUS -> "minus"
        HistoryType.RESET -> "reset"
        HistoryType.NAME_CHANGE -> "nameChange"
    }

    private fun typeFromString(raw: String): HistoryType = when (raw) {
        "plus" -> HistoryType.PLUS
        "minus" -> HistoryType.MINUS
        "reset" -> HistoryType.RESET
        else -> HistoryType.NAME_CHANGE
    }
}
