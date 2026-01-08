package io.proteus.core.domain

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DomainModelSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun testConfigValueStringSerializationDeserialization() {
        // Test ConfigValue.Text
        val textValue = ConfigValue.Text("test value")
        val textJson = json.encodeToString(ConfigValue.Text.serializer(), textValue)
        val deserializedText = json.decodeFromString(ConfigValue.Text.serializer(), textJson)
        assertEquals(textValue.value, deserializedText.value)
    }

    @Test
    fun testConfigValueBooleanSerializationDeserialization() {
        // Test ConfigValue.Boolean
        val boolValue = ConfigValue.Boolean(true)
        val boolJson = json.encodeToString(ConfigValue.Boolean.serializer(), boolValue)
        val deserializedBool = json.decodeFromString(ConfigValue.Boolean.serializer(), boolJson)
        assertEquals(boolValue.value, deserializedBool.value)
    }

    @Test
    fun testConfigValueLongSerializationDeserialization() {
        // Test ConfigValue.Long
        val longValue = ConfigValue.Long(12345L)
        val longJson = json.encodeToString(ConfigValue.Long.serializer(), longValue)
        val deserializedLong = json.decodeFromString(ConfigValue.Long.serializer(), longJson)
        assertEquals(longValue.value, deserializedLong.value)
    }

    @Test
    fun testConfigValueDoubleSerializationDeserialization() {
        // Test ConfigValue.Double
        val doubleValue = ConfigValue.Double(123.45)
        val doubleJson = json.encodeToString(ConfigValue.Double.serializer(), doubleValue)
        val deserializedDouble = json.decodeFromString(ConfigValue.Double.serializer(), doubleJson)
        assertEquals(doubleValue.value, deserializedDouble.value)
    }

    @Test
    fun testFeatureMetadataSerializationDeserialization() {
        val metadata = FeatureMetadata(
            featureKey = "test_feature",
            defaultValue = "default_value",
            valueType = "text"
        )

        val jsonString = json.encodeToString(FeatureMetadata.serializer(), metadata)
        val deserialized = json.decodeFromString(FeatureMetadata.serializer(), jsonString)

        assertEquals(metadata.featureKey, deserialized.featureKey)
        assertEquals(metadata.defaultValue, deserialized.defaultValue)
        assertEquals(metadata.valueType, deserialized.valueType)
    }

    @Test
    fun testFeatureMetadataWithSerialNames() {
        // Test that @SerialName annotations work correctly
        val jsonString = """
            {
                "feature_key": "my_feature",
                "default_value": "my_default",
                "value_type": "text"
            }
        """.trimIndent()

        val metadata = json.decodeFromString(FeatureMetadata.serializer(), jsonString)
        assertEquals("my_feature", metadata.featureKey)
        assertEquals("my_default", metadata.defaultValue)
        assertEquals("text", metadata.valueType)

        // Test serialization maintains proper field names
        val serialized = json.encodeToString(FeatureMetadata.serializer(), metadata)
        assertTrue(serialized.contains("\"feature_key\""))
        assertTrue(serialized.contains("\"default_value\""))
        assertTrue(serialized.contains("\"value_type\""))
    }
}