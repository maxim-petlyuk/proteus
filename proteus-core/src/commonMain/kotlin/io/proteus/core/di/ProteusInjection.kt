package io.proteus.core.di

import io.proteus.core.data.DataStoreMockConfigStorage
import io.proteus.core.data.MockConfigRepository
import io.proteus.core.data.MockConfigRepositoryImpl
import io.proteus.core.data.MockConfigStorage
import io.proteus.core.platform.DataStoreFactory
import io.proteus.core.platform.PlatformContext
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory
import io.proteus.core.provider.FeatureConfigProviderImpl
import io.proteus.core.provider.MockConfigProvider

internal object ProteusInjection {

    fun provideMockConfigStorage(context: PlatformContext): MockConfigStorage {
        val dataStore = DataStoreFactory.createDataStore("proteus_config", context)
        return DataStoreMockConfigStorage(dataStore)
    }

    fun provideMockConfigRepository(mockConfigStorage: MockConfigStorage): MockConfigRepository {
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