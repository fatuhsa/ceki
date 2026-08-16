package com.sanxmon.ceki.data.local

/**
 * Minimal string key-value store abstraction. The production implementation is
 * backed by SharedPreferences; tests inject an in-memory fake.
 */
interface KeyValueStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
}
