package io.proteus.ui.presentation.configurator

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class ConfiguratorLoadingStatePreview : PreviewParameterProvider<FeatureConfiguratorState> {

    override val values: Sequence<FeatureConfiguratorState> = sequenceOf(
        FeatureConfiguratorState(isLoading = true)
    )
}

internal class ConfiguratorLoadedStatePreview : PreviewParameterProvider<FeatureConfiguratorState> {

    override val values: Sequence<FeatureConfiguratorState> = sequenceOf(
        FeatureConfiguratorState(isLoading = false)
    )
}
