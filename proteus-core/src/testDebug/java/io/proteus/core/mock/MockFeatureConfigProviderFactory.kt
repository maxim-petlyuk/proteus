package io.proteus.core.mock

import io.proteus.core.exceptions.IllegalConfigOwnerException
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory

/**
 * @param serviceMapping - map where key is string representation of feature key, value is responsible [MockFeatureConfigOwner].
 */
internal class MockFeatureConfigProviderFactory(
    private val firebaseConfigProvider: FeatureConfigProvider,
    private val clevertapConfigProvider: FeatureConfigProvider,
    private val serviceMapping: Map<String, MockFeatureConfigOwner>
) : FeatureConfigProviderFactory {

    @Throws(IllegalConfigOwnerException::class)
    override fun getProvider(featureKey: String): FeatureConfigProvider {
        return when (getServiceOwner(featureKey)) {
            MockFeatureConfigOwner.Firebase -> firebaseConfigProvider
            MockFeatureConfigOwner.CleverTap -> clevertapConfigProvider
        }
    }

    override fun getProviderTag(featureKey: String): String {
        return getServiceOwner(featureKey).serviceOwner
    }

    private fun getServiceOwner(featureKey: String): MockFeatureConfigOwner {
        return serviceMapping[featureKey]
            ?: throw IllegalConfigOwnerException("There is no registered service owner for feature key: $featureKey")
    }
}
