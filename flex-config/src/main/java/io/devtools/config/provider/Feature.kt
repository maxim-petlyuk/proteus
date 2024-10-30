package io.devtools.config.provider

import kotlin.reflect.KClass

data class Feature<DataType : Any>(
    val key: String,
    val defaultValue: DataType,
    val valueClass: KClass<DataType>,
    val owner: FeatureConfigOwner
)
