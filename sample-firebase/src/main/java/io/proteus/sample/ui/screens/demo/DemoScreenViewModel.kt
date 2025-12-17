package io.proteus.sample.ui.screens.demo

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory
import io.proteus.sample.data.FeatureFlagState
import io.proteus.sample.data.FeatureSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the DemoScreen that handles all business logic related to feature flag state.
 *
 * Responsibilities:
 * - Load feature flag values from FeatureConfigProvider
 * - Determine if values are from remote or local override (MOCK)
 * - Provide reactive state updates for the UI
 * - Handle lifecycle-aware data refresh
 */
class DemoScreenViewModel(
    private val featureConfigProvider: FeatureConfigProvider,
    private val remoteConfigProviderFactory: FeatureConfigProviderFactory,
) : ViewModel() {

    private val _featureFlagState = MutableStateFlow(getPreviewFeatureFlagState())
    val featureFlagState: StateFlow<FeatureFlagState> = _featureFlagState.asStateFlow()

    init {
        loadFeatureFlagState()
    }

    /**
     * Loads the current feature flag state from the provider.
     * This should be called when the screen becomes visible or when returning from configurator.
     */
    fun loadFeatureFlagState() {
        viewModelScope.launch {
            _featureFlagState.value = createFeatureFlagState()
        }
    }

    /**
     * Refreshes the feature flag state.
     * Called when returning from the configurator to pick up any changes.
     */
    fun refreshFeatureFlagState() {
        loadFeatureFlagState()
    }

    /**
     * Creates a FeatureFlagState from the current FeatureConfigProvider values.
     */
    private fun createFeatureFlagState(): FeatureFlagState {
        val key = "optional_server"
        val value = featureConfigProvider.getString(key)

        // Determine if this is a local override (MOCK) or remote value (REMOTE)
        val source = if (hasLocalOverride(key)) {
            FeatureSource.MOCK
        } else {
            FeatureSource.REMOTE
        }

        return FeatureFlagState(
            name = "AI Assistant Mode",
            key = key,
            value = value,
            type = "String",
            owner = remoteConfigProviderFactory.getProviderTag(key).capitalize(Locale.current),
            source = source,
            description = "Controls the AI assistant operational mode for enhanced user interactions and intelligent responses."
        )
    }

    /**
     * Determines if a feature has a local override.
     * This is a simplified implementation that assumes no override checking capability.
     * In a real scenario, the FeatureConfigProvider might have a method to check overrides.
     */
    private fun hasLocalOverride(key: String): Boolean {
        // Note: This is a simplified implementation
        // For now, we'll assume REMOTE unless we can detect otherwise
        // In the future, this could be enhanced to actually detect local overrides
        return false
    }

    companion object {
        /**
         * Creates a preview FeatureFlagState for use in Compose previews
         */
        fun getPreviewFeatureFlagState() = FeatureFlagState(
            name = "AI Assistant Mode",
            key = "optional_server",
            value = "ADVANCED",
            type = "String",
            owner = "Firebase",
            source = FeatureSource.REMOTE,
            description = "Controls the AI assistant operational mode for enhanced user interactions and intelligent responses."
        )
    }
}