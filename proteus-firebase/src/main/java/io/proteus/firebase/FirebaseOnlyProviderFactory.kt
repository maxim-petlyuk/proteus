package io.proteus.firebase

import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory

class FirebaseOnlyProviderFactory : FeatureConfigProviderFactory {

    private val firebaseFeatureConfigProvider = FirebaseFeatureConfigProvider()

    override fun getProvider(featureKey: String): FeatureConfigProvider {
        return firebaseFeatureConfigProvider
    }

    override fun getProviderTag(featureKey: String): String {
        return "firebase"
    }
}
