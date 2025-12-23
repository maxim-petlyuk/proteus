package io.proteus.sample.data

import androidx.compose.runtime.Immutable

data class FeatureFlagState(
    val name: String,
    val key: String,
    val value: String,
    val type: String,
    val owner: String,
    val source: FeatureSource,
    val description: String
)

enum class FeatureSource {
    REMOTE,
    MOCK
}

/**
 * UI state wrapper for feature flag data with loading and error states
 */
@Immutable
sealed class DemoUiState {

    @Immutable
    data object Loading : DemoUiState()

    @Immutable
    data class Success(
        val featureFlag: FeatureFlagState
    ) : DemoUiState()

    @Immutable
    data class Error(
        val message: String
    ) : DemoUiState()

    @Immutable
    data object Empty : DemoUiState()
}