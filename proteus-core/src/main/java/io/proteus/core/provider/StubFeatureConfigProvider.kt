package io.proteus.core.provider

import io.proteus.core.exceptions.MockConfigUnavailableException

data object StubFeatureConfigProvider : FeatureConfigProvider {

    override fun getBoolean(featureKey: String): Boolean {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override fun getString(featureKey: String): String {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override fun getLong(featureKey: String): Long {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override fun getDouble(featureKey: String): Double {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }
}