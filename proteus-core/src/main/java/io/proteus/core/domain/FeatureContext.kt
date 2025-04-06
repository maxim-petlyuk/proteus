package io.proteus.core.domain

import kotlin.reflect.KClass

interface FeatureContext<DataType : Any> {

    val key: String
    val defaultValue: DataType
    val valueClass: KClass<DataType>
}