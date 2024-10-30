package io.devtools.config.domain

class FeatureConfigProviderImpl(
    private val providerFactory: FeatureConfigProviderFactory
) : FeatureConfigProvider {

    override fun getBoolean(feature: Feature<Boolean>): Boolean {
        return providerFactory.getProvider(feature.owner).getBoolean(feature)
    }

    override fun getString(feature: Feature<String>): String {
        return providerFactory.getProvider(feature.owner).getString(feature)
    }

    override fun getLong(feature: Feature<Long>): Long {
        return providerFactory.getProvider(feature.owner).getLong(feature)
    }

    override fun getDouble(feature: Feature<Double>): Double {
        return providerFactory.getProvider(feature.owner).getDouble(feature)
    }
}