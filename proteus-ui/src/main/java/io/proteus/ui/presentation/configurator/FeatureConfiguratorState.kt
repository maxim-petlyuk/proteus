package io.proteus.ui.presentation.configurator

internal data class FeatureConfiguratorState(
    val isLoading: Boolean = false,
) {

    companion object {

        fun idle(): FeatureConfiguratorState {
            return FeatureConfiguratorState()
        }
    }
}