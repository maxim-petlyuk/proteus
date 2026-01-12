package io.proteus.core.provider

import io.proteus.core.exceptions.MockConfigUnavailableException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class StubFeatureConfigProviderTest {

    @Test
    fun `getBoolean should throw MockConfigUnavailableException`() = runTest {
        // Arrange
        val featureA = "featureA"
        val stubFeatureConfigProvider = StubFeatureConfigProvider

        // Act & Assert
        val exception = assertFailsWith<MockConfigUnavailableException> {
            stubFeatureConfigProvider.getBoolean(featureA)
        }

        assertEquals(
            "Mock config is not available under StubFeatureConfigProvider",
            exception.message
        )
    }

    @Test
    fun `getString should throw MockConfigUnavailableException`() = runTest {
        // Arrange
        val featureB = "featureB"
        val stubFeatureConfigProvider = StubFeatureConfigProvider

        // Act & Assert
        val exception = assertFailsWith<MockConfigUnavailableException> {
            stubFeatureConfigProvider.getString(featureB)
        }

        assertEquals(
            "Mock config is not available under StubFeatureConfigProvider",
            exception.message
        )
    }

    @Test
    fun `getLong should throw MockConfigUnavailableException`() = runTest {
        // Arrange
        val featureC = "featureC"
        val stubFeatureConfigProvider = StubFeatureConfigProvider

        // Act & Assert
        val exception = assertFailsWith<MockConfigUnavailableException> {
            stubFeatureConfigProvider.getLong(featureC)
        }

        assertEquals(
            "Mock config is not available under StubFeatureConfigProvider",
            exception.message
        )
    }

    @Test
    fun `getDouble should throw MockConfigUnavailableException`() = runTest {
        // Arrange
        val featureD = "featureD"
        val stubFeatureConfigProvider = StubFeatureConfigProvider

        // Act & Assert
        val exception = assertFailsWith<MockConfigUnavailableException> {
            stubFeatureConfigProvider.getDouble(featureD)
        }

        assertEquals(
            "Mock config is not available under StubFeatureConfigProvider",
            exception.message
        )
    }
}