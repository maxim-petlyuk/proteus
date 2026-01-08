package io.proteus.core.data

import io.proteus.core.domain.Feature
import io.proteus.core.domain.FeatureMetadata
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FeatureMetadataMapperTest {

    private val mapper = FeatureMetadataMapper()

    @Test
    fun testMapStringFeature() {
        val metadata = FeatureMetadata(
            featureKey = "string_feature",
            defaultValue = "default_text",
            valueType = "text"
        )

        val feature = mapper.toFeatureContext(metadata)

        assertEquals("string_feature", feature.key)
        assertEquals("default_text", feature.defaultValue)
        assertEquals(String::class, feature.valueClass)
        assertTrue(feature is Feature<*>)
    }

    @Test
    fun testMapLongFeature() {
        val metadata = FeatureMetadata(
            featureKey = "long_feature",
            defaultValue = "12345",
            valueType = "long"
        )

        val feature = mapper.toFeatureContext(metadata)

        assertEquals("long_feature", feature.key)
        assertEquals(12345L, feature.defaultValue)
        assertEquals(Long::class, feature.valueClass)
    }

    @Test
    fun testMapBooleanFeature() {
        val metadata = FeatureMetadata(
            featureKey = "boolean_feature",
            defaultValue = "true",
            valueType = "boolean"
        )

        val feature = mapper.toFeatureContext(metadata)

        assertEquals("boolean_feature", feature.key)
        assertEquals(true, feature.defaultValue)
        assertEquals(Boolean::class, feature.valueClass)
    }

    @Test
    fun testMapDoubleFeature() {
        val metadata = FeatureMetadata(
            featureKey = "double_feature",
            defaultValue = "123.45",
            valueType = "double"
        )

        val feature = mapper.toFeatureContext(metadata)

        assertEquals("double_feature", feature.key)
        assertEquals(123.45, feature.defaultValue)
        assertEquals(Double::class, feature.valueClass)
    }

    @Test
    fun testEmptyFeatureKeyThrowsException() {
        val metadata = FeatureMetadata(
            featureKey = "",
            defaultValue = "value",
            valueType = "text"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }

    @Test
    fun testInvalidValueTypeThrowsException() {
        val metadata = FeatureMetadata(
            featureKey = "test_feature",
            defaultValue = "value",
            valueType = "invalid_type"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }

    @Test
    fun testInvalidLongValueThrowsException() {
        val metadata = FeatureMetadata(
            featureKey = "test_feature",
            defaultValue = "not_a_number",
            valueType = "long"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }

    @Test
    fun testInvalidBooleanValueThrowsException() {
        val metadata = FeatureMetadata(
            featureKey = "test_feature",
            defaultValue = "not_a_boolean",
            valueType = "boolean"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }

    @Test
    fun testInvalidDoubleValueThrowsException() {
        val metadata = FeatureMetadata(
            featureKey = "test_feature",
            defaultValue = "not_a_number",
            valueType = "double"
        )

        assertFailsWith<IllegalArgumentException> {
            mapper.toFeatureContext(metadata)
        }
    }
}