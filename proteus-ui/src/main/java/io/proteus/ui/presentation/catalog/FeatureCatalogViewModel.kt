package io.proteus.ui.presentation.catalog

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

internal class FeatureCatalogViewModel : ViewModel() {

    val screenState = MutableStateFlow(FeatureCatalogState.idle())
}
