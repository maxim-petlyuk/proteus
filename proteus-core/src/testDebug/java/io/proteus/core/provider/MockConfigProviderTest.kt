package io.proteus.core.provider

import io.proteus.core.data.MockConfigRepositoryImpl
import io.proteus.core.data.MockConfigStorage
import io.proteus.core.exceptions.MockConfigUnavailableException
import io.proteus.core.mock.MemoryMockConfigStorage
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class MockConfigProviderTest {

    private val memoryMockConfigStorage: MockConfigStorage = MemoryMockConfigStorage()
    private val mockConfigProvider: MockConfigProvider =
        MockConfigProvider(MockConfigRepositoryImpl(memoryMockConfigStorage))

    @Before
    fun setUp() = runTest {
        memoryMockConfigStorage.clear()
    }

    @Test
    fun `should return correct mocked boolean value when feature is found`() = runTest {
        // Given
        val featureA = "featureA"

        val expectedValue = true
        memoryMockConfigStorage.save(featureA, expectedValue)

        // When
        val result = mockConfigProvider.getBoolean(featureA)

        // Then
        assertEquals(expectedValue, result)
    }

    @Test
    fun `should throw exception when boolean config value is not found`() = runTest {
        // Given
        val featureA = "featureA"

        // When & Then
        assertFailsWith<MockConfigUnavailableException> {
            mockConfigProvider.getBoolean(featureA)
        }
    }

    @Test
    fun `should return correct mocked string value when feature is found`() = runTest {
        // Given
        val featureB = "featureB"

        val expectedValue = "this is a string"
        memoryMockConfigStorage.save(featureB, expectedValue)

        // When
        val result = mockConfigProvider.getString(featureB)

        // Then
        assertEquals(expectedValue, result)
    }

    @Test
    fun `should throw exception when text config value is not found`() = runTest {
        // Given
        val featureB = "featureB"

        // When & Then
        assertFailsWith<MockConfigUnavailableException> {
            mockConfigProvider.getString(featureB)
        }
    }

    @Test
    fun `should return correct mocked long value when feature is found`() = runTest {
        // Given
        val featureC = "featureC"
        val expectedValue = 100L

        memoryMockConfigStorage.save(featureC, expectedValue)

        // When
        val result = mockConfigProvider.getLong(featureC)

        // Then
        assertEquals(expectedValue, result)
    }

    @Test
    fun `should throw exception when long config value is not found`() = runTest {
        // Given
        val featureC = "featureC"

        // When & Then
        assertFailsWith<MockConfigUnavailableException> {
            mockConfigProvider.getLong(featureC)
        }
    }

    @Test
    fun `should return correct mocked double value when feature is found`() = runTest {
        // Given
        val featureD = "featureD"
        val expectedValue = 100.0

        memoryMockConfigStorage.save(featureD, expectedValue)

        // When
        val result = mockConfigProvider.getDouble(featureD)

        // Then
        assertEquals(expectedValue, result)
    }

    @Test
    fun `should throw exception when double config value is not found`() = runTest {
        // Given
        val featureD = "featureD"

        // When & Then
        assertFailsWith<MockConfigUnavailableException> {
            mockConfigProvider.getDouble(featureD)
        }
    }
}
