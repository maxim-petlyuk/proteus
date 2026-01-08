package io.proteus.core.data

import io.proteus.core.mock.MemoryMockConfigStorage
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MockConfigStorageTest {

    private lateinit var storage: MockConfigStorage

    @BeforeTest
    fun setup() {
        // Use MemoryMockConfigStorage for cross-platform testing
        // DataStore integration is tested in platform-specific tests
        storage = MemoryMockConfigStorage()
    }

    @Test
    fun `should return false when feature is not found`() = runTest {
        val result = storage.contains("non_existent_feature")
        assertFalse(result)
    }

    @Test
    fun `should return correct long value when feature is found`() = runTest {
        val featureKey = "test_long_feature"
        val expectedValue = 123456L

        storage.save(featureKey, expectedValue)

        assertTrue(storage.contains(featureKey))
        assertEquals(expectedValue, storage.getLong(featureKey))
    }

    @Test
    fun `should return correct double value when feature is found`() = runTest {
        val featureKey = "test_double_feature"
        val expectedValue = 123.456

        storage.save(featureKey, expectedValue)

        assertTrue(storage.contains(featureKey))
        assertEquals(expectedValue, storage.getDouble(featureKey))
    }

    @Test
    fun `should return correct string value when feature is found`() = runTest {
        val featureKey = "test_string_feature"
        val expectedValue = "test_value"

        storage.save(featureKey, expectedValue)

        assertTrue(storage.contains(featureKey))
        assertEquals(expectedValue, storage.getString(featureKey))
    }

    @Test
    fun `should return correct boolean value when feature is found`() = runTest {
        val featureKey = "test_boolean_feature"
        val expectedValue = true

        storage.save(featureKey, expectedValue)

        assertTrue(storage.contains(featureKey))
        assertEquals(expectedValue, storage.getBoolean(featureKey))
    }

    @Test
    fun `should remove feature when requested`() = runTest {
        val featureKey = "test_remove_feature"
        val value = "test_value"

        storage.save(featureKey, value)
        assertTrue(storage.contains(featureKey))

        storage.remove(featureKey)
        assertFalse(storage.contains(featureKey))
    }

    @Test
    fun `should clear all features when requested`() = runTest {
        val featureKey1 = "test_feature_1"
        val featureKey2 = "test_feature_2"

        storage.save(featureKey1, "value1")
        storage.save(featureKey2, 123L)

        assertTrue(storage.contains(featureKey1))
        assertTrue(storage.contains(featureKey2))

        storage.clear()

        assertFalse(storage.contains(featureKey1))
        assertFalse(storage.contains(featureKey2))
    }
}