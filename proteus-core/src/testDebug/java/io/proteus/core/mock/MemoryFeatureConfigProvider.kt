package io.proteus.core.mock

import io.proteus.core.provider.FeatureConfigProvider

internal class MemoryFeatureConfigProvider(
    private val featuresGuide: List<FeatureTestGuide<*>>
) : FeatureConfigProvider {

    constructor(vararg featuresGuide: FeatureTestGuide<*>) : this(featuresGuide.toList())

    override fun getBoolean(featureKey: String): Boolean {
        return featuresGuide.find { it.featureKey == featureKey }
            ?.getValue() as? Boolean
            ?: throw IllegalStateException("Feature guide not found: $featureKey")
    }

    override fun getString(featureKey: String): String {
        return featuresGuide.find { it.featureKey == featureKey }
            ?.getValue() as? String
            ?: throw IllegalStateException("Feature guide not found: $featureKey")
    }

    override fun getLong(featureKey: String): Long {
        return featuresGuide.find { it.featureKey == featureKey }
            ?.getValue() as? Long
            ?: throw IllegalStateException("Feature guide not found: $featureKey")
    }

    override fun getDouble(featureKey: String): Double {
        return featuresGuide.find { it.featureKey == featureKey }
            ?.getValue() as? Double
            ?: throw IllegalStateException("Feature guide not found: $featureKey")
    }
}
