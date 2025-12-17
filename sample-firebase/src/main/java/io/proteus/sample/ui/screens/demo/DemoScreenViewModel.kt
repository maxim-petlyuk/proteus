package io.proteus.sample.ui.screens.demo

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.proteus.core.data.MockConfigRepository
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory
import io.proteus.sample.data.FeatureFlagState
import io.proteus.sample.data.FeatureSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

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
    private val mockConfigRepository: MockConfigRepository,
) : ViewModel() {

    private val _featureFlagState = MutableStateFlow(getPreviewFeatureFlagState())
    val featureFlagState: StateFlow<FeatureFlagState> = _featureFlagState.asStateFlow()

    init {
        loadFeatureFlagState()
    }

    fun loadFeatureFlagState() {
        viewModelScope.launch {
            _featureFlagState.value = createFeatureFlagState()
        }
    }

    fun refreshFeatureFlagState() {
        loadFeatureFlagState()
    }

    private fun createFeatureFlagState(): FeatureFlagState {
        val key = "optional_server"
        val typeClass = String::class
        val value = featureConfigProvider.getString(key)

        val source = if (hasLocalOverride(key, typeClass)) {
            FeatureSource.MOCK
        } else {
            FeatureSource.REMOTE
        }

        return FeatureFlagState(
            name = "AI Assistant Mode",
            key = key,
            value = value,
            type = typeClass.simpleName ?: "",
            owner = remoteConfigProviderFactory.getProviderTag(key).capitalize(Locale.current),
            source = source,
            description = "Controls the AI assistant operational mode for enhanced user interactions and intelligent responses."
        )
    }

    private fun hasLocalOverride(key: String, typeClass: KClass<*>): Boolean {
        return mockConfigRepository.getMockedConfigValue(key, typeClass) != null
    }

    companion object {
        /**
         * Creates a preview FeatureFlagState for use in Compose previews
         */
        fun getPreviewFeatureFlagState() = FeatureFlagState(
            name = "",
            key = "",
            value = "",
            type = "",
            owner = "",
            source = FeatureSource.REMOTE,
            description = "typeClass"
        )
    }
}