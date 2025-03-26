package io.proteus.core.domain

import kotlin.reflect.KClass

internal data class Feature<DataType : Any>(
    override val key: String,
    override val defaultValue: DataType,
    override val valueClass: KClass<DataType>,
    override val owner: String
) : FeatureContext<DataType>
