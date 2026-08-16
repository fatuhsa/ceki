package com.sanxmon.ceki.domain.usecase

/** Player name validation rules. */
object NameRules {
    const val ERROR_EMPTY = "Nama player tidak boleh kosong"

    /** Returns the error message for an invalid name, or null when valid. */
    fun errorFor(name: String): String? =
        if (name.trim().isEmpty()) ERROR_EMPTY else null
}
