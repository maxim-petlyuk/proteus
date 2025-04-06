package io.proteus.core.provider

import io.proteus.core.data.MockConfigRepositoryImpl
import io.proteus.core.data.MockConfigStorage
import io.proteus.core.exceptions.MockConfigUnavailableException
import io.proteus.core.mock.MemoryMockConfigStorage
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
        val featureA = "featureA"

        val expectedValue = true
        memoryMockConfigStorage.save(featureA, expectedValue)

        // When
        val result = mockConfigProvider.getBoolean(featureA)

        // Then
        assert(result == expectedValue) {
            "Expected result is $expectedValue, but was [$result]"
        }
    }

    @Test(expected = MockConfigUnavailableException::class)
    fun `should throw exception when boolean config value is not found`() {
        // Given
        val featureA = "featureA"

        // When
        mockConfigProvider.getBoolean(featureA)

        // Then
        // Expecting MockConfigUnavailableException
    }

    @Test
    fun `should return correct mocked string value when feature is found`() {
        // Given
        val featureB = "featureB"

        val expectedValue = "this is a string"
        memoryMockConfigStorage.save(featureB, expectedValue)

        // When
        val result = mockConfigProvider.getString(featureB)

        // Then
        assert(result == expectedValue) {
            "Expected result is $expectedValue, but was [$result]"
        }
    }

    @Test(expected = MockConfigUnavailableException::class)
    fun `should throw exception when text config value is not found`() {
        // Given
        val featureB = "featureB"

        // When
        mockConfigProvider.getString(featureB)

        // Then
        // Expecting MockConfigUnavailableException
    }

    @Test
    fun `should return correct mocked long value when feature is found`() {
        // Given
        val featureC = "featureC"
        val expectedValue = 100L

        memoryMockConfigStorage.save(featureC, expectedValue)

        // When
        val result = mockConfigProvider.getLong(featureC)

        // Then
        assert(result == expectedValue) {
            "Expected result is $expectedValue, but was [$result]"
        }
    }

    @Test(expected = MockConfigUnavailableException::class)
    fun `should throw exception when long config value is not found`() {
        // Given
        val featureC = "featureC"

        // When
        mockConfigProvider.getLong(featureC)

        // Then
        // Expecting MockConfigUnavailableException
    }

    @Test
    fun `should return correct mocked double value when feature is found`() {
        // Given
        val featureD = "featureD"
        val expectedValue = 100.0

        memoryMockConfigStorage.save(featureD, expectedValue)

        // When
        val result = mockConfigProvider.getDouble(featureD)

        // Then
        assert(result == expectedValue) {
            "Expected result is $expectedValue, but was [$result]"
        }
    }

    @Test(expected = MockConfigUnavailableException::class)
    fun `should throw exception when double config value is not found`() {
        // Given
        val featureD = "featureD"

        // When
        mockConfigProvider.getDouble(featureD)

        // Then
        // Expecting MockConfigUnavailableException
    }
}
