package io.proteus.core.data

import io.proteus.core.domain.Feature
import io.proteus.core.domain.FeatureContext
import io.proteus.core.domain.FeatureMetadata

class FeatureMetadataMapper {

    @Throws(IllegalArgumentException::class)
    internal fun toFeatureContext(from: FeatureMetadata): FeatureContext<*> {
        val featureKey = from.featureKey
        if (featureKey.isEmpty()) {
            throw IllegalArgumentException("Feature key must not be empty")
        }

        return when (from.valueType) {
            TYPE_TEXT -> {
                Feature<String>(
                    key = from.featureKey,
                    defaultValue = from.defaultValue,
                    valueClass = String::class,
                )
            }

            TYPE_LONG -> {
                val defaultValue = from.defaultValue.toLongOrNull()
                    ?: throw IllegalArgumentException("Feature [$featureKey] has invalid default value, expected one is Long")

                Feature<Long>(
                    key = from.featureKey,
                    defaultValue = defaultValue,
                    valueClass = Long::class,
                )
            }

            TYPE_BOOLEAN -> {
                val defaultValue = from.defaultValue.toBooleanStrictOrNull()
                    ?: throw IllegalArgumentException("Feature [$featureKey] has invalid default value, expected one is Boolean (true/false)")

                Feature<Boolean>(
                    key = from.featureKey,
                    defaultValue = defaultValue,
                    valueClass = Boolean::class,
                )
            }

            TYPE_DOUBLE -> {
                val defaultValue = from.defaultValue.toDoubleOrNull()
                    ?: throw IllegalArgumentException("Feature [$featureKey] has invalid default value, expected one is Double")

                Feature<Double>(
                    key = from.featureKey,
                    defaultValue = defaultValue,
                    valueClass = Double::class,
                )
            }

            else -> {
                throw IllegalArgumentException("Feature [$featureKey] has invalid value type")
            }
        }
    }

    private companion object {

        private const val TYPE_TEXT = "text"
        private const val TYPE_LONG = "long"
        private const val TYPE_BOOLEAN = "boolean"
        private const val TYPE_DOUBLE = "double"
    }
}
