package io.proteus.ui.presentation.configurator

import androidx.compose.ui.text.input.KeyboardType
import io.proteus.core.exceptions.IllegalConfigDataTypeException
import io.proteus.core.provider.ConfigValue
import io.proteus.ui.domain.entity.FeatureNote
import io.proteus.ui.presentation.configurator.FeatureConfiguratorState.MockInputType
import io.proteus.ui.presentation.configurator.FeatureConfiguratorState.MockInputType.TextInput
import io.proteus.ui.presentation.configurator.FeatureConfiguratorState.MockInputType.Toggle
import io.proteus.ui.presentation.configurator.FeatureConfiguratorState.TextType

@Throws(IllegalConfigDataTypeException::class)
internal fun MockInputType.asConfigValue(): ConfigValue<*> {
    return when (this) {
        is Toggle -> {
            ConfigValue.Boolean(isActivated)
        }

        is TextInput -> {
            when (textInputType) {
                TextType.INTEGER -> {
                    val mockedInteger = text.toLongOrNull() ?: throw IllegalConfigDataTypeException("Invalid integer value")
                    ConfigValue.Long(mockedInteger)
                }

                TextType.DECIMAL -> {
                    val mockedDouble = text.toDoubleOrNull() ?: throw IllegalConfigDataTypeException("Invalid double value")
                    ConfigValue.Double(mockedDouble)
                }

                TextType.RAW -> {
                    ConfigValue.Text(text)
                }
            }
        }
    }
}

@Throws(IllegalConfigDataTypeException::class)
internal fun FeatureNote<*>.asInputType(): TextType {
    return when (this.feature.valueClass) {
        Long::class -> TextType.INTEGER
        String::class -> TextType.RAW
        Double::class -> TextType.DECIMAL
        else -> throw IllegalConfigDataTypeException("Unsupported data type: ${this.feature.valueClass}")
    }
}

internal fun TextType.asKeyboardType(): KeyboardType {
    return when (this) {
        TextType.INTEGER -> KeyboardType.NumberPassword
        TextType.DECIMAL -> KeyboardType.Decimal
        TextType.RAW -> KeyboardType.Text
    }
}
