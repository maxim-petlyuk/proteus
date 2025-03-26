package io.proteus.core.mock

import io.proteus.core.domain.FeatureContext
import io.proteus.core.provider.FeatureConfigProvider

internal class MemoryFeatureConfigProvider(
    private val featuresGuide: List<FeatureTestGuide<*>>
) : FeatureConfigProvider {

    constructor(vararg featuresGuide: FeatureTestGuide<*>) : this(featuresGuide.toList())

    override fun getBoolean(feature: FeatureContext<Boolean>): Boolean {
        return featuresGuide.find { it.feature.key == feature.key }
            ?.getValue() as? Boolean
            ?: throw IllegalStateException("Feature guide not found: ${feature.key}")
    }

    override fun getString(feature: FeatureContext<String>): String {
        return featuresGuide.find { it.feature.key == feature.key }
            ?.getValue() as? String
            ?: throw IllegalStateException("Feature guide not found: ${feature.key}")
    }

    override fun getLong(feature: FeatureContext<Long>): Long {
        return featuresGuide.find { it.feature.key == feature.key }
            ?.getValue() as? Long
            ?: throw IllegalStateException("Feature guide not found: ${feature.key}")
    }

    override fun getDouble(feature: FeatureContext<Double>): Double {
        return featuresGuide.find { it.feature.key == feature.key }
            ?.getValue() as? Double
            ?: throw IllegalStateException("Feature guide not found: ${feature.key}")
    }
}
