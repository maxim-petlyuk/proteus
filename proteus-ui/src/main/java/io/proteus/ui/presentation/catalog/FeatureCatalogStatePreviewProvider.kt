package io.proteus.ui.presentation.catalog

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class LoadingStatePreviewProvider : PreviewParameterProvider<FeatureCatalogState> {

    override val values: Sequence<FeatureCatalogState> = sequenceOf(
        FeatureCatalogState(isLoading = true)
    )
}