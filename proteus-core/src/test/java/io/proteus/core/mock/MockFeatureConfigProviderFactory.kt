package io.proteus.core.mock

import io.proteus.core.exceptions.IllegalConfigOwnerException
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory

internal class MockFeatureConfigProviderFactory(
    private val firebaseConfigProvider: FeatureConfigProvider,
    private val clevertapConfigProvider: FeatureConfigProvider
) : FeatureConfigProviderFactory {

    @Throws(IllegalConfigOwnerException::class)
    override fun getProvider(owner: String): FeatureConfigProvider {
        return when (MockFeatureConfigOwner.fromServiceOwner(owner)) {
            MockFeatureConfigOwner.Firebase -> firebaseConfigProvider
            MockFeatureConfigOwner.CleverTap -> clevertapConfigProvider
            else -> throw IllegalConfigOwnerException("Unknown owner: $owner")
        }
    }
}
