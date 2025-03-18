package io.proteus.core.provider

import kotlinx.serialization.Serializable

@Serializable
internal sealed class ConfigValue<Value> {

    abstract val value: Value

    override fun equals(other: Any?): kotlin.Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConfigValue<*>

        return value == other.value
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
}
