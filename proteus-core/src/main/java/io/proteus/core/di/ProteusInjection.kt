package io.proteus.core.di

import android.content.Context
import io.proteus.core.data.MockConfigRepository
import io.proteus.core.data.MockConfigRepositoryImpl
import io.proteus.core.data.MockConfigStorage
import io.proteus.core.data.PreferenceMockConfigStorage
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory
import io.proteus.core.provider.FeatureConfigProviderImpl
import io.proteus.core.provider.MockConfigProvider

internal object ProteusInjection {

    fun provideMockConfigStorage(context: Context): MockConfigStorage {
        return PreferenceMockConfigStorage(context = context)
    }

    fun providerMockConfigRepository(mockConfigStorage: MockConfigStorage): MockConfigRepository {
        return MockConfigRepositoryImpl(mockConfigStorage)
    }

    fun getMockConfigProvider(mockConfigRepository: MockConfigRepository): FeatureConfigProvider {
        return MockConfigProvider(mockConfigRepository)
    }

    fun getFeatureConfigProvider(
        mockConfigProvider: FeatureConfigProvider,
        featureConfigProviderFactory: FeatureConfigProviderFactory
    ): FeatureConfigProvider {
        return FeatureConfigProviderImpl(
            mockConfigProvider = mockConfigProvider,
            providerFactory = featureConfigProviderFactory
        )
    }
}
