package io.devtools.config.data

import io.devtools.config.exceptions.IllegalConfigDataTypeException
import io.devtools.config.provider.ConfigValue
import io.devtools.config.provider.Feature

internal interface MockConfigRepository {

    @Throws(IllegalConfigDataTypeException::class)
    fun getMockedConfigValue(feature: Feature<*>): ConfigValue<*>?
}
