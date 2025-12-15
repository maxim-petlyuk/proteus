package io.proteus.ui.presentation.catalog

import androidx.compose.runtime.Immutable
import io.proteus.ui.domain.entity.FeatureNote

@Immutable
internal data class FeatureCatalogState(
    val searchQuery: String = "",
    val originalFeatureBook: List<FeatureNote<*>> = emptyList(),
    val filteredFeatureBook: List<FeatureNote<*>> = emptyList(),
    val highlightRanges: Map<String, List<IntRange>> = emptyMap(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
) {

    val featureBook: List<FeatureNote<*>>
        get() = if (searchQuery.isEmpty()) originalFeatureBook else filteredFeatureBook

    val shouldHighlight: Boolean
        get() = searchQuery.length > 2

    fun getHighlightRanges(featureKey: String): List<IntRange> =
        highlightRanges[featureKey] ?: emptyList()

    val uiState: UiState
        get() = when {
            isLoading -> UiState.Loading
            errorMessage != null -> UiState.Error
            else -> UiState.Loaded
        }

    companion object {

        fun idle(): FeatureCatalogState {
            return FeatureCatalogState()
        }
    }
}
