package io.proteus.core.data

import io.proteus.core.domain.Feature
import io.proteus.core.domain.FeatureMetadata
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

class FeatureMetadataMapperTest {

    private val mapper = FeatureMetadataMapper()

    @Test
    fun `test toFeatureContext for text type`() {
        val metadata = FeatureMetadata(
            featureKey = "test_text",
            defaultValue = "hello",
            valueType = "text"
        )

        val result = mapper.toFeatureContext(metadata)
        assertIs<Feature<String>>(result)
        assertEquals("test_text", result.key)
        assertEquals("hello", result.defaultValue)
        assertEquals(String::class, result.valueClass)
    }

    @Test
    fun `test toFeatureContext for long type`() {
        val metadata = FeatureMetadata(
            featureKey = "test_long",
            defaultValue = "42",
            valueType = "long"
        )

        val result = mapper.toFeatureContext(metadata)
        assertIs<Feature<Long>>(result)
        assertEquals("test_long", result.key)
        assertEquals(42L, result.defaultValue)
        assertEquals(Long::class, result.valueClass)
    }

    @Test
    fun `test toFeatureContext for boolean type`() {
        val metadata = FeatureMetadata(
            featureKey = "test_boolean",
            defaultValue = "true",
            valueType = "boolean"
        )

        val result = mapper.toFeatureContext(metadata)
        assertIs<Feature<Boolean>>(result)
        assertEquals("test_boolean", result.key)
        assertTrue(result.defaultValue)
        assertEquals(Boolean::class, result.valueClass)
    }

    @Test
    fun `test toFeatureContext for double type`() {
        val metadata = FeatureMetadata(
            featureKey = "test_double",
            defaultValue = "3.14",
            valueType = "double"
        )

        val result = mapper.toFeatureContext(metadata)
        assertIs<Feature<Double>>(result)
        assertEquals("test_double", result.key)
        assertEquals(3.14, result.defaultValue)
        assertEquals(Double::class, result.valueClass)
    }

    @Test
    fun `test toFeatureContext throws for empty feature key`() {
        val metadata = FeatureMetadata(
            featureKey = "",
            defaultValue = "test",
            valueType = "text"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }

    @Test
    fun `test toFeatureContext throws for invalid long value`() {
        val metadata = FeatureMetadata(
            featureKey = "test_long",
            defaultValue = "not_a_number",
            valueType = "long"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }

    @Test
    fun `test toFeatureContext throws for invalid boolean value`() {
        val metadata = FeatureMetadata(
            featureKey = "test_boolean",
            defaultValue = "not_boolean",
            valueType = "boolean"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }

    @Test
    fun `test toFeatureContext throws for invalid double value`() {
        val metadata = FeatureMetadata(
            featureKey = "test_double",
            defaultValue = "not_a_number",
            valueType = "double"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }

    @Test
    fun `test toFeatureContext throws for invalid value type`() {
        val metadata = FeatureMetadata(
            featureKey = "test",
            defaultValue = "test",
            valueType = "invalid_type"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }
} 