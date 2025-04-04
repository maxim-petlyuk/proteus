package io.proteus.core.data

import io.proteus.core.domain.ConfigValue
import io.proteus.core.domain.FeatureContext
import io.proteus.core.exceptions.IllegalConfigDataTypeException

internal class MockConfigRepositoryImpl(
    private val mockConfigStorage: MockConfigStorage
) : MockConfigRepository {

    @Throws(IllegalConfigDataTypeException::class)
    override fun getMockedConfigValue(feature: FeatureContext<*>): ConfigValue<*>? {
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

    override fun save(feature: FeatureContext<*>, configValue: ConfigValue<*>) {
        when (feature.valueClass) {
            Int::class,
            Long::class -> {
                configValue as? ConfigValue.Long
                    ?: throw IllegalConfigDataTypeException("Invalid data type: ${configValue::class}")

                mockConfigStorage.save(feature.key, configValue.value)
            }

            Double::class -> {
                configValue as? ConfigValue.Double
                    ?: throw IllegalConfigDataTypeException("Invalid data type: ${configValue::class}")

                mockConfigStorage.save(feature.key, configValue.value)
            }

            String::class -> {
                configValue as? ConfigValue.Text
                    ?: throw IllegalConfigDataTypeException("Invalid data type: ${configValue::class}")

                mockConfigStorage.save(feature.key, configValue.value)
            }

            Boolean::class -> {
                configValue as? ConfigValue.Boolean
                    ?: throw IllegalConfigDataTypeException("Invalid data type: ${configValue::class}")

                mockConfigStorage.save(feature.key, configValue.value)
            }

            else -> {
                throw IllegalConfigDataTypeException("Unsupported data type: ${feature.valueClass}")
            }
        }
    }

    override fun remove(feature: FeatureContext<*>) {
        mockConfigStorage.remove(feature.key)
    }
}
