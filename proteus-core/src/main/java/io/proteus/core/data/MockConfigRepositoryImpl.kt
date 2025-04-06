package io.proteus.core.data

import io.proteus.core.domain.ConfigValue
import io.proteus.core.exceptions.IllegalConfigDataTypeException
import kotlin.reflect.KClass

internal class MockConfigRepositoryImpl(
    private val mockConfigStorage: MockConfigStorage
) : MockConfigRepository {

    @Throws(IllegalConfigDataTypeException::class)
    override fun getMockedConfigValue(featureKey: String, typeClass: KClass<*>): ConfigValue<*>? {
        if (!mockConfigStorage.contains(featureKey)) {
            return null
        }

        return when (typeClass) {
            Int::class,
            Long::class -> {
                val localValue = mockConfigStorage.getLong(featureKey)
                ConfigValue.Long(localValue)
            }

            Double::class -> {
                val localValue = mockConfigStorage.getDouble(featureKey)
                ConfigValue.Double(localValue)
            }

            String::class -> {
                val localValue = mockConfigStorage.getString(featureKey)
                ConfigValue.Text(localValue)
            }

            Boolean::class -> {
                val localValue = mockConfigStorage.getBoolean(featureKey)
                ConfigValue.Boolean(localValue)
            }

            else -> {
                throw IllegalConfigDataTypeException("Unsupported data type: $typeClass")
            }
        }
    }

    override fun save(featureKey: String, typeClass: KClass<*>, configValue: ConfigValue<*>) {
        when (typeClass) {
            Int::class,
            Long::class -> {
                configValue as? ConfigValue.Long
                    ?: throw IllegalConfigDataTypeException("Invalid data type: ${configValue::class}")

                mockConfigStorage.save(featureKey, configValue.value)
            }

            Double::class -> {
                configValue as? ConfigValue.Double
                    ?: throw IllegalConfigDataTypeException("Invalid data type: ${configValue::class}")

                mockConfigStorage.save(featureKey, configValue.value)
            }

            String::class -> {
                configValue as? ConfigValue.Text
                    ?: throw IllegalConfigDataTypeException("Invalid data type: ${configValue::class}")

                mockConfigStorage.save(featureKey, configValue.value)
            }

            Boolean::class -> {
                configValue as? ConfigValue.Boolean
                    ?: throw IllegalConfigDataTypeException("Invalid data type: ${configValue::class}")

                mockConfigStorage.save(featureKey, configValue.value)
            }

            else -> {
                throw IllegalConfigDataTypeException("Unsupported data type: $typeClass")
            }
        }
    }

    override fun remove(featureKey: String) {
        mockConfigStorage.remove(featureKey)
    }
}
