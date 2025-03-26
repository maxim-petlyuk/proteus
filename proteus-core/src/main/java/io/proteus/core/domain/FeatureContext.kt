package io.proteus.core.domain

import io.proteus.core.provider.FeatureConfigOwner
import kotlin.reflect.KClass

interface FeatureContext<DataType : Any> {

    val key: String
    val defaultValue: DataType
    val valueClass: KClass<DataType>
    val owner: FeatureConfigOwner
}