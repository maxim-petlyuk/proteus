package io.proteus.ui.presentation.configurator

internal sealed class FeatureConfiguratorAction {

    data class ToggleMockConfiguration(val isActivated: Boolean) : FeatureConfiguratorAction()

    data class ChangeBooleanFeatureValue(val isActivated: Boolean) : FeatureConfiguratorAction()

    data class ChangeTextFeatureValue(val mockedValue: String) : FeatureConfiguratorAction()

    data object SaveChanges : FeatureConfiguratorAction()

    data object ConsumeFailureMessage : FeatureConfiguratorAction()
}
