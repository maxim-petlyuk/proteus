package io.proteus.sample.ui.screens.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.proteus.core.data.MockConfigRepository
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory

/**
 * Factory for creating DemoScreenViewModel with FeatureConfigProvider dependency.
 */
class DemoScreenViewModelFactory(
    private val featureConfigProvider: FeatureConfigProvider,
    private val remoteConfigProviderFactory: FeatureConfigProviderFactory,
    private val mockConfigRepository: MockConfigRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DemoScreenViewModel::class.java)) {
            return DemoScreenViewModel(featureConfigProvider, remoteConfigProviderFactory, mockConfigRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}