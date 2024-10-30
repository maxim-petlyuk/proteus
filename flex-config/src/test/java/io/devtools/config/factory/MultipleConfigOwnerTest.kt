package io.devtools.config.factory

import io.devtools.config.provider.Feature
import io.devtools.config.provider.FeatureConfigOwner
import io.devtools.config.provider.FeatureConfigProvider
import io.devtools.config.provider.FeatureConfigProviderImpl
import io.devtools.config.exceptions.IllegalConfigOwnerException
import io.devtools.config.mock.FeatureTestGuide
import io.devtools.config.mock.MemoryFeatureConfigProvider
import io.devtools.config.mock.MockFeatureConfigOwner
import io.devtools.config.mock.MockFeatureConfigProviderFactory
import org.junit.Test

class MultipleConfigOwnerTest {

    @Test(expected = IllegalConfigOwnerException::class)
    fun `verify that factory will throw exception if provider is not registered`() {
        // Given
        val firebaseFeatureConfigProvider = MemoryFeatureConfigProvider()
        val cleverTapFeatureConfigProvider = MemoryFeatureConfigProvider()

        val configProviderFactory = MockFeatureConfigProviderFactory(
            firebaseFeatureConfigProvider,
            cleverTapFeatureConfigProvider
        )

        // When
        val unknownProvider = object : FeatureConfigOwner {}
        configProviderFactory.getProvider(unknownProvider)

        // Then
        // Expecting IllegalConfigOwnerException
    }

    @Test
    fun `verify that the provider returns the correct config value of the right owner`() {
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
            givenSource = FeatureTestGuide.Source.Remote
        )

        val firebaseFeatureConfigProvider = MemoryFeatureConfigProvider(featureTestGuideA)
        val cleverTapFeatureConfigProvider = MemoryFeatureConfigProvider(featureTestGuideB)

        val featureConfigProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            MockFeatureConfigProviderFactory(
                firebaseFeatureConfigProvider,
                cleverTapFeatureConfigProvider
            )
        )

        // When
        val mockedValueOfFeatureA = featureConfigProvider.getBoolean(featureA)
        val remoteValueOfFeatureB = featureConfigProvider.getString(featureB)

        // Then
        assert(mockedValueOfFeatureA) {
            "Expected mocked value of featureA is [${featureTestGuideA.mockValue}], " +
                "but was [$mockedValueOfFeatureA]"
        }

        assert(remoteValueOfFeatureB == featureTestGuideB.remoteValue) {
            "Expected remote value of featureB is [${featureTestGuideB.remoteValue}], " +
                "but was [$remoteValueOfFeatureB]"
        }
    }
}
