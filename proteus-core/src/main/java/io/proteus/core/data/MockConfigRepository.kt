package io.proteus.core.data

import io.proteus.core.domain.ConfigValue
import io.proteus.core.domain.FeatureContext
import io.proteus.core.exceptions.IllegalConfigDataTypeException

interface MockConfigRepository {

    @Throws(IllegalConfigDataTypeException::class)
    fun getMockedConfigValue(feature: FeatureContext<*>): ConfigValue<*>?

    fun save(feature: FeatureContext<*>, configValue: ConfigValue<*>)

    fun remove(feature: FeatureContext<*>)
}
