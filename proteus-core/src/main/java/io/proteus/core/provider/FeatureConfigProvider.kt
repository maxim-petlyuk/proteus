package io.proteus.core.provider

import io.proteus.core.domain.FeatureContext

interface FeatureConfigProvider {

    fun getBoolean(feature: FeatureContext<Boolean>): Boolean

    fun getString(feature: FeatureContext<String>): String

    fun getLong(feature: FeatureContext<Long>): Long

    fun getDouble(feature: FeatureContext<Double>): Double
}
