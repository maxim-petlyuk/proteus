package io.proteus.ui.data

import io.proteus.core.data.FeatureBookDataSource
import io.proteus.core.data.MockConfigRepository
import io.proteus.core.provider.ConfigValue
import io.proteus.core.provider.Feature
import io.proteus.core.provider.FeatureConfigProviderFactory
import io.proteus.core.provider.FeatureContext
import io.proteus.ui.domain.entity.FeatureNote
import kotlin.reflect.KClass

internal class FeatureBookRepository(
    private val featureBookDataSource: FeatureBookDataSource,
    private val remoteConfigProviderFactory: FeatureConfigProviderFactory,
    private val mockConfigRepository: MockConfigRepository
) {

    suspend fun removeMockedConfig(feature: FeatureContext<*>) {
        mockConfigRepository.remove(feature)
    }

    suspend fun saveMockedConfig(feature: FeatureContext<*>, configValue: ConfigValue<*>) {
        mockConfigRepository.save(feature, configValue)
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
                feature = featureContext.buildFeature(),
                remoteConfigValue = getRemoteConfigValue(featureContext),
                localConfigValue = getLocalConfigValue(featureContext) as? ConfigValue<Any>
            )
        }
    }

    private fun <T : Any> getRemoteConfigValue(featureContext: FeatureContext<T>): String {
        val remoteConfigProvider = remoteConfigProviderFactory.getProvider(featureContext.owner)

        return when (featureContext.valueClass) {
            Long::class -> remoteConfigProvider.getLong(featureContext as FeatureContext<Long>).toString()
            String::class -> remoteConfigProvider.getString(featureContext as FeatureContext<String>)
            Boolean::class -> remoteConfigProvider.getBoolean(featureContext as FeatureContext<Boolean>).toString()
            Double::class -> remoteConfigProvider.getDouble(featureContext as FeatureContext<Double>).toString()
            else -> throw IllegalArgumentException("Unsupported data type: ${featureContext.valueClass}")
        }
    }

    private fun getLocalConfigValue(featureContext: FeatureContext<*>): ConfigValue<*>? {
        return mockConfigRepository.getMockedConfigValue(featureContext)
    }

    private fun FeatureContext<*>.buildFeature(): Feature<Any> {
        return Feature<Any>(
            key = this.key,
            defaultValue = this.defaultValue,
            valueClass = this.valueClass as KClass<Any>,
            owner = this.owner
        )
    }
}
