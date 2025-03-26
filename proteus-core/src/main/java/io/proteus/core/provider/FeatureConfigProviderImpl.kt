package io.proteus.core.provider

import io.proteus.core.domain.FeatureContext
import io.proteus.core.exceptions.MockConfigUnavailableException

class FeatureConfigProviderImpl(
    private val mockConfigProvider: FeatureConfigProvider,
    private val providerFactory: FeatureConfigProviderFactory
) : FeatureConfigProvider {

    override fun getBoolean(feature: FeatureContext<Boolean>): Boolean {
        return tryOverriddenConfig(
            { mockConfigProvider.getBoolean(feature) },
            { providerFactory.getProvider(feature.owner).getBoolean(feature) }
        )
    }

    override fun getString(feature: FeatureContext<String>): String {
        return tryOverriddenConfig(
            { mockConfigProvider.getString(feature) },
            { providerFactory.getProvider(feature.owner).getString(feature) }
        )
    }

    override fun getLong(feature: FeatureContext<Long>): Long {
        return tryOverriddenConfig(
            { mockConfigProvider.getLong(feature) },
            { providerFactory.getProvider(feature.owner).getLong(feature) }
        )
    }

    override fun getDouble(feature: FeatureContext<Double>): Double {
        return tryOverriddenConfig(
            { mockConfigProvider.getDouble(feature) },
            { providerFactory.getProvider(feature.owner).getDouble(feature) }
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
