package io.proteus.core.data

interface MockConfigStorage {

    fun contains(featureKey: String): Boolean

    fun getLong(featureKey: String): Long

    fun getDouble(featureKey: String): Double

    fun getString(featureKey: String): String

    fun getBoolean(featureKey: String): Boolean

    fun save(featureKey: String, value: Long)

    fun save(featureKey: String, value: Double)

    fun save(featureKey: String, value: String)

    fun save(featureKey: String, value: Boolean)

    fun clear()
}
