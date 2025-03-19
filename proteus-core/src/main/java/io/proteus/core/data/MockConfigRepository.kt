package io.proteus.core.data

import io.proteus.core.exceptions.IllegalConfigDataTypeException
import io.proteus.core.provider.ConfigValue
import io.proteus.core.provider.FeatureContext

interface MockConfigRepository {

    @Throws(IllegalConfigDataTypeException::class)
    fun getMockedConfigValue(feature: FeatureContext<*>): ConfigValue<*>?
}
