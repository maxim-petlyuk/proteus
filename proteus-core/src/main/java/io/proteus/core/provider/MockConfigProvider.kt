package io.proteus.core.provider

import io.proteus.core.data.MockConfigRepository
import io.proteus.core.domain.ConfigValue
import io.proteus.core.domain.FeatureContext
import io.proteus.core.exceptions.MockConfigUnavailableException

internal class MockConfigProvider(
    private val mockConfigRepository: MockConfigRepository
) : FeatureConfigProvider {

    @Throws(MockConfigUnavailableException::class)
    override fun getBoolean(feature: FeatureContext<Boolean>): Boolean {
        val localConfig = mockConfigRepository.getMockedConfigValue(feature)

        if (localConfig !is ConfigValue.Boolean) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }

    @Throws(MockConfigUnavailableException::class)
    override fun getString(feature: FeatureContext<String>): String {
        val localConfig = mockConfigRepository.getMockedConfigValue(feature)

        if (localConfig !is ConfigValue.Text) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }

    @Throws(MockConfigUnavailableException::class)
    override fun getLong(feature: FeatureContext<Long>): Long {
        val localConfig = mockConfigRepository.getMockedConfigValue(feature)

        if (localConfig !is ConfigValue.Long) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }

    @Throws(MockConfigUnavailableException::class)
    override fun getDouble(feature: FeatureContext<Double>): Double {
        val localConfig = mockConfigRepository.getMockedConfigValue(feature)

        if (localConfig !is ConfigValue.Double) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }
}
