package io.proteus.core.provider

import io.proteus.core.data.MockConfigRepository
import io.proteus.core.exceptions.MockConfigUnavailableException

internal class MockConfigProvider(
    private val mockConfigRepository: MockConfigRepository
) : FeatureConfigProvider {

    @Throws(MockConfigUnavailableException::class)
    override fun getBoolean(feature: Feature<Boolean>): Boolean {
        val localConfig = mockConfigRepository.getMockedConfigValue(feature)

        if (localConfig !is ConfigValue.Boolean) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }

    @Throws(MockConfigUnavailableException::class)
    override fun getString(feature: Feature<String>): String {
        val localConfig = mockConfigRepository.getMockedConfigValue(feature)

        if (localConfig !is ConfigValue.Text) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }

    @Throws(MockConfigUnavailableException::class)
    override fun getLong(feature: Feature<Long>): Long {
        val localConfig = mockConfigRepository.getMockedConfigValue(feature)

        if (localConfig !is ConfigValue.Long) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }

    @Throws(MockConfigUnavailableException::class)
    override fun getDouble(feature: Feature<Double>): Double {
        val localConfig = mockConfigRepository.getMockedConfigValue(feature)

        if (localConfig !is ConfigValue.Double) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }
}
