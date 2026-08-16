package com.sanxmon.ceki.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ScoreRulesTest {

    @Test
    fun appendDigit_limitsInputToFourDigits() {
        assertEquals("12", ScoreRules.appendDigit("1", '2'))
        assertEquals("1234", ScoreRules.appendDigit("123", '4'))
        // Fifth digit is rejected.
        assertEquals("1234", ScoreRules.appendDigit("1234", '5'))
        assertEquals("1234", ScoreRules.appendDigit("12345", '6'))
    }

    @Test
    fun backspace_removesLastDigit() {
        assertEquals("12", ScoreRules.backspace("123"))
        assertEquals("", ScoreRules.backspace("1"))
        assertEquals("", ScoreRules.backspace(""))
    }

    @Test
    fun errorFor_acceptsNonZeroMultipleOfFive() {
        assertNull(ScoreRules.errorFor("5"))
        assertNull(ScoreRules.errorFor("25"))
        assertNull(ScoreRules.errorFor("100"))
    }

    @Test
    fun errorFor_rejectsEmptyAndNonNumeric() {
        assertEquals(ScoreRules.ERROR_INVALID_NUMBER, ScoreRules.errorFor(""))
        assertEquals(ScoreRules.ERROR_INVALID_NUMBER, ScoreRules.errorFor("abc"))
        assertEquals(ScoreRules.ERROR_INVALID_NUMBER, ScoreRules.errorFor("5a"))
    }

    @Test
    fun errorFor_rejectsZero() {
        assertEquals(ScoreRules.ERROR_INVALID_NUMBER, ScoreRules.errorFor("0"))
    }

    @Test
    fun errorFor_rejectsNonMultipleOfFive() {
        assertEquals(ScoreRules.ERROR_NOT_MULTIPLE, ScoreRules.errorFor("3"))
        assertEquals(ScoreRules.ERROR_NOT_MULTIPLE, ScoreRules.errorFor("7"))
        assertEquals(ScoreRules.ERROR_NOT_MULTIPLE, ScoreRules.errorFor("22"))
    }
}
