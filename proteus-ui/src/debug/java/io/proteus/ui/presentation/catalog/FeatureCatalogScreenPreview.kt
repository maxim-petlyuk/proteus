package io.proteus.ui.presentation.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import io.proteus.ui.presentation.theme.ProteusTheme

@Preview
@Composable
private fun LoadingStatePreview(
    @PreviewParameter(LoadingStatePreviewProvider::class)
    state: FeatureCatalogState
) {
    ProteusTheme {
        FeatureCatalogScreen(
            state = state
        )
    }
}

@Preview
@Composable
private fun LoadedStatePreview(
    @PreviewParameter(LoadedStatePreviewProvider::class)
    state: FeatureCatalogState
) {
    ProteusTheme {
        FeatureCatalogScreen(
            state = state
        )
    }
}