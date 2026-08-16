package com.sanxmon.ceki.domain.model

enum class HistoryType { PLUS, MINUS, RESET, NAME_CHANGE }

data class NameChangeExtra(
    val oldName: String,
    val newName: String,
)

data class HistoryLog(
    val id: Long,
    val name: String,
    val amount: Int,
    val type: HistoryType,
    val extra: NameChangeExtra? = null,
    val timestamp: String,
)
