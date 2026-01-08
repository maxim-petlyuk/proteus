package io.proteus.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FeatureTest {

    @Test
    fun `test Feature creation with different data types`() {
        // Test with String
        val stringFeature = Feature(
            key = "test_string",
            defaultValue = "default",
            valueClass = String::class
        )
        assertEquals("test_string", stringFeature.key)
        assertEquals("default", stringFeature.defaultValue)
        assertEquals(String::class, stringFeature.valueClass)

        // Test with Long
        val longFeature = Feature(
            key = "test_long",
            defaultValue = 42L,
            valueClass = Long::class
        )
        assertEquals("test_long", longFeature.key)
        assertEquals(42L, longFeature.defaultValue)
        assertEquals(Long::class, longFeature.valueClass)

        // Test with Boolean
        val booleanFeature = Feature(
            key = "test_boolean",
            defaultValue = true,
            valueClass = Boolean::class
        )
        assertEquals("test_boolean", booleanFeature.key)
        assertTrue(booleanFeature.defaultValue)
        assertEquals(Boolean::class, booleanFeature.valueClass)
    }
} 