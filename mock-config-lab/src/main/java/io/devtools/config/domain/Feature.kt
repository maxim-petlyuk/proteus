package io.devtools.config.domain

import kotlin.reflect.KClass

data class Feature(
    val key: String,
    val defaultValue: Any,
    val valueClass: KClass<*>,
    val owner: FeatureConfigOwner
)
