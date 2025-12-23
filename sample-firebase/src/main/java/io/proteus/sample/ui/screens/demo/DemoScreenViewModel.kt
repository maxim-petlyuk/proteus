package io.proteus.sample.ui.screens.demo

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.proteus.core.data.MockConfigRepository
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory
import io.proteus.sample.data.DemoUiState
import io.proteus.sample.data.FeatureFlagState
import io.proteus.sample.data.FeatureSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.milliseconds

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

    val featureFlagState = MutableStateFlow<DemoUiState>(DemoUiState.Loading)

    fun retry() {
        loadFeatureFlagState()
    }

    fun refreshFeatureFlagState() {
        loadFeatureFlagState()
    }

    private fun loadFeatureFlagState() {
        viewModelScope.launch {
            try {
                val silently = featureFlagState.value is DemoUiState.Success

                if (!silently) {
                    featureFlagState.value = DemoUiState.Loading
                    delay(2000.milliseconds)
                }

                val featureFlag = createFeatureFlagState()

                if (featureFlag != null) {
                    featureFlagState.value = DemoUiState.Success(featureFlag)
                } else {
                    featureFlagState.value = DemoUiState.Empty
                }
            } catch (e: Exception) {
                featureFlagState.value = DemoUiState.Error(
                    message = "Failed to load feature configuration: ${e.localizedMessage ?: "Unknown error"}"
                )
            }
        }
    }

    private fun createFeatureFlagState(): FeatureFlagState? {
        return try {
            val key = "ai_assistant_mode"
            val typeClass = String::class
            val value = featureConfigProvider.getString(key)

            val source = if (hasLocalOverride(key, typeClass)) {
                FeatureSource.MOCK
            } else {
                FeatureSource.REMOTE
            }

            FeatureFlagState(
                name = "AI Assistant Mode",
                key = key,
                value = value,
                type = typeClass.simpleName ?: "",
                owner = remoteConfigProviderFactory.getProviderTag(key).capitalize(Locale.current),
                source = source,
                description = "Controls the AI assistant operational mode for enhanced user interactions and intelligent responses."
            )
        } catch (e: Exception) {
            // Log the error in a real app
            null
        }
    }

    private fun hasLocalOverride(key: String, typeClass: KClass<*>): Boolean {
        return mockConfigRepository.getMockedConfigValue(key, typeClass) != null
    }
}