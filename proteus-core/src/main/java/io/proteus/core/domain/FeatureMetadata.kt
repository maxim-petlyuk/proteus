package io.proteus.core.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FeatureMetadata(
    @SerialName("feature_key")
    val featureKey: String,

    @SerialName("default_value")
    val defaultValue: String,

    @SerialName("value_type")
    val valueType: String
)