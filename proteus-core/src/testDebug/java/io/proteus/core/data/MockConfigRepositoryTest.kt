package io.proteus.core.data

import io.proteus.core.domain.ConfigValue
import io.proteus.core.exceptions.IllegalConfigDataTypeException
import io.proteus.core.mock.MemoryMockConfigStorage
import io.proteus.core.mock.MockFeatureConfigOwner
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class MockConfigRepositoryTest {

    private val memoryMockConfigStorage: MockConfigStorage = MemoryMockConfigStorage()
    private val mockConfigRepository: MockConfigRepository = MockConfigRepositoryImpl(memoryMockConfigStorage)

    @Before
    fun setUp() = runTest {
        memoryMockConfigStorage.clear()
    }

    @Test
    fun `should return null when feature is not found`() = runTest {
        // Given
        val featureKey = "feature"

        // When
        val result = mockConfigRepository.getMockedConfigValue(featureKey, String::class)

        // Then
        assertNull(result)
    }

    @Test
    fun `should return correct long value when feature is found`() = runTest {
        // Given
        val featureKey = "feature"

        val expectedValue = 5L
        memoryMockConfigStorage.save(featureKey, expectedValue)

        // When
        val result = mockConfigRepository.getMockedConfigValue(featureKey, Long::class)

        // Then
        assertIs<ConfigValue.Long>(result)
        assertEquals(expectedValue, result.value)
    }

    @Test
    fun `should return correct string value when feature is found`() = runTest {
        // Given
        val featureKey = "feature"
        val expectedValue = "John Doe"

        memoryMockConfigStorage.save(featureKey, expectedValue)

        // When
        val result = mockConfigRepository.getMockedConfigValue(featureKey, String::class)

        // Then
        assertIs<ConfigValue.Text>(result)
        assertEquals(expectedValue, result.value)
    }

    @Test
    fun `should return correct double value when feature is found`() = runTest {
        // Given
        val featureKey = "feature"
        val expectedValue = 5.7

        memoryMockConfigStorage.save(featureKey, expectedValue)

        // When
        val result = mockConfigRepository.getMockedConfigValue(featureKey, Double::class)

        // Then
        assertIs<ConfigValue.Double>(result)
        assertEquals(expectedValue, result.value)
    }

    @Test
    fun `should return correct boolean value when feature is found`() = runTest {
        // Given
        val featureKey = "feature"
        val expectedValue = true

        memoryMockConfigStorage.save(featureKey, expectedValue)

        // When
        val result = mockConfigRepository.getMockedConfigValue(featureKey, Boolean::class)

        // Then
        assertIs<ConfigValue.Boolean>(result)
        assertEquals(expectedValue, result.value)
    }

    @Test
    fun `throw exception when feature is found but value is not of correct type`() = runTest {
        // Given
        val featureKey = "feature"
        val expectedValue = "John Doe"

        memoryMockConfigStorage.save(featureKey, expectedValue)

        // When & Then
        assertFailsWith<IllegalConfigDataTypeException> {
            mockConfigRepository.getMockedConfigValue(featureKey, MockFeatureConfigOwner::class)
        }
    }
}
