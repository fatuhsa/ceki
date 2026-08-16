package com.sanxmon.ceki.ui.theme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeRegistryTest {

    @Test
    fun registry_hasThreeGenericThemes() {
        val themes = ThemeManager.THEMES

        assertEquals(3, themes.size)
        assertEquals(themes.map { it.id }.toSet().size, themes.size)
        assertEquals(themes.map { it.displayName }.toSet().size, themes.size)
        assertTrue(themes.all { it.displayName.isNotBlank() })
    }

    @Test
    fun defaultThemeIsMidnight() {
        assertEquals("midnight", ThemeManager.DEFAULT_THEME.id)
        assertEquals("Midnight", ThemeManager.DEFAULT_THEME.displayName)
    }

    @Test
    fun midnight_usesEditorialSpecPalette() {
        val c = MidnightTheme.colors
        assertEquals(Color(0xFF0D0D0D), c.background)
        assertEquals(Color(0xFF171717), c.surface)
        assertEquals(Color(0xFF212121), c.surfaceElevated)
        assertEquals(Color(0xFFFFD600), c.primary)
        assertEquals(Color(0xFFFFD600), c.accent)
        assertEquals(Color(0xFFFF3B30), c.error)
    }

    @Test
    fun themesHaveDistinctVisualIdentity() {
        val themes = ThemeManager.THEMES

        // Backgrounds, primaries, accents and pressed accents are pairwise distinct.
        assertEquals(3, themes.map { it.colors.background }.toSet().size)
        assertEquals(3, themes.map { it.colors.primary }.toSet().size)
        assertEquals(3, themes.map { it.colors.accent }.toSet().size)
        assertEquals(3, themes.map { it.colors.accentPressed }.toSet().size)
        // Dark vs light intent matches the design.
        assertTrue(MidnightTheme.colors.isDark)
        assertTrue(!GoldenTheme.colors.isDark)
        assertTrue(NoirTheme.colors.isDark)
    }

    @Test
    fun getTheme_resolvesByIdAndUnknownFallsBackToNull() {
        val manager = ThemeManager(FakeThemeRepository())

        assertNotNull(manager.getTheme("golden"))
        assertEquals(GoldenTheme.theme, manager.getTheme("golden"))
        assertEquals(NoirTheme.theme, manager.getTheme("noir"))
        assertNull(manager.getTheme("does-not-exist"))
    }

    @Test
    fun availableThemes_matchesRegistryOrder() {
        val manager = ThemeManager(FakeThemeRepository())
        assertEquals(ThemeManager.THEMES, manager.availableThemes())
    }
}
