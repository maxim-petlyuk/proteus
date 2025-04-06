package io.proteus.core.provider

import io.proteus.core.exceptions.IllegalConfigOwnerException
import io.proteus.core.mock.FeatureTestGuide
import io.proteus.core.mock.MemoryFeatureConfigProvider
import io.proteus.core.mock.MockFeatureConfigOwner
import io.proteus.core.mock.MockFeatureConfigProviderFactory
import org.junit.Test

internal class MultipleConfigOwnerTest {

    @Test(expected = IllegalConfigOwnerException::class)
    fun `verify that factory will throw exception if provider is not registered`() {
        // Given
        val firebaseFeatureConfigProvider = MemoryFeatureConfigProvider()
        val cleverTapFeatureConfigProvider = MemoryFeatureConfigProvider()

        val configProviderFactory = MockFeatureConfigProviderFactory(
            firebaseFeatureConfigProvider,
            cleverTapFeatureConfigProvider,
            serviceMapping = mapOf(
                "featureA" to MockFeatureConfigOwner.Firebase,
                "featureB" to MockFeatureConfigOwner.CleverTap
            )
        )

        // When
        configProviderFactory.getProvider("UnknownFeatureKey")

        // Then
        // Expecting IllegalConfigOwnerException
    }

    @Test
    fun `verify that the provider returns the correct config value of the right owner`() {
        // Given
        val featureA = "featureA"
        val featureTestGuideA = FeatureTestGuide(
            featureKey = featureA,
            mockValue = true,
            remoteValue = false,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val featureB = "featureB"
        val featureTestGuideB = FeatureTestGuide(
            featureKey = featureB,
            mockValue = "this is mocked value",
            remoteValue = "this is remote value",
            givenSource = FeatureTestGuide.Source.Remote
        )

        val firebaseFeatureConfigProvider = MemoryFeatureConfigProvider(featureTestGuideA)
        val cleverTapFeatureConfigProvider = MemoryFeatureConfigProvider(featureTestGuideB)

        val featureConfigProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            StubFeatureConfigProvider,
            MockFeatureConfigProviderFactory(
                firebaseFeatureConfigProvider,
                cleverTapFeatureConfigProvider,
                serviceMapping = mapOf(
                    featureA to MockFeatureConfigOwner.Firebase,
                    featureB to MockFeatureConfigOwner.CleverTap
                )
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
