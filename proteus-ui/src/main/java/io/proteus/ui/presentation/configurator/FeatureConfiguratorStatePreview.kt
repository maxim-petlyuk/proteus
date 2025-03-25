package io.proteus.ui.presentation.configurator

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.proteus.core.provider.ConfigValue
import io.proteus.core.provider.Feature
import io.proteus.core.provider.FeatureConfigOwner
import io.proteus.ui.domain.entity.FeatureNote

internal class ConfiguratorLoadingStatePreview : PreviewParameterProvider<FeatureConfiguratorState> {

    override val values: Sequence<FeatureConfiguratorState> = sequenceOf(
        FeatureConfiguratorState(isLoading = true)
    )
}

internal class TextFeatureConfiguratorStatePreview : PreviewParameterProvider<FeatureConfiguratorState> {

    override val values: Sequence<FeatureConfiguratorState> = sequenceOf(
        FeatureConfiguratorState(
            isLoading = false,
            featureNote = FeatureNote<String>(
                feature = Feature<String>(
                    key = "optional_domain",
                    defaultValue = "http://example.com",
                    valueClass = String::class,
                    owner = object : FeatureConfigOwner {}
                ),
                remoteConfigValue = "http://google.com",
                localConfigValue = ConfigValue.Text("http://override.com")
            ),
            mockInputType = FeatureConfiguratorState.MockInputType.TextInput("http://override.com", FeatureConfiguratorState.TextType.RAW)
        )
    )
}

internal class DecimalFeatureConfiguratorStatePreview : PreviewParameterProvider<FeatureConfiguratorState> {

    override val values: Sequence<FeatureConfiguratorState> = sequenceOf(
        FeatureConfiguratorState(
            isLoading = false,
            featureNote = FeatureNote<Double>(
                feature = Feature<Double>(
                    key = "optional_domain",
                    defaultValue = 0.2,
                    valueClass = Double::class,
                    owner = object : FeatureConfigOwner {}
                ),
                remoteConfigValue = "http://google.com",
                localConfigValue = ConfigValue.Double(0.9)
            ),
            mockInputType = FeatureConfiguratorState.MockInputType.TextInput("0.9", FeatureConfiguratorState.TextType.DECIMAL)
        )
    )
}

internal class LongFeatureConfiguratorStatePreview : PreviewParameterProvider<FeatureConfiguratorState> {

    override val values: Sequence<FeatureConfiguratorState> = sequenceOf(
        FeatureConfiguratorState(
            isLoading = false,
            featureNote = FeatureNote<Long>(
                feature = Feature<Long>(
                    key = "optional_domain",
                    defaultValue = 50,
                    valueClass = Long::class,
                    owner = object : FeatureConfigOwner {}
                ),
                remoteConfigValue = "100",
                localConfigValue = ConfigValue.Long(200)
            ),
            mockInputType = FeatureConfiguratorState.MockInputType.TextInput("12", FeatureConfiguratorState.TextType.INTEGER)
        )
    )
}

internal class BooleanFeatureConfiguratorStatePreview : PreviewParameterProvider<FeatureConfiguratorState> {

    override val values: Sequence<FeatureConfiguratorState> = sequenceOf(
        FeatureConfiguratorState(
            isLoading = false,
            featureNote = FeatureNote<Boolean>(
                feature = Feature<Boolean>(
                    key = "optional_domain",
                    defaultValue = false,
                    valueClass = Boolean::class,
                    owner = object : FeatureConfigOwner {}
                ),
                remoteConfigValue = "true"
            ),
            mockInputType = FeatureConfiguratorState.MockInputType.Toggle(false)
        )
    )
}
