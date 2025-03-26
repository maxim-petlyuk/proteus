package io.proteus.core.domain

import io.proteus.core.provider.FeatureConfigOwner
import kotlin.reflect.KClass

data class Feature<DataType : Any>(
    override val key: String,
    override val defaultValue: DataType,
    override val valueClass: KClass<DataType>,
    override val owner: FeatureConfigOwner
) : FeatureContext<DataType>
