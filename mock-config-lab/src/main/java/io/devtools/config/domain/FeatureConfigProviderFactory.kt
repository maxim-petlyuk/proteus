package io.devtools.config.domain

interface FeatureConfigProviderFactory {

    fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider
}
