package io.proteus.ui.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.proteus.ui.data.FeatureBookRepository
import io.proteus.ui.di.ProteusPresentationInjection
import kotlinx.coroutines.flow.MutableStateFlow

internal class FeatureCatalogViewModel(
    private val featureBookRepository: FeatureBookRepository
) : ViewModel() {

    val screenState = MutableStateFlow(FeatureCatalogState.idle())

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FeatureCatalogViewModel(
                    featureBookRepository = ProteusPresentationInjection.featureBookRepository,
                ) as T
            }
        }
    }
}
