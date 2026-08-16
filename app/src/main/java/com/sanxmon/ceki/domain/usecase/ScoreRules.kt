package com.sanxmon.ceki.domain.usecase

/**
 * Score input and validation rules, preserving the original behavior:
 * input capped at 4 digits, values must be non-zero multiples of 5.
 */
object ScoreRules {
    const val MAX_DIGITS = 4
    const val STEP = 5

    const val ERROR_NO_SELECTION = "Pilih player terlebih dahulu"
    const val ERROR_INVALID_NUMBER = "Masukkan angka valid"
    const val ERROR_NOT_MULTIPLE = "Harus kelipatan 5"

    fun appendDigit(current: String, digit: Char): String =
        if (current.length >= MAX_DIGITS) current else current + digit

    fun backspace(current: String): String = current.dropLast(1)

    /** Returns the error message for an invalid input, or null when valid. */
    fun errorFor(input: String): String? {
        val value = input.toIntOrNull() ?: return ERROR_INVALID_NUMBER
        if (value == 0) return ERROR_INVALID_NUMBER
        if (value % STEP != 0) return ERROR_NOT_MULTIPLE
        return null
    }
}
