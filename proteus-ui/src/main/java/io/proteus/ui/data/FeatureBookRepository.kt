package io.proteus.ui.data

import io.proteus.core.data.FeatureBookDataSource
import io.proteus.core.data.MockConfigRepository
import io.proteus.core.domain.ConfigValue
import io.proteus.core.domain.Feature
import io.proteus.core.domain.FeatureContext
import io.proteus.core.provider.FeatureConfigProviderFactory
import io.proteus.ui.domain.entity.FeatureNote
import kotlin.reflect.KClass

internal class FeatureBookRepository(
    private val featureBookDataSource: FeatureBookDataSource,
    private val remoteConfigProviderFactory: FeatureConfigProviderFactory,
    private val mockConfigRepository: MockConfigRepository
) {

    suspend fun removeMockedConfig(featureKey: String) {
        mockConfigRepository.remove(featureKey = featureKey)
    }

    suspend fun saveMockedConfig(featureKey: String, configValue: ConfigValue<*>) {
        mockConfigRepository.save(
            featureKey = featureKey,
            typeClass = getTypeClass(configValue),
            configValue = configValue
        )
    }

    suspend fun getFeatureNote(featureKey: String): Result<FeatureNote<*>> {
        return try {
            getFeatureBook()
                .map {
                    it.find { it.feature.key == featureKey }
                        ?: throw IllegalStateException("Feature with key $featureKey not found")
                }
        } catch (error: IllegalStateException) {
            Result.failure(error)
        }
    }

    suspend fun getFeatureBook(): Result<List<FeatureNote<*>>> {
        return featureBookDataSource.getFeatureBook()
            .map { it.aggregateConfig() }
    }

    private fun List<FeatureContext<*>>.aggregateConfig(): List<FeatureNote<*>> {
        return this.map { featureContext ->
            FeatureNote<Any>(
                serviceOwner = remoteConfigProviderFactory.getProviderTag(featureContext.key),
                feature = featureContext.buildFeature(),
                remoteConfigValue = getRemoteConfigValue(featureContext),
                localConfigValue = getLocalConfigValue(featureContext) as? ConfigValue<Any>
            )
        }
    }

    private fun <T : Any> getRemoteConfigValue(featureContext: FeatureContext<T>): String {
        val remoteConfigProvider = remoteConfigProviderFactory.getProvider(featureContext.key)

        return when (featureContext.valueClass) {
            Int::class, Long::class -> remoteConfigProvider.getLong(featureContext.key).toString()
            String::class -> remoteConfigProvider.getString(featureContext.key)
            Boolean::class -> remoteConfigProvider.getBoolean(featureContext.key).toString()
            Double::class -> remoteConfigProvider.getDouble(featureContext.key).toString()
            else -> throw IllegalArgumentException("Unsupported data type: ${featureContext.valueClass}")
        }
    }

    private fun getLocalConfigValue(featureContext: FeatureContext<*>): ConfigValue<*>? {
        return mockConfigRepository.getMockedConfigValue(
            featureKey = featureContext.key,
            typeClass = featureContext.valueClass
        )
    }

    private fun FeatureContext<*>.buildFeature(): Feature<Any> {
        return Feature<Any>(
            key = this.key,
            defaultValue = this.defaultValue,
            valueClass = this.valueClass as KClass<Any>,
        )
    }

    private fun getTypeClass(configValue: ConfigValue<*>): KClass<*> {
        return when (configValue) {
            is ConfigValue.Long -> Long::class
            is ConfigValue.Text -> String::class
            is ConfigValue.Boolean -> Boolean::class
            is ConfigValue.Double -> Double::class
        }
    }
}
