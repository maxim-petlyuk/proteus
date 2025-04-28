package io.proteus.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConfigValueTest {

    @Test
    fun `test Long ConfigValue`() {
        val longValue = ConfigValue.Long(42L)
        assertEquals(42L, longValue.value)
        assertEquals("42", longValue.toString())
    }

    @Test
    fun `test Double ConfigValue`() {
        val doubleValue = ConfigValue.Double(3.14)
        assertEquals(3.14, doubleValue.value)
        assertEquals("3.14", doubleValue.toString())
    }

    @Test
    fun `test Text ConfigValue`() {
        val textValue = ConfigValue.Text("hello")
        assertEquals("hello", textValue.value)
        assertEquals("hello", textValue.toString())
    }

    @Test
    fun `test Boolean ConfigValue`() {
        val booleanValue = ConfigValue.Boolean(true)
        assertTrue(booleanValue.value)
        assertEquals("true", booleanValue.toString())
    }

    @Test
    fun `test ConfigValue equality`() {
        val long1 = ConfigValue.Long(42L)
        val long2 = ConfigValue.Long(42L)
        val long3 = ConfigValue.Long(43L)
        
        assertEquals(long1, long2)
        assertFalse(long1 == long3)
    }

    @Test
    fun `test ConfigValue hashCode`() {
        val long1 = ConfigValue.Long(42L)
        val long2 = ConfigValue.Long(42L)
        val long3 = ConfigValue.Long(43L)
        
        assertEquals(long1.hashCode(), long2.hashCode())
        assertFalse(long1.hashCode() == long3.hashCode())
    }
} 