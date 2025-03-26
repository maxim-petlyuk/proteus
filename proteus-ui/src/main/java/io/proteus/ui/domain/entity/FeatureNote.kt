package io.proteus.ui.domain.entity

import io.proteus.core.domain.ConfigValue
import io.proteus.core.domain.FeatureContext

internal data class FeatureNote<DataType : Any>(
    val feature: FeatureContext<DataType>,
    val remoteConfigValue: String,
    val localConfigValue: ConfigValue<DataType>? = null,
) {

    val mockedConfigValue: String?
        get() = localConfigValue?.toString()

    val isOverrideActivated: Boolean
        get() = localConfigValue != null
}
