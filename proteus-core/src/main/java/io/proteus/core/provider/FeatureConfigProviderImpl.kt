package io.proteus.core.provider

import io.proteus.core.exceptions.MockConfigUnavailableException

class FeatureConfigProviderImpl(
    private val mockConfigProvider: FeatureConfigProvider,
    private val providerFactory: FeatureConfigProviderFactory
) : FeatureConfigProvider {

    override fun getBoolean(featureKey: String): Boolean {
        return tryOverriddenConfig(
            { mockConfigProvider.getBoolean(featureKey) },
            { providerFactory.getProvider(featureKey).getBoolean(featureKey) }
        )
    }

    override fun getString(featureKey: String): String {
        return tryOverriddenConfig(
            { mockConfigProvider.getString(featureKey) },
            { providerFactory.getProvider(featureKey).getString(featureKey) }
        )
    }

    override fun getLong(featureKey: String): Long {
        return tryOverriddenConfig(
            { mockConfigProvider.getLong(featureKey) },
            { providerFactory.getProvider(featureKey).getLong(featureKey) }
        )
    }

    override fun getDouble(featureKey: String): Double {
        return tryOverriddenConfig(
            { mockConfigProvider.getDouble(featureKey) },
            { providerFactory.getProvider(featureKey).getDouble(featureKey) }
        )
    }

    private fun <T> tryOverriddenConfig(mockRequest: () -> T, originalRequest: () -> T): T {
        return try {
            mockRequest.invoke()
        } catch (_: MockConfigUnavailableException) {
            originalRequest.invoke()
        }
    }
}
