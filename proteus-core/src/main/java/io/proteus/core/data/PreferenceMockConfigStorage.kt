package io.proteus.core.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import kotlin.coroutines.CoroutineContext

internal class PreferenceMockConfigStorage(
    private val context: Context,
    private val fileName: String = "config_storage",
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : MockConfigStorage {

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    override suspend fun contains(featureKey: String): Boolean = withContext(coroutineContext) {
        sharedPreferences.contains(featureKey.asPreferenceKey())
    }

    override suspend fun getLong(featureKey: String): Long = withContext(coroutineContext) {
        sharedPreferences.getLong(featureKey.asPreferenceKey(), DEFAULT_LONG)
    }

    override suspend fun getDouble(featureKey: String): Double = withContext(coroutineContext) {
        sharedPreferences.getFloat(featureKey.asPreferenceKey(), DEFAULT_FLOAT).toString().toDouble()
    }

    override suspend fun getString(featureKey: String): String = withContext(coroutineContext) {
        sharedPreferences.getString(featureKey.asPreferenceKey(), DEFAULT_TEXT)
            ?: DEFAULT_TEXT
    }

    override suspend fun getBoolean(featureKey: String): Boolean = withContext(coroutineContext) {
        sharedPreferences.getBoolean(featureKey.asPreferenceKey(), DEFAULT_BOOLEAN)
    }

    override suspend fun save(featureKey: String, value: Long) = withContext(coroutineContext) {
        sharedPreferences.edit {
            putLong(featureKey.asPreferenceKey(), value)
        }
    }

    override suspend fun save(featureKey: String, value: Double) = withContext(coroutineContext) {
        sharedPreferences.edit {
            putFloat(featureKey.asPreferenceKey(), value.toFloat())
        }
    }

    override suspend fun save(featureKey: String, value: String) = withContext(coroutineContext) {
        sharedPreferences.edit {
            putString(featureKey.asPreferenceKey(), value)
        }
    }

    override suspend fun save(featureKey: String, value: Boolean) = withContext(coroutineContext) {
        sharedPreferences.edit {
            putBoolean(featureKey.asPreferenceKey(), value)
        }
    }

    override suspend fun remove(featureKey: String) = withContext(coroutineContext) {
        sharedPreferences.edit {
            remove(featureKey.asPreferenceKey())
        }
    }

    override suspend fun clear() = withContext(coroutineContext) {
        sharedPreferences.edit {
            clear()
        }
    }

    private fun String.asPreferenceKey(): String {
        return FEATURE_KEY_PREFIX + this.sha1()
    }

    private fun String.sha1(): String {
        val bytes = this.toByteArray()
        val md = MessageDigest.getInstance("SHA-1")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    private companion object {

        private const val FEATURE_KEY_PREFIX = "local_"
        private const val DEFAULT_LONG = 0L
        private const val DEFAULT_FLOAT = 0F
        private const val DEFAULT_TEXT = ""
        private const val DEFAULT_BOOLEAN = false
    }
}
