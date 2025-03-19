package io.proteus.core.data

import io.proteus.core.domain.FeatureMetadata
import io.proteus.core.provider.Feature
import io.proteus.core.provider.FeatureConfigOwner
import io.proteus.core.provider.FeatureContext

internal class FeatureMetadataMapper {

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
                    owner = Stub()
                )
            }

            TYPE_LONG -> {
                val defaultValue = from.defaultValue.toLongOrNull()
                    ?: throw IllegalArgumentException("Feature [$featureKey] has invalid default value, expected one is Long")

                Feature<Long>(
                    key = from.featureKey,
                    defaultValue = defaultValue,
                    valueClass = Long::class,
                    owner = Stub()
                )
            }

            TYPE_BOOLEAN -> {
                val defaultValue = from.defaultValue.toBooleanStrictOrNull()
                    ?: throw IllegalArgumentException("Feature [$featureKey] has invalid default value, expected one is Boolean (true/false)")

                Feature<Boolean>(
                    key = from.featureKey,
                    defaultValue = defaultValue,
                    valueClass = Boolean::class,
                    owner = Stub()
                )
            }

            TYPE_DOUBLE -> {
                val defaultValue = from.defaultValue.toDoubleOrNull()
                    ?: throw IllegalArgumentException("Feature [$featureKey] has invalid default value, expected one is Double")

                Feature<Double>(
                    key = from.featureKey,
                    defaultValue = defaultValue,
                    valueClass = Double::class,
                    owner = Stub()
                )
            }

            else -> {
                throw IllegalArgumentException("Feature [$featureKey] has invalid value type")
            }
        }
    }

    private class Stub : FeatureConfigOwner

    private companion object {

        private const val TYPE_TEXT = "text"
        private const val TYPE_LONG = "long"
        private const val TYPE_BOOLEAN = "boolean"
        private const val TYPE_DOUBLE = "double"
    }
}
