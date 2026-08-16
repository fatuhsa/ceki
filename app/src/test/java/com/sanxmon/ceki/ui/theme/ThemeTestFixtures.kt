package com.sanxmon.ceki.ui.theme

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/** In-memory [ThemeRepository] used by unit tests. */
class FakeThemeRepository(initialThemeId: String = ThemeManager.DEFAULT_THEME.id) : ThemeRepository {

    private val state = MutableStateFlow(initialThemeId)

    override val themeIdFlow: Flow<String> = state

    override suspend fun setThemeId(themeId: String) {
        state.value = themeId
    }
}
