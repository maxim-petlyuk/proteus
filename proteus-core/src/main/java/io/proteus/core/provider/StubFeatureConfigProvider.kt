package io.proteus.core.provider

import io.proteus.core.exceptions.MockConfigUnavailableException

data object StubFeatureConfigProvider : FeatureConfigProvider {

    override fun getBoolean(feature: Feature<Boolean>): Boolean {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override fun getString(feature: Feature<String>): String {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override fun getLong(feature: Feature<Long>): Long {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }

    override fun getDouble(feature: Feature<Double>): Double {
        throw MockConfigUnavailableException("Mock config is not available under StubFeatureConfigProvider")
    }
}