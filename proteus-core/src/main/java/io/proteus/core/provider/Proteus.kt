package io.proteus.core.provider

import android.content.Context
import io.proteus.core.data.FeatureBookDataSource
import io.proteus.core.data.MockConfigRepository
import io.proteus.core.data.MockConfigStorage
import io.proteus.core.di.ProteusInjection

class Proteus private constructor(
    private val featureBookDataSource: FeatureBookDataSource,
    private val remoteConfigProviderFactory: FeatureConfigProviderFactory,
    private val mockConfigRepository: MockConfigRepository
) {

    fun buildConfigProvider(): FeatureConfigProvider {
        return FeatureConfigProviderImpl(
            mockConfigProvider = ProteusInjection.getMockConfigProvider(mockConfigRepository),
            providerFactory = remoteConfigProviderFactory
        )
    }

    fun getFeatureBookDataSource(): FeatureBookDataSource {
        return featureBookDataSource
    }

    fun getMockConfigRepository(): MockConfigRepository {
        return mockConfigRepository
    }

    fun getRemoteConfigProviderFactory(): FeatureConfigProviderFactory {
        return remoteConfigProviderFactory
    }

    class Builder(context: Context) {

        private var storage: MockConfigStorage = ProteusInjection.provideMockConfigStorage(context)
        private var configProviderFactory: FeatureConfigProviderFactory? = null
        private var featureBookDataSource: FeatureBookDataSource? = null

        fun setMockConfigStorage(storage: MockConfigStorage): Builder {
            this.storage = storage
            return this
        }

        fun registerConfigProviderFactory(factory: FeatureConfigProviderFactory): Builder {
            this.configProviderFactory = factory
            return this
        }

        fun registerFeatureBookDataSource(dataSource: FeatureBookDataSource): Builder {
            this.featureBookDataSource = dataSource
            return this
        }

        fun build(): Proteus {
            if (instance != null) {
                throw IllegalStateException("Proteus has been already initialized")
            }

            val configProviderFactory = configProviderFactory
                ?: throw IllegalStateException("You must register FeatureConfigProviderFactory before building FeatureConfigProvider")

            val featureBookDataSource = featureBookDataSource
                ?: throw IllegalStateException("You must provide source from where all feature configurations will be fetched")

            instance = Proteus(
                featureBookDataSource = featureBookDataSource,
                remoteConfigProviderFactory = configProviderFactory,
                mockConfigRepository = ProteusInjection.provideMockConfigRepository(storage)
            )

            return instance!!
        }
    }

    companion object {

        @Volatile
        private var instance: Proteus? = null
        private var mutex = Object()

        fun getInstance(): Proteus {
            return synchronized(mutex) {
                instance ?: throw IllegalStateException("Proteus is not initialized. Call Builder().build() first.")
            }
        }
    }
}
