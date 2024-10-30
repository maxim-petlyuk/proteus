package io.devtools.config.provider

import io.devtools.config.exceptions.MockConfigUnavailableException

class FeatureConfigProviderImpl(
    private val mockConfigProvider: FeatureConfigProvider,
    private val providerFactory: FeatureConfigProviderFactory
) : FeatureConfigProvider {

    override fun getBoolean(feature: Feature<Boolean>): Boolean {
        return tryOverriddenConfig(
            { mockConfigProvider.getBoolean(feature) },
            { providerFactory.getProvider(feature.owner).getBoolean(feature) }
        )
    }

    override fun getString(feature: Feature<String>): String {
        return tryOverriddenConfig(
            { mockConfigProvider.getString(feature) },
            { providerFactory.getProvider(feature.owner).getString(feature) }
        )
    }

    override fun getLong(feature: Feature<Long>): Long {
        return tryOverriddenConfig(
            { mockConfigProvider.getLong(feature) },
            { providerFactory.getProvider(feature.owner).getLong(feature) }
        )
    }

    override fun getDouble(feature: Feature<Double>): Double {
        return tryOverriddenConfig(
            { mockConfigProvider.getDouble(feature) },
            { providerFactory.getProvider(feature.owner).getDouble(feature) }
        )
    }

    private fun <T> tryOverriddenConfig(mockRequest: () -> T, originalRequest: () -> T): T {
        return try {
            mockRequest.invoke()
        } catch (error: MockConfigUnavailableException) {
            originalRequest.invoke()
        }
    }
}
