package io.proteus.ui.presentation.configurator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.proteus.core.provider.Proteus
import io.proteus.ui.data.FeatureBookRepository
import io.proteus.ui.di.ProteusPresentationInjection
import kotlinx.coroutines.flow.MutableStateFlow

internal class FeatureConfiguratorViewModel(
    private val featureKey: String,
    private val featureBookRepository: FeatureBookRepository
) : ViewModel() {

    val screenState = MutableStateFlow(FeatureConfiguratorState.idle())

    class Factory(val featureKey: String) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FeatureConfiguratorViewModel(
                featureKey = featureKey,
                featureBookRepository = ProteusPresentationInjection.provideFeatureBookRepository(
                    featureBookDataSource = Proteus.getInstance().getFeatureBookDataSource(),
                    remoteConfigProviderFactory = Proteus.getInstance().getRemoteConfigProviderFactory(),
                    mockConfigRepository = Proteus.getInstance().getMockConfigRepository()
                ),
            ) as T
        }
    }
}
