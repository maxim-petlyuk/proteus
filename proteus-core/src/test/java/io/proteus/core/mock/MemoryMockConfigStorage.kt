package io.proteus.core.mock

import io.proteus.core.data.MockConfigStorage

internal class MemoryMockConfigStorage : MockConfigStorage {

    private val storage = mutableMapOf<String, Any>()

    override fun contains(featureKey: String): Boolean {
        return storage.contains(featureKey)
    }

    override fun getLong(featureKey: String): Long {
        return storage[featureKey] as? Long
            ?: throw IllegalArgumentException("Feature key $featureKey is not a Long")
    }

    override fun getDouble(featureKey: String): Double {
        return storage[featureKey] as? Double
            ?: throw IllegalArgumentException("Feature key $featureKey is not a Double")
    }

    override fun getString(featureKey: String): String {
        return storage[featureKey] as? String
            ?: throw IllegalArgumentException("Feature key $featureKey is not a String")
    }

    override fun getBoolean(featureKey: String): Boolean {
        return storage[featureKey] as? Boolean
            ?: throw IllegalArgumentException("Feature key $featureKey is not a Boolean")
    }

    override fun save(featureKey: String, value: Long) {
        storage[featureKey] = value
    }

    override fun save(featureKey: String, value: Double) {
        storage[featureKey] = value
    }

    override fun save(featureKey: String, value: String) {
        storage[featureKey] = value
    }

    override fun save(featureKey: String, value: Boolean) {
        storage[featureKey] = value
    }

    override fun remove(featureKey: String) {
        storage.remove(featureKey)
    }

    override fun clear() {
        storage.clear()
    }
}
