package io.proteus.core.provider

import android.content.Context
import io.proteus.core.data.MockConfigRepository
import io.proteus.core.data.MockConfigStorage
import io.proteus.core.di.ProteusInjection

class Proteus {

    class Builder(context: Context) {

        private var storage: MockConfigStorage = ProteusInjection.provideMockConfigStorage(context)
        private var configProviderFactory: FeatureConfigProviderFactory? = null

        fun setMockConfigStorage(storage: MockConfigStorage): Builder {
            this.storage = storage
            return this
        }

        fun registerConfigProviderFactory(factory: FeatureConfigProviderFactory): Builder {
            this.configProviderFactory = factory
            return this
        }

        fun build(): FeatureConfigProvider {
            val mockConfigRepository: MockConfigRepository = ProteusInjection.providerMockConfigRepository(storage)

            val configProviderFactory = configProviderFactory
                ?: throw IllegalStateException("You must register FeatureConfigProviderFactory before building FeatureConfigProvider")

            return FeatureConfigProviderImpl(
                mockConfigProvider = MockConfigProvider(mockConfigRepository),
                providerFactory = configProviderFactory
            )
        }
    }
}
