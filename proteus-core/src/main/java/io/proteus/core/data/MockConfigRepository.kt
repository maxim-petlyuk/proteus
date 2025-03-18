package io.proteus.core.data

import io.proteus.core.exceptions.IllegalConfigDataTypeException
import io.proteus.core.provider.ConfigValue
import io.proteus.core.provider.Feature

internal interface MockConfigRepository {

    @Throws(IllegalConfigDataTypeException::class)
    fun getMockedConfigValue(feature: Feature<*>): ConfigValue<*>?
}
