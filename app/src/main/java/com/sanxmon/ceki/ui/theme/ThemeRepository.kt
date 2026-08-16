package com.sanxmon.ceki.ui.theme

import kotlinx.coroutines.flow.Flow

/**
 * Persistence boundary for the selected theme id. The production
 * implementation stores the value in DataStore Preferences; tests inject a
 * fake.
 */
interface ThemeRepository {
    val themeIdFlow: Flow<String>
    suspend fun setThemeId(themeId: String)
}
