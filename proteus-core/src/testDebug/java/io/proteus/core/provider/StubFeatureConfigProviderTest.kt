package io.proteus.core.provider

import io.proteus.core.exceptions.MockConfigUnavailableException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

internal class StubFeatureConfigProviderTest {

    @Test
    fun `getBoolean should throw MockConfigUnavailableException`() {
        // Arrange
        val featureA = "featureA"
        val stubFeatureConfigProvider = StubFeatureConfigProvider

        // Act
        val exception = assertThrows(MockConfigUnavailableException::class.java) {
            stubFeatureConfigProvider.getBoolean(featureA)
        }

        // Assert
        assertEquals(
            "Mock config is not available under StubFeatureConfigProvider",
            exception.message
        )
    }

    @Test
    fun `getString should throw MockConfigUnavailableException`() {
        // Arrange
        val featureB = "featureB"
        val stubFeatureConfigProvider = StubFeatureConfigProvider

        // Act
        val exception = assertThrows(MockConfigUnavailableException::class.java) {
            stubFeatureConfigProvider.getString(featureB)
        }

        // Assert
        assertEquals(
            "Mock config is not available under StubFeatureConfigProvider",
            exception.message
        )
    }

    @Test
    fun `getLong should throw MockConfigUnavailableException`() {
        // Arrange
        val featureC = "featureC"
        val stubFeatureConfigProvider = StubFeatureConfigProvider

        // Act
        val exception = assertThrows(MockConfigUnavailableException::class.java) {
            stubFeatureConfigProvider.getLong(featureC)
        }

        // Assert
        assertEquals(
            "Mock config is not available under StubFeatureConfigProvider",
            exception.message
        )
    }

    @Test
    fun `getDouble should throw MockConfigUnavailableException`() {
        // Arrange
        val featureD = "featureD"
        val stubFeatureConfigProvider = StubFeatureConfigProvider

        // Act
        val exception = assertThrows(MockConfigUnavailableException::class.java) {
            stubFeatureConfigProvider.getDouble(featureD)
        }

        // Assert
        assertEquals(
            "Mock config is not available under StubFeatureConfigProvider",
            exception.message
        )
    }
}
