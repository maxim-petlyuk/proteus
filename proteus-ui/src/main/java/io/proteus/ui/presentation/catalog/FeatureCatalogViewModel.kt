package io.proteus.ui.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.proteus.core.provider.Proteus
import io.proteus.ui.data.FeatureBookRepository
import io.proteus.ui.di.ProteusPresentationInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class FeatureCatalogViewModel(
    private val featureBookRepository: FeatureBookRepository
) : ViewModel() {

    val screenState = MutableStateFlow(FeatureCatalogState.idle())

    init {
        loadState()
    }

    private fun loadState() {
        viewModelScope.launch(Dispatchers.IO) {
            rebuild {
                copy(
                    isLoading = true
                )
            }

            featureBookRepository.getFeatureBook()
                .onSuccess { featureBook ->
                    rebuild {
                        copy(
                            featureBook = featureBook,
                            isLoading = false
                        )
                    }
                }.onFailure {
                    rebuild {
                        copy(
                            isLoading = false
                        )
                    }
                }
        }
    }

    private suspend fun rebuild(stateBuilder: FeatureCatalogState.() -> FeatureCatalogState) {
        screenState.emit(stateBuilder.invoke(screenState.value))
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FeatureCatalogViewModel(
                    featureBookRepository = ProteusPresentationInjection.provideFeatureBookRepository(
                        featureBookDataSource = Proteus.getInstance().getFeatureBookDataSource(),
                        remoteConfigProviderFactory = Proteus.getInstance().getRemoteConfigProviderFactory(),
                        mockConfigRepository = Proteus.getInstance().getMockConfigRepository()
                    ),
                ) as T
            }
        }
    }
}
