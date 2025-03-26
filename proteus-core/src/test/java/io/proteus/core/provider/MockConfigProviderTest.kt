package io.proteus.core.provider

import io.proteus.core.data.MockConfigRepositoryImpl
import io.proteus.core.data.MockConfigStorage
import io.proteus.core.domain.Feature
import io.proteus.core.exceptions.MockConfigUnavailableException
import io.proteus.core.mock.MemoryMockConfigStorage
import io.proteus.core.mock.MockFeatureConfigOwner
import org.junit.Before
import org.junit.Test

internal class MockConfigProviderTest {

    private val memoryMockConfigStorage: MockConfigStorage = MemoryMockConfigStorage()
    private val mockConfigProvider: MockConfigProvider =
        MockConfigProvider(MockConfigRepositoryImpl(memoryMockConfigStorage))

    @Before
    fun setUp() {
        memoryMockConfigStorage.clear()
    }

    @Test
    fun `should return correct mocked boolean value when feature is found`() {
        // Given
        val feature = Feature(
            "featureA",
            false,
            Boolean::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = true
        memoryMockConfigStorage.save(feature.key, expectedValue)

        // When
        val result = mockConfigProvider.getBoolean(feature)

        // Then
        assert(result == expectedValue) {
            "Expected result is $expectedValue, but was [$result]"
        }
    }

    @Test(expected = MockConfigUnavailableException::class)
    fun `should throw exception when boolean config value is not found`() {
        // Given
        val feature = Feature(
            "featureA",
            false,
            Boolean::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        // When
        mockConfigProvider.getBoolean(feature)

        // Then
        // Expecting MockConfigUnavailableException
    }

    @Test
    fun `should return correct mocked string value when feature is found`() {
        // Given
        val feature = Feature(
            "featureB",
            "",
            String::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = "this is a string"
        memoryMockConfigStorage.save(feature.key, expectedValue)

        // When
        val result = mockConfigProvider.getString(feature)

        // Then
        assert(result == expectedValue) {
            "Expected result is $expectedValue, but was [$result]"
        }
    }

    @Test(expected = MockConfigUnavailableException::class)
    fun `should throw exception when text config value is not found`() {
        // Given
        val feature = Feature(
            "featureB",
            "",
            String::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        // When
        mockConfigProvider.getString(feature)

        // Then
        // Expecting MockConfigUnavailableException
    }

    @Test
    fun `should return correct mocked long value when feature is found`() {
        // Given
        val feature = Feature(
            "featureC",
            0L,
            Long::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = 100L
        memoryMockConfigStorage.save(feature.key, expectedValue)

        // When
        val result = mockConfigProvider.getLong(feature)

        // Then
        assert(result == expectedValue) {
            "Expected result is $expectedValue, but was [$result]"
        }
    }

    @Test(expected = MockConfigUnavailableException::class)
    fun `should throw exception when long config value is not found`() {
        // Given
        val feature = Feature(
            "featureC",
            0L,
            Long::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        // When
        mockConfigProvider.getLong(feature)

        // Then
        // Expecting MockConfigUnavailableException
    }

    @Test
    fun `should return correct mocked double value when feature is found`() {
        // Given
        val feature = Feature(
            "featureD",
            0.0,
            Double::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = 100.0
        memoryMockConfigStorage.save(feature.key, expectedValue)

        // When
        val result = mockConfigProvider.getDouble(feature)

        // Then
        assert(result == expectedValue) {
            "Expected result is $expectedValue, but was [$result]"
        }
    }

    @Test(expected = MockConfigUnavailableException::class)
    fun `should throw exception when double config value is not found`() {
        // Given
        val feature = Feature(
            "featureD",
            0.0,
            Double::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        // When
        mockConfigProvider.getDouble(feature)

        // Then
        // Expecting MockConfigUnavailableException
    }
}
