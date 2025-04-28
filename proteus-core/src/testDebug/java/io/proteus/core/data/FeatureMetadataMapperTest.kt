package io.proteus.core.data

import io.proteus.core.domain.Feature
import io.proteus.core.domain.FeatureMetadata
import kotlin.test.*

class FeatureMetadataMapperTest {

    private val mapper = FeatureMetadataMapper()

    @Test
    fun `test toFeatureContext for text type`() {
        val metadata = FeatureMetadata(
            featureKey = "test_text",
            defaultValue = "hello",
            valueType = "text",
            serviceOwner = "test_team"
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
            valueType = "long",
            serviceOwner = "test_team"
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
            valueType = "boolean",
            serviceOwner = "test_team"
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
            valueType = "double",
            serviceOwner = "test_team"
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
            valueType = "text",
            serviceOwner = "test_team"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }

    @Test
    fun `test toFeatureContext throws for empty service owner`() {
        val metadata = FeatureMetadata(
            featureKey = "test",
            defaultValue = "test",
            valueType = "text",
            serviceOwner = ""
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
            valueType = "long",
            serviceOwner = "test_team"
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
            valueType = "boolean",
            serviceOwner = "test_team"
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
            valueType = "double",
            serviceOwner = "test_team"
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
            valueType = "invalid_type",
            serviceOwner = "test_team"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }
} 