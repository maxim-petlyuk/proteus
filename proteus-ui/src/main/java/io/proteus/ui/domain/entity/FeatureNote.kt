package io.proteus.ui.domain.entity

import io.proteus.core.provider.ConfigValue
import io.proteus.core.provider.Feature

internal data class FeatureNote<DataType : Any>(
    val feature: Feature<DataType>,
    val remoteConfigValue: String,
    private val localConfigValue: ConfigValue<DataType>? = null,
) {

    val mockedConfigValue: String?
        get() = localConfigValue?.toString()

    val isOverloaded: Boolean
        get() = localConfigValue != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FeatureNote<*>

        if (feature.key != other.feature.key) return false

        return true
    }

    override fun hashCode(): Int {
        var result = feature.hashCode()
        result = 31 * result + remoteConfigValue.hashCode()
        result = 31 * result + localConfigValue.hashCode()
        return result
    }
}
