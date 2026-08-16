package com.sanxmon.ceki.data.local

import android.content.Context
import android.content.SharedPreferences

/** [KeyValueStore] backed by SharedPreferences. */
class SharedPrefsKeyValueStore(context: Context) : KeyValueStore {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getString(key: String): String? = prefs.getString(key, null)

    override fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    companion object {
        const val PREFS_NAME = "ceki_prefs"
    }
}
