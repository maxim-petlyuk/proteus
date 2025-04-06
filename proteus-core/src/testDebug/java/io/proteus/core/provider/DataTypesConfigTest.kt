package io.proteus.core.provider

import io.proteus.core.mock.FeatureTestGuide
import io.proteus.core.mock.MemoryFeatureConfigProvider
import org.junit.Test

internal class DataTypesConfigTest {

    @Test
    fun `verify that boolean type function is giving correct result`() {
        // Given
        val featureKey = "featureA"

        val featureTestGuideA = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = true,
            remoteValue = false,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            StubFeatureConfigProvider,
            TestFeatureConfigProviderFactory(featureTestGuideA)
        )

        // When
        val mockedValueOfFeatureA = configProvider.getBoolean(featureKey)

        // Then
        assert(mockedValueOfFeatureA) {
            "Expected mocked value of feature is [${featureTestGuideA.mockValue}], " +
                "but was [$mockedValueOfFeatureA]"
        }
    }

    @Test
    fun `verify that string type function is giving correct result`() {
        // Given
        val featureB = "featureB"

        val featureTestGuideB = FeatureTestGuide(
            featureKey = featureB,
            mockValue = "this is mocked value",
            remoteValue = "this is remote value",
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            StubFeatureConfigProvider,
            TestFeatureConfigProviderFactory(featureTestGuideB)
        )

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
        val featureB = "featureB"

        val featureTestGuideB = FeatureTestGuide(
            featureKey = featureB,
            mockValue = 10L,
            remoteValue = 20L,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            StubFeatureConfigProvider,
            TestFeatureConfigProviderFactory(featureTestGuideB)
        )

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
        val featureB = "featureB"

        val featureTestGuideB = FeatureTestGuide(
            featureKey = featureB,
            mockValue = 10.0,
            remoteValue = 20.0,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            StubFeatureConfigProvider,
            TestFeatureConfigProviderFactory(featureTestGuideB)
        )

        // When
        val mockedValueOfFeatureB = configProvider.getDouble(featureB)

        // Then
        assert(mockedValueOfFeatureB == featureTestGuideB.mockValue) {
            "Expected mocked value of feature is [${featureTestGuideB.mockValue}], " +
                "but was [$mockedValueOfFeatureB]"
        }
    }

    private class TestFeatureConfigProviderFactory(vararg featuresGuide: FeatureTestGuide<*>) : FeatureConfigProviderFactory {

        private val guide = featuresGuide.toList()

        override fun getProvider(owner: String): FeatureConfigProvider {
            return MemoryFeatureConfigProvider(guide)
        }

        override fun getProviderTag(featureKey: String): String {
            return "TestFeatureConfigProviderFactory"
        }
    }
}
