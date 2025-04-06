package io.proteus.core.provider

import io.proteus.core.data.MockConfigRepository
import io.proteus.core.domain.ConfigValue
import io.proteus.core.exceptions.MockConfigUnavailableException

internal class MockConfigProvider(
    private val mockConfigRepository: MockConfigRepository
) : FeatureConfigProvider {

    @Throws(MockConfigUnavailableException::class)
    override fun getBoolean(featureKey: String): Boolean {
        val localConfig = mockConfigRepository.getMockedConfigValue(featureKey, Boolean::class)

        if (localConfig !is ConfigValue.Boolean) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }

    @Throws(MockConfigUnavailableException::class)
    override fun getString(featureKey: String): String {
        val localConfig = mockConfigRepository.getMockedConfigValue(featureKey, String::class)

        if (localConfig !is ConfigValue.Text) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }

    @Throws(MockConfigUnavailableException::class)
    override fun getLong(featureKey: String): Long {
        val localConfig = mockConfigRepository.getMockedConfigValue(featureKey, Long::class)

        if (localConfig !is ConfigValue.Long) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }

    @Throws(MockConfigUnavailableException::class)
    override fun getDouble(featureKey: String): Double {
        val localConfig = mockConfigRepository.getMockedConfigValue(featureKey, Double::class)

        if (localConfig !is ConfigValue.Double) {
            throw MockConfigUnavailableException()
        }

        return localConfig.value
    }
}
