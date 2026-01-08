package io.proteus.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import java.io.File

class DataStoreMockConfigStorageTest {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var storage: DataStoreMockConfigStorage

    @Before
    fun setup() {
        val tempFile = File.createTempFile("test_${System.currentTimeMillis()}", ".preferences_pb")
        tempFile.deleteOnExit()

        dataStore = PreferenceDataStoreFactory.create(
            produceFile = { tempFile }
        )
        storage = DataStoreMockConfigStorage(dataStore)
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
        assertEquals(expectedValue, storage.getDouble(featureKey), 0.001)
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