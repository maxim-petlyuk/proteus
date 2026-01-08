package io.proteus.core.provider

import io.proteus.core.exceptions.MockConfigUnavailableException

data object StubFeatureConfigProvider : FeatureConfigProvider {

    override suspend fun getBoolean(featureKey: String): Boolean {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override suspend fun getString(featureKey: String): String {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override suspend fun getLong(featureKey: String): Long {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override suspend fun getDouble(featureKey: String): Double {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }
}