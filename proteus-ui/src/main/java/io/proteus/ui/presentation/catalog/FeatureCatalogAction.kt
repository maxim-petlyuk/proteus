package io.proteus.ui.presentation.catalog

internal sealed class FeatureCatalogAction {

    data class QueryChanged(val query: String) : FeatureCatalogAction()

    data object ResetSearch : FeatureCatalogAction()
}
