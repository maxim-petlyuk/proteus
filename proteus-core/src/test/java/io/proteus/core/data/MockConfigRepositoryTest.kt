package io.proteus.core.data

import io.proteus.core.exceptions.IllegalConfigDataTypeException
import io.proteus.core.mock.MemoryMockConfigStorage
import io.proteus.core.mock.MockFeatureConfigOwner
import io.proteus.core.provider.ConfigValue
import io.proteus.core.provider.Feature
import org.junit.Before
import org.junit.Test

internal class MockConfigRepositoryTest {

    private val memoryMockConfigStorage: MockConfigStorage = MemoryMockConfigStorage()
    private val mockConfigRepository: MockConfigRepository =
        MockConfigRepositoryImpl(memoryMockConfigStorage)

    @Before
    fun setUp() {
        memoryMockConfigStorage.clear()
    }

    @Test
    fun `should return null when feature is not found`() {
        // Given
        val feature = Feature(
            "feature",
            false,
            Boolean::class,
            MockFeatureConfigOwner.Firebase
        )

        // When
        val result = mockConfigRepository.getMockedConfigValue(feature)

        // Then
        assert(result == null)
    }

    @Test
    fun `should return correct long value when feature is found`() {
        // Given
        val feature = Feature(
            "feature",
            0L,
            Long::class,
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = 5L
        memoryMockConfigStorage.save(feature.key, expectedValue)

        // When
        val result = mockConfigRepository.getMockedConfigValue(feature)

        // Then
        assert(result is ConfigValue.Long) {
            "Expected result is Long, but was [${result?.javaClass}]"
        }

        assert(result?.value as Long == expectedValue) {
            "Expected result is [$expectedValue], but was [${result.value}]"
        }
    }

    @Test
    fun `should return correct string value when feature is found`() {
        // Given
        val feature = Feature(
            "feature",
            "",
            String::class,
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = "John Doe"
        memoryMockConfigStorage.save(feature.key, expectedValue)

        // When
        val result = mockConfigRepository.getMockedConfigValue(feature)

        // Then
        assert(result is ConfigValue.Text) {
            "Expected result is String, but was [${result?.javaClass}]"
        }

        assert(result?.value as String == expectedValue) {
            "Expected result is [$expectedValue], but was [${result.value}]"
        }
    }

    @Test
    fun `should return correct double value when feature is found`() {
        // Given
        val feature = Feature(
            "feature",
            0.0,
            Double::class,
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = 5.7
        memoryMockConfigStorage.save(feature.key, expectedValue)

        // When
        val result = mockConfigRepository.getMockedConfigValue(feature)

        // Then
        assert(result is ConfigValue.Double) {
            "Expected result is Double, but was [${result?.javaClass}]"
        }

        assert(result?.value as Double == expectedValue) {
            "Expected result is [$expectedValue], but was [${result.value}]"
        }
    }

    @Test
    fun `should return correct boolean value when feature is found`() {
        // Given
        val feature = Feature(
            "feature",
            false,
            Boolean::class,
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = true
        memoryMockConfigStorage.save(feature.key, expectedValue)

        // When
        val result = mockConfigRepository.getMockedConfigValue(feature)

        // Then
        assert(result is ConfigValue.Boolean) {
            "Expected result is Boolean, but was [${result?.javaClass}]"
        }

        assert(result?.value as Boolean == expectedValue) {
            "Expected result is [$expectedValue], but was [${result.value}]"
        }
    }

    @Test(expected = IllegalConfigDataTypeException::class)
    fun `throw exception when feature is found but value is not of correct type`() {
        // Given
        val feature = Feature(
            "feature",
            MockFeatureConfigOwner.Firebase,
            MockFeatureConfigOwner::class,
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = "John Doe"
        memoryMockConfigStorage.save(feature.key, expectedValue)

        // When
        mockConfigRepository.getMockedConfigValue(feature)

        // Then
    }
}
