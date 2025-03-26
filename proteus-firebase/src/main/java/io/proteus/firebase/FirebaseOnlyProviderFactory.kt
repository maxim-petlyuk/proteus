package io.proteus.firebase

import io.proteus.core.provider.FeatureConfigOwner
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory

class FirebaseOnlyProviderFactory : FeatureConfigProviderFactory {

    override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
        return FirebaseFeatureConfigProvider()
    }
}
