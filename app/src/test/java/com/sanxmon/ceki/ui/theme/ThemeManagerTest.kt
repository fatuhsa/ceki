package com.sanxmon.ceki.ui.theme

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeManagerTest {

    @Test
    fun currentTheme_defaultsToMidnightWhenUnset() = runBlocking {
        val manager = ThemeManager(FakeThemeRepository())

        assertEquals(ThemeManager.DEFAULT_THEME, manager.currentTheme.first())
        assertEquals("midnight", manager.currentTheme.first().id)
    }

    @Test
    fun setTheme_emitsTheNewThemeImmediately() = runBlocking {
        val manager = ThemeManager(FakeThemeRepository())

        manager.setTheme("noir")

        assertEquals("noir", manager.currentTheme.first().id)
        assertEquals("Noir", manager.currentTheme.first().displayName)
    }

    @Test
    fun setTheme_roundTripsThroughTheRepository() = runBlocking {
        val repository = FakeThemeRepository()
        val manager = ThemeManager(repository)

        manager.setTheme("golden")

        assertEquals("golden", repository.themeIdFlow.first())
        assertEquals(GoldenTheme.theme, manager.currentTheme.first())
    }

    @Test
    fun unknownPersistedId_fallsBackToDefaultTheme() = runBlocking {
        val manager = ThemeManager(FakeThemeRepository("not-a-theme"))

        assertEquals(ThemeManager.DEFAULT_THEME, manager.currentTheme.first())
    }
}
