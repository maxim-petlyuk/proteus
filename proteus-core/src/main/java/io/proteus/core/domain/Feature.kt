package io.proteus.core.domain

import kotlin.reflect.KClass

data class Feature<DataType : Any>(
    override val key: String,
    override val defaultValue: DataType,
    override val valueClass: KClass<DataType>,
) : FeatureContext<DataType>
