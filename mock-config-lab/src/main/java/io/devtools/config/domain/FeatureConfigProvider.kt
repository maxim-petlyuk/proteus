package io.devtools.config.domain

interface FeatureConfigProvider {

    fun getBoolean(feature: Feature<Boolean>): Boolean

    fun getString(feature: Feature<String>): String

    fun getLong(feature: Feature<Long>): Long

    fun getDouble(feature: Feature<Double>): Double
}
