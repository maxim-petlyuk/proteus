package io.proteus.core.provider

interface FeatureConfigProvider {

    fun getBoolean(featureKey: String): Boolean

    fun getString(featureKey: String): String

    fun getLong(featureKey: String): Long

    fun getDouble(featureKey: String): Double
}
