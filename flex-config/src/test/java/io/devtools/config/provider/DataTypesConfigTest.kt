package io.devtools.config.provider

import io.devtools.config.mock.FeatureTestGuide
import io.devtools.config.mock.MemoryFeatureConfigProvider
import io.devtools.config.mock.MockFeatureConfigOwner
import org.junit.Test

class DataTypesConfigTest {

    @Test
    fun `verify that boolean type function is giving correct result`() {
        // Given
        val featureA = Feature(
            key = "featureA",
            owner = MockFeatureConfigOwner.Firebase,
            defaultValue = false,
            valueClass = Boolean::class
        )

        val featureTestGuideA = FeatureTestGuide(
            feature = featureA,
            mockValue = true,
            remoteValue = false,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideA)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(configFactory)

        // When
        val mockedValueOfFeatureA = configProvider.getBoolean(featureA)

        // Then
        assert(mockedValueOfFeatureA) {
            "Expected mocked value of feature is [${featureTestGuideA.mockValue}], " +
                "but was [$mockedValueOfFeatureA]"
        }
    }

    @Test
    fun `verify that string type function is giving correct result`() {
        // Given
        val featureB = Feature(
            key = "featureB",
            owner = MockFeatureConfigOwner.CleverTap,
            defaultValue = "",
            valueClass = String::class
        )

        val featureTestGuideB = FeatureTestGuide(
            feature = featureB,
            mockValue = "this is mocked value",
            remoteValue = "this is remote value",
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideB)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(configFactory)

        // When
        val mockedValueOfFeatureB = configProvider.getString(featureB)

        // Then
        assert(mockedValueOfFeatureB == featureTestGuideB.mockValue) {
            "Expected mocked value of feature is [${featureTestGuideB.mockValue}], " +
                "but was [$mockedValueOfFeatureB]"
        }
    }

    @Test
    fun `verify that long type function is giving correct result`() {
        // Given
        val featureB = Feature(
            key = "featureB",
            owner = MockFeatureConfigOwner.CleverTap,
            defaultValue = 5L,
            valueClass = Long::class
        )

        val featureTestGuideB = FeatureTestGuide(
            feature = featureB,
            mockValue = 10L,
            remoteValue = 20L,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideB)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(configFactory)

        // When
        val mockedValueOfFeatureB = configProvider.getLong(featureB)

        // Then
        assert(mockedValueOfFeatureB == featureTestGuideB.mockValue) {
            "Expected mocked value of the feature is [${featureTestGuideB.mockValue}], " +
                "but was [$mockedValueOfFeatureB]"
        }
    }

    @Test
    fun `verify that double type function is giving correct result`() {
        // Given
        val featureB = Feature(
            key = "featureB",
            owner = MockFeatureConfigOwner.CleverTap,
            defaultValue = 5.0,
            valueClass = Double::class
        )

        val featureTestGuideB = FeatureTestGuide(
            feature = featureB,
            mockValue = 10.0,
            remoteValue = 20.0,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideB)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(configFactory)

        // When
        val mockedValueOfFeatureB = configProvider.getDouble(featureB)

        // Then
        assert(mockedValueOfFeatureB == featureTestGuideB.mockValue) {
            "Expected mocked value of feature is [${featureTestGuideB.mockValue}], " +
                "but was [$mockedValueOfFeatureB]"
        }
    }
}
