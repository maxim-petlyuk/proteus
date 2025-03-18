package io.devtools.config.mock

import io.devtools.config.provider.Feature
import io.devtools.config.provider.FeatureConfigProvider

internal class MemoryFeatureConfigProvider(
    private val featuresGuide: List<FeatureTestGuide<*>>
) : FeatureConfigProvider {

    constructor(vararg featuresGuide: FeatureTestGuide<*>) : this(featuresGuide.toList())

    override fun getBoolean(feature: Feature<Boolean>): Boolean {
        return featuresGuide.find { it.feature.key == feature.key }
            ?.getValue() as? Boolean
            ?: throw IllegalStateException("Feature guide not found: ${feature.key}")
    }

    override fun getString(feature: Feature<String>): String {
        return featuresGuide.find { it.feature.key == feature.key }
            ?.getValue() as? String
            ?: throw IllegalStateException("Feature guide not found: ${feature.key}")
    }

    override fun getLong(feature: Feature<Long>): Long {
        return featuresGuide.find { it.feature.key == feature.key }
            ?.getValue() as? Long
            ?: throw IllegalStateException("Feature guide not found: ${feature.key}")
    }

    override fun getDouble(feature: Feature<Double>): Double {
        return featuresGuide.find { it.feature.key == feature.key }
            ?.getValue() as? Double
            ?: throw IllegalStateException("Feature guide not found: ${feature.key}")
    }
}
