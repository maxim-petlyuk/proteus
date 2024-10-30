package io.devtools.config.domain

interface FeatureConfigProvider {

    fun isFeatureAvailable(feature: Feature): Boolean

    fun getString(feature: Feature): String

    fun getLong(feature: Feature): Long

    fun getDouble(feature: Feature): Double
}
