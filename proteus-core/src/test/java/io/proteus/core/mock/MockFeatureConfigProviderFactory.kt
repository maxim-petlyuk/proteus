package io.proteus.core.mock

import io.proteus.core.exceptions.IllegalConfigOwnerException
import io.proteus.core.provider.FeatureConfigOwner
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory

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
