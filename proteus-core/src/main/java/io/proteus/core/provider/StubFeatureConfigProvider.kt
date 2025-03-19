package io.proteus.core.provider

import io.proteus.core.exceptions.MockConfigUnavailableException

data object StubFeatureConfigProvider : FeatureConfigProvider {

    override fun getBoolean(feature: FeatureContext<Boolean>): Boolean {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override fun getString(feature: FeatureContext<String>): String {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override fun getLong(feature: FeatureContext<Long>): Long {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override fun getDouble(feature: FeatureContext<Double>): Double {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }
}