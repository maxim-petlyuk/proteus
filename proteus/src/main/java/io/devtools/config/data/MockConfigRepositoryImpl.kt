package io.devtools.config.data

import io.devtools.config.exceptions.IllegalConfigDataTypeException
import io.devtools.config.provider.ConfigValue
import io.devtools.config.provider.Feature

internal class MockConfigRepositoryImpl(
    private val mockConfigStorage: MockConfigStorage
) : MockConfigRepository {

    @Throws(IllegalConfigDataTypeException::class)
    override fun getMockedConfigValue(feature: Feature<*>): ConfigValue<*>? {
        if (!mockConfigStorage.contains(feature.key)) {
            return null
        }

        return when (feature.valueClass) {
            Int::class,
            Long::class -> {
                val localValue = mockConfigStorage.getLong(feature.key)
                ConfigValue.Long(localValue)
            }

            Double::class -> {
                val localValue = mockConfigStorage.getDouble(feature.key)
                ConfigValue.Double(localValue)
            }

            String::class -> {
                val localValue = mockConfigStorage.getString(feature.key)
                ConfigValue.Text(localValue)
            }

            Boolean::class -> {
                val localValue = mockConfigStorage.getBoolean(feature.key)
                ConfigValue.Boolean(localValue)
            }

            else -> {
                throw IllegalConfigDataTypeException("Unsupported data type: ${feature.valueClass}")
            }
        }
    }
}
