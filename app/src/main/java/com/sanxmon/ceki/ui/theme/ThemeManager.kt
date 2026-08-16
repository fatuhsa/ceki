package com.sanxmon.ceki.ui.theme

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Registry and runtime holder for the app themes.
 *
 * ThemeManager
 *  ├── availableThemes()
 *  ├── getCurrentTheme() (via [currentTheme])
 *  └── setTheme(themeId) → ThemeRepository → DataStore
 *
 * The current theme follows the persisted id; unknown ids fall back to the
 * default theme.
 */
class ThemeManager(private val repository: ThemeRepository) {

    val currentTheme: Flow<AppTheme> = repository.themeIdFlow
        .map { id -> getTheme(id) ?: DEFAULT_THEME }

    fun availableThemes(): List<AppTheme> = THEMES

    fun getTheme(themeId: String): AppTheme? = THEMES.firstOrNull { it.id == themeId }

    suspend fun setTheme(themeId: String) {
        repository.setThemeId(themeId)
    }

    companion object {
        val DEFAULT_THEME: AppTheme = MidnightTheme.theme

        /** Registry order is the order shown in the theme selector. */
        val THEMES: List<AppTheme> = listOf(
            MidnightTheme.theme,
            GoldenTheme.theme,
            NoirTheme.theme,
        )
    }
}
