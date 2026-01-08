package io.proteus.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class DataStoreMockConfigStorage(
    private val dataStore: DataStore<Preferences>
) : MockConfigStorage {

    override suspend fun contains(featureKey: String): Boolean {
        return dataStore.data.map { preferences ->
            preferences.contains(featureKey.asPreferenceKey())
        }.first()
    }

    override suspend fun getLong(featureKey: String): Long {
        return dataStore.data.map { preferences ->
            preferences[featureKey.asLongPreferenceKey()] ?: DEFAULT_LONG
        }.first()
    }

    override suspend fun getDouble(featureKey: String): Double {
        return dataStore.data.map { preferences ->
            preferences[featureKey.asDoublePreferenceKey()] ?: DEFAULT_DOUBLE
        }.first()
    }

    override suspend fun getString(featureKey: String): String {
        return dataStore.data.map { preferences ->
            preferences[featureKey.asStringPreferenceKey()] ?: DEFAULT_TEXT
        }.first()
    }

    override suspend fun getBoolean(featureKey: String): Boolean {
        return dataStore.data.map { preferences ->
            preferences[featureKey.asBooleanPreferenceKey()] ?: DEFAULT_BOOLEAN
        }.first()
    }

    override suspend fun save(featureKey: String, value: Long) {
        dataStore.edit { preferences ->
            preferences[featureKey.asLongPreferenceKey()] = value
        }
    }

    override suspend fun save(featureKey: String, value: Double) {
        dataStore.edit { preferences ->
            preferences[featureKey.asDoublePreferenceKey()] = value
        }
    }

    override suspend fun save(featureKey: String, value: String) {
        dataStore.edit { preferences ->
            preferences[featureKey.asStringPreferenceKey()] = value
        }
    }

    override suspend fun save(featureKey: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[featureKey.asBooleanPreferenceKey()] = value
        }
    }

    override suspend fun remove(featureKey: String) {
        dataStore.edit { preferences ->
            preferences.remove(featureKey.asLongPreferenceKey())
            preferences.remove(featureKey.asDoublePreferenceKey())
            preferences.remove(featureKey.asStringPreferenceKey())
            preferences.remove(featureKey.asBooleanPreferenceKey())
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private fun String.asPreferenceKey(): Preferences.Key<*> {
        return stringPreferencesKey(FEATURE_KEY_PREFIX + this.sha1())
    }

    private fun String.asLongPreferenceKey(): Preferences.Key<Long> {
        return longPreferencesKey(FEATURE_KEY_PREFIX + this.sha1())
    }

    private fun String.asDoublePreferenceKey(): Preferences.Key<Double> {
        return doublePreferencesKey(FEATURE_KEY_PREFIX + this.sha1())
    }

    private fun String.asStringPreferenceKey(): Preferences.Key<String> {
        return stringPreferencesKey(FEATURE_KEY_PREFIX + this.sha1())
    }

    private fun String.asBooleanPreferenceKey(): Preferences.Key<Boolean> {
        return booleanPreferencesKey(FEATURE_KEY_PREFIX + this.sha1())
    }

    private fun String.sha1(): String {
        return this.hashCode().toString()
    }

    private companion object {
        private const val FEATURE_KEY_PREFIX = "local_"
        private const val DEFAULT_LONG = 0L
        private const val DEFAULT_DOUBLE = 0.0
        private const val DEFAULT_TEXT = ""
        private const val DEFAULT_BOOLEAN = false
    }
}