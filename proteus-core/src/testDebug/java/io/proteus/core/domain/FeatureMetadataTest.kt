package io.proteus.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FeatureMetadataTest {

    @Test
    fun `test FeatureMetadata creation and properties`() {
        val metadata = FeatureMetadata(
            featureKey = "test_feature",
            defaultValue = "default_value",
            valueType = "String",
            serviceOwner = "test_team"
        )

        assertEquals("test_feature", metadata.featureKey)
        assertEquals("default_value", metadata.defaultValue)
        assertEquals("String", metadata.valueType)
        assertEquals("test_team", metadata.serviceOwner)
    }

    @Test
    fun `test FeatureMetadata equality`() {
        val metadata1 = FeatureMetadata(
            featureKey = "test_feature",
            defaultValue = "default_value",
            valueType = "String",
            serviceOwner = "test_team"
        )

        val metadata2 = FeatureMetadata(
            featureKey = "test_feature",
            defaultValue = "default_value",
            valueType = "String",
            serviceOwner = "test_team"
        )

        val metadata3 = FeatureMetadata(
            featureKey = "different_feature",
            defaultValue = "default_value",
            valueType = "String",
            serviceOwner = "test_team"
        )

        assertEquals(metadata1, metadata2)
        assertFalse(metadata1 == metadata3)
    }

    @Test
    fun `test FeatureMetadata hashCode`() {
        val metadata1 = FeatureMetadata(
            featureKey = "test_feature",
            defaultValue = "default_value",
            valueType = "String",
            serviceOwner = "test_team"
        )

        val metadata2 = FeatureMetadata(
            featureKey = "test_feature",
            defaultValue = "default_value",
            valueType = "String",
            serviceOwner = "test_team"
        )

        val metadata3 = FeatureMetadata(
            featureKey = "different_feature",
            defaultValue = "default_value",
            valueType = "String",
            serviceOwner = "test_team"
        )

        assertEquals(metadata1.hashCode(), metadata2.hashCode())
        assertFalse(metadata1.hashCode() == metadata3.hashCode())
    }
} 