package io.devtools.config.provider

import kotlin.reflect.KClass

data class Feature<DataType : Any>(
    override val key: String,
    override val defaultValue: DataType,
    override val valueClass: KClass<DataType>,
    override val owner: FeatureConfigOwner
) : FeatureContext<DataType>

interface FeatureContext<DataType : Any> {

    val key: String
    val defaultValue: DataType
    val valueClass: KClass<DataType>
    val owner: FeatureConfigOwner
}
