package com.sanxmon.ceki.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NameRulesTest {

    @Test
    fun validNamesAreAccepted() {
        assertNull(NameRules.errorFor("Player 1"))
        assertNull(NameRules.errorFor("Budi"))
        assertNull(NameRules.errorFor("  Budi  "))
    }

    @Test
    fun emptyAndBlankNamesAreRejected() {
        assertEquals(NameRules.ERROR_EMPTY, NameRules.errorFor(""))
        assertEquals(NameRules.ERROR_EMPTY, NameRules.errorFor("   "))
        assertEquals(NameRules.ERROR_EMPTY, NameRules.errorFor("\t\n"))
    }
}
