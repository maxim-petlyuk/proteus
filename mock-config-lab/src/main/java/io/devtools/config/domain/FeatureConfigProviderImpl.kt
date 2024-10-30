package io.devtools.config.domain

class FeatureConfigProviderImpl(
    private val providerFactory: FeatureConfigProviderFactory
) : FeatureConfigProvider {

    override fun isFeatureAvailable(feature: Feature): Boolean {
        return providerFactory.getProvider(feature.owner).isFeatureAvailable(feature)
    }

    override fun getString(feature: Feature): String {
        return providerFactory.getProvider(feature.owner).getString(feature)
    }

    override fun getLong(feature: Feature): Long {
        return providerFactory.getProvider(feature.owner).getLong(feature)
    }

    override fun getDouble(feature: Feature): Double {
        return providerFactory.getProvider(feature.owner).getDouble(feature)
    }
}