package io.proteus.core.data

import io.proteus.core.domain.ConfigValue
import io.proteus.core.exceptions.IllegalConfigDataTypeException
import kotlin.reflect.KClass

interface MockConfigRepository {

    @Throws(IllegalConfigDataTypeException::class)
    suspend fun getMockedConfigValue(featureKey: String, typeClass: KClass<*>): ConfigValue<*>?

    suspend fun save(featureKey: String, typeClass: KClass<*>, configValue: ConfigValue<*>)

    suspend fun remove(featureKey: String)
}
