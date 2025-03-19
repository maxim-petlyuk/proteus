package io.proteus.core.provider

interface FeatureConfigProvider {

    fun getBoolean(feature: FeatureContext<Boolean>): Boolean

    fun getString(feature: FeatureContext<String>): String

    fun getLong(feature: FeatureContext<Long>): Long

    fun getDouble(feature: FeatureContext<Double>): Double
}
