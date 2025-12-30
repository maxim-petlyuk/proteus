package io.proteus.core.data

interface MockConfigStorage {

    suspend fun contains(featureKey: String): Boolean

    suspend fun getLong(featureKey: String): Long

    suspend fun getDouble(featureKey: String): Double

    suspend fun getString(featureKey: String): String

    suspend fun getBoolean(featureKey: String): Boolean

    suspend fun save(featureKey: String, value: Long)

    suspend fun save(featureKey: String, value: Double)

    suspend fun save(featureKey: String, value: String)

    suspend fun save(featureKey: String, value: Boolean)

    suspend fun remove(featureKey: String)

    suspend fun clear()
}
