package io.proteus.core.provider

import io.proteus.core.exceptions.MockConfigUnavailableException

class FeatureConfigProviderImpl(
    private val mockConfigProvider: FeatureConfigProvider,
    private val providerFactory: FeatureConfigProviderFactory
) : FeatureConfigProvider {

    override suspend fun getBoolean(featureKey: String): Boolean {
        return tryOverriddenConfig(
            { mockConfigProvider.getBoolean(featureKey) },
            { providerFactory.getProvider(featureKey).getBoolean(featureKey) }
        )
    }

    override suspend fun getString(featureKey: String): String {
        return tryOverriddenConfig(
            { mockConfigProvider.getString(featureKey) },
            { providerFactory.getProvider(featureKey).getString(featureKey) }
        )
    }

    override suspend fun getLong(featureKey: String): Long {
        return tryOverriddenConfig(
            { mockConfigProvider.getLong(featureKey) },
            { providerFactory.getProvider(featureKey).getLong(featureKey) }
        )
    }

    override suspend fun getDouble(featureKey: String): Double {
        return tryOverriddenConfig(
            { mockConfigProvider.getDouble(featureKey) },
            { providerFactory.getProvider(featureKey).getDouble(featureKey) }
        )
    }

    private suspend fun <T> tryOverriddenConfig(mockRequest: suspend () -> T, originalRequest: suspend () -> T): T {
        return try {
            mockRequest()
        } catch (_: MockConfigUnavailableException) {
            originalRequest()
        }
    }
}
