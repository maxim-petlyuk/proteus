package io.proteus.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ConfigValueTest {

    @Test
    fun `test same double values are equal`() {
        val value1 = ConfigValue.Double(1.0)
        val value2 = ConfigValue.Double(1.0)
        assertEquals(value1, value2)
    }

    @Test
    fun `test different double values are not equal`() {
        val value1 = ConfigValue.Double(1.0)
        val value2 = ConfigValue.Double(2.0)
        assertFalse(value1 == value2)
    }

    @Test
    fun `test same string values are equal`() {
        val value1 = ConfigValue.Text("value")
        val value2 = ConfigValue.Text("value")
        assertEquals(value1, value2)
    }

    @Test
    fun `test different string values are not equal`() {
        val value1 = ConfigValue.Text("value1")
        val value2 = ConfigValue.Text("value2")
        assertFalse(value1 == value2)
    }

    @Test
    fun `test same boolean values are equal`() {
        val value1 = ConfigValue.Boolean(true)
        val value2 = ConfigValue.Boolean(true)
        assertEquals(value1, value2)
    }

    @Test
    fun `test different boolean values are not equal`() {
        val value1 = ConfigValue.Boolean(true)
        val value2 = ConfigValue.Boolean(false)
        assertFalse(value1 == value2)
    }

    @Test
    fun `test same long values are equal`() {
        val value1 = ConfigValue.Long(1)
        val value2 = ConfigValue.Long(1)
        assertEquals(value1, value2)
    }

    @Test
    fun `test different long values are not equal`() {
        val value1 = ConfigValue.Long(1)
        val value2 = ConfigValue.Long(2)
        assertFalse(value1 == value2)
    }

    @Test
    fun `test same text values have same hash code`() {
        val value1 = ConfigValue.Text("value")
        val value2 = ConfigValue.Text("value")
        assertEquals(value1.hashCode(), value2.hashCode())
    }

    @Test
    fun `test different text values have different hash code`() {
        val value1 = ConfigValue.Text("value1")
        val value2 = ConfigValue.Text("value2")
        assertFalse(value1.hashCode() == value2.hashCode())
    }

    @Test
    fun `test same boolean values have same hash code`() {
        val value1 = ConfigValue.Boolean(true)
        val value2 = ConfigValue.Boolean(true)
        assertEquals(value1.hashCode(), value2.hashCode())
    }

    @Test
    fun `test different boolean values have different hash code`() {
        val value1 = ConfigValue.Boolean(true)
        val value2 = ConfigValue.Boolean(false)
        assertFalse(value1.hashCode() == value2.hashCode())
    }

    @Test
    fun `test same long values have same hash code`() {
        val value1 = ConfigValue.Long(1)
        val value2 = ConfigValue.Long(1)
        assertEquals(value1.hashCode(), value2.hashCode())
    }

    @Test
    fun `test different long values have different hash code`() {
        val value1 = ConfigValue.Long(1)
        val value2 = ConfigValue.Long(2)
        assertFalse(value1.hashCode() == value2.hashCode())
    }

    @Test
    fun `test same double values have same hash code`() {
        val value1 = ConfigValue.Double(1.0)
        val value2 = ConfigValue.Double(1.0)
        assertEquals(value1.hashCode(), value2.hashCode())
    }

    @Test
    fun `test different double values have different hash code`() {
        val value1 = ConfigValue.Double(1.0)
        val value2 = ConfigValue.Double(2.0)
        assertFalse(value1.hashCode() == value2.hashCode())
    }

    @Test
    fun `test text value string representation`() {
        val value = ConfigValue.Text("value")
        assertEquals("value", value.toString())
    }

    @Test
    fun `test double value string representation`() {
        val value = ConfigValue.Double(1.0)
        assertEquals("1.0", value.toString())
    }

    @Test
    fun `test long value string representation`() {
        val value = ConfigValue.Long(1)
        assertEquals("1", value.toString())
    }

    @Test
    fun `test boolean value string representation`() {
        val value = ConfigValue.Boolean(true)
        assertEquals("true", value.toString())
    }
}