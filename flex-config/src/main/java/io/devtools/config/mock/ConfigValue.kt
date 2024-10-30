package io.devtools.config.mock

import kotlin.reflect.KClass
import kotlinx.serialization.Serializable

@Serializable
internal sealed class ConfigValue<Value> {

    abstract val value: Value

    override fun equals(other: Any?): kotlin.Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConfigValue<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value.toString()
    }

    @Serializable
    class Long(override val value: kotlin.Long) : ConfigValue<kotlin.Long>()

    @Serializable
    class Double(override val value: kotlin.Double) : ConfigValue<kotlin.Double>()

    @Serializable
    class Text(override val value: String) : ConfigValue<String>()

    @Serializable
    class Boolean(override val value: kotlin.Boolean) : ConfigValue<kotlin.Boolean>()

    companion object {

        fun fromBoolean(value: kotlin.Boolean): ConfigValue<*> {
            return Boolean(value)
        }

        fun fromText(value: String, expectedClass: KClass<*>): ConfigValue<*>? {
            return try {
                when (expectedClass) {
                    Int::class,
                    kotlin.Long::class -> {
                        Long(value.toLong())
                    }

                    kotlin.Double::class -> {
                        Double(value.toDouble())
                    }

                    String::class -> {
                        Text(value)
                    }

                    else -> {
                        null
                    }
                }
            } catch (error: Throwable) {
                null
            }
        }
    }
}
