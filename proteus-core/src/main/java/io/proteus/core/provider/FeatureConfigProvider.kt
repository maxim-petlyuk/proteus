package io.proteus.core.provider

interface FeatureConfigProvider {

    suspend fun getBoolean(featureKey: String): Boolean

    suspend fun getString(featureKey: String): String

    suspend fun getLong(featureKey: String): Long

    suspend fun getDouble(featureKey: String): Double
}
