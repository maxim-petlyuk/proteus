package io.proteus.core.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.security.MessageDigest

internal class PreferenceMockConfigStorage(
    private val context: Context,
    private val fileName: String = "config_storage"
) : MockConfigStorage {

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    override fun contains(featureKey: String): Boolean {
        return sharedPreferences.contains(featureKey.asPreferenceKey())
    }

    override fun getLong(featureKey: String): Long {
        return sharedPreferences.getLong(featureKey.asPreferenceKey(), DEFAULT_LONG)
    }

    override fun getDouble(featureKey: String): Double {
        return sharedPreferences.getFloat(featureKey.asPreferenceKey(), DEFAULT_FLOAT).toDouble()
    }

    override fun getString(featureKey: String): String {
        return sharedPreferences.getString(featureKey.asPreferenceKey(), DEFAULT_TEXT)
            ?: DEFAULT_TEXT
    }

    override fun getBoolean(featureKey: String): Boolean {
        return sharedPreferences.getBoolean(featureKey.asPreferenceKey(), DEFAULT_BOOLEAN)
    }

    override fun save(featureKey: String, value: Long) {
        sharedPreferences.edit {
            putLong(featureKey.asPreferenceKey(), value)
        }
    }

    override fun save(featureKey: String, value: Double) {
        sharedPreferences.edit {
            putFloat(featureKey.asPreferenceKey(), value.toFloat())
        }
    }

    override fun save(featureKey: String, value: String) {
        sharedPreferences.edit {
            putString(featureKey.asPreferenceKey(), value)
        }
    }

    override fun save(featureKey: String, value: Boolean) {
        sharedPreferences.edit {
            putBoolean(featureKey.asPreferenceKey(), value)
        }
    }

    override fun clear() {
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
