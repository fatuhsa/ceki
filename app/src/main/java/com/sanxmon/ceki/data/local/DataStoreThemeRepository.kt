package com.sanxmon.ceki.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sanxmon.ceki.ui.theme.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

/** [ThemeRepository] backed by DataStore Preferences (key: `theme_id`). */
class DataStoreThemeRepository(private val context: Context) : ThemeRepository {

    private val themeIdKey = stringPreferencesKey(KEY_THEME_ID)

    override val themeIdFlow: Flow<String> = context.settingsDataStore.data.map { prefs ->
        prefs[themeIdKey] ?: DEFAULT_THEME_ID
    }

    override suspend fun setThemeId(themeId: String) {
        context.settingsDataStore.edit { prefs ->
            prefs[themeIdKey] = themeId
        }
    }

    companion object {
        const val DEFAULT_THEME_ID = "midnight"
        private const val KEY_THEME_ID = "theme_id"
    }
}
