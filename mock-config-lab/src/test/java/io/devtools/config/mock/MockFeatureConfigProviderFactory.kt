package io.devtools.config.mock

import io.devtools.config.domain.FeatureConfigOwner
import io.devtools.config.domain.FeatureConfigProvider
import io.devtools.config.domain.FeatureConfigProviderFactory
import io.devtools.config.exceptions.IllegalConfigOwnerException

internal class MockFeatureConfigProviderFactory(
    private val firebaseConfigProvider: FeatureConfigProvider,
    private val clevertapConfigProvider: FeatureConfigProvider
) : FeatureConfigProviderFactory {

    @Throws(IllegalConfigOwnerException::class)
    override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
        return when (owner) {
            is MockFeatureConfigOwner.Firebase -> firebaseConfigProvider
            is MockFeatureConfigOwner.CleverTap -> clevertapConfigProvider
            else -> throw IllegalConfigOwnerException("Unknown owner: $owner")
        }
    }
}
