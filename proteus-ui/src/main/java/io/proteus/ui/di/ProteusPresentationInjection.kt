package io.proteus.ui.di

import io.proteus.core.data.FeatureBookDataSource
import io.proteus.core.data.MockConfigRepository
import io.proteus.core.provider.FeatureConfigProviderFactory
import io.proteus.ui.data.FeatureBookRepository

internal object ProteusPresentationInjection {

    fun provideFeatureBookRepository(
        featureBookDataSource: FeatureBookDataSource,
        remoteConfigProviderFactory: FeatureConfigProviderFactory,
        mockConfigRepository: MockConfigRepository
    ): FeatureBookRepository {
        return FeatureBookRepository(
            featureBookDataSource = featureBookDataSource,
            remoteConfigProviderFactory = remoteConfigProviderFactory,
            mockConfigRepository = mockConfigRepository
        )
    }
}
