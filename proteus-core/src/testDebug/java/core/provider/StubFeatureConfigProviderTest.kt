package core.provider

import core.mock.MockFeatureConfigOwner
import io.proteus.core.domain.Feature
import io.proteus.core.exceptions.MockConfigUnavailableException
import io.proteus.core.provider.StubFeatureConfigProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

internal class StubFeatureConfigProviderTest {

    @Test
    fun `getBoolean should throw MockConfigUnavailableException`() {
        // Arrange
        val featureA = Feature(
            key = "featureA",
            owner = MockFeatureConfigOwner.Firebase.serviceOwner,
            defaultValue = false,
            valueClass = Boolean::class
        )

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
        val featureB = Feature(
            key = "featureB",
            owner = MockFeatureConfigOwner.CleverTap.serviceOwner,
            defaultValue = "",
            valueClass = String::class
        )

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
        val featureC = Feature(
            key = "featureC",
            owner = MockFeatureConfigOwner.Firebase.serviceOwner,
            defaultValue = 0L,
            valueClass = Long::class
        )

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
        val featureD = Feature(
            key = "featureD",
            owner = MockFeatureConfigOwner.CleverTap.serviceOwner,
            defaultValue = 0.0,
            valueClass = Double::class
        )

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
