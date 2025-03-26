package io.proteus.ui.presentation.configurator

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import io.proteus.ui.presentation.theme.ProteusTheme

@Preview
@Composable
private fun LoadingPreview(
    @PreviewParameter(ConfiguratorLoadingStatePreview::class)
    state: FeatureConfiguratorState
) {
    ProteusTheme {
        FeatureConfiguratorScreen(state = state)
    }
}

@Preview
@Composable
private fun TextFeaturePreview(
    @PreviewParameter(TextFeatureConfiguratorStatePreview::class)
    state: FeatureConfiguratorState
) {
    ProteusTheme {
        FeatureConfiguratorScreen(state = state)
    }
}

@Preview
@Composable
private fun DoubleFeaturePreview(
    @PreviewParameter(DecimalFeatureConfiguratorStatePreview::class)
    state: FeatureConfiguratorState
) {
    ProteusTheme {
        FeatureConfiguratorScreen(state = state)
    }
}

@Preview
@Composable
private fun LongFeaturePreview(
    @PreviewParameter(LongFeatureConfiguratorStatePreview::class)
    state: FeatureConfiguratorState
) {
    ProteusTheme {
        FeatureConfiguratorScreen(state = state)
    }
}

@Preview
@Composable
private fun BooleanFeaturePreview(
    @PreviewParameter(BooleanFeatureConfiguratorStatePreview::class)
    state: FeatureConfiguratorState
) {
    ProteusTheme {
        FeatureConfiguratorScreen(state = state)
    }
}
