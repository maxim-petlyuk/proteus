package io.proteus.ui.presentation.catalog

import io.proteus.ui.domain.entity.FeatureNote

internal data class FeatureCatalogState(
    val searchQuery: String = "",
    val featureBook: List<FeatureNote<*>> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {

    val uiState: UiState
        get() = when {
            isLoading -> UiState.Loading
            errorMessage != null -> UiState.Error
            else -> UiState.Loaded
        }

    enum class UiState {
        Loading,
        Error,
        Loaded
    }

    companion object {

        fun idle(): FeatureCatalogState {
            return FeatureCatalogState()
        }
    }
}
