package core.data

import io.proteus.core.domain.ConfigValue
import org.junit.Test

internal class ConfigValueTest {

    @Test
    fun `2 same double values should be same by equals`() {
        // Given
        val value1 = ConfigValue.Double(1.0)
        val value2 = ConfigValue.Double(1.0)

        // When
        val result = value1 == value2

        // Then
        assert(result) {
            "Expected result is true, but was false"
        }
    }

    @Test
    fun `2 different double values should be different by equals`() {
        // Given
        val value1 = ConfigValue.Double(1.0)
        val value2 = ConfigValue.Double(2.0)

        // When
        val result = value1 == value2

        // Then
        assert(!result) {
            "Expected result is false, but was true"
        }
    }

    @Test
    fun `2 same string values should be same by equals`() {
        // Given
        val value1 = ConfigValue.Text("value")
        val value2 = ConfigValue.Text("value")

        // When
        val result = value1 == value2

        // Then
        assert(result) {
            "Expected result is true, but was false"
        }
    }

    @Test
    fun `2 different string values should be different by equals`() {
        // Given
        val value1 = ConfigValue.Text("value1")
        val value2 = ConfigValue.Text("value2")

        // When
        val result = value1 == value2

        // Then
        assert(!result) {
            "Expected result is false, but was true"
        }
    }

    @Test
    fun `2 same boolean values should be same by equals`() {
        // Given
        val value1 = ConfigValue.Boolean(true)
        val value2 = ConfigValue.Boolean(true)

        // When
        val result = value1 == value2

        // Then
        assert(result) {
            "Expected result is true, but was false"
        }
    }

    @Test
    fun `2 different boolean values should be different by equals`() {
        // Given
        val value1 = ConfigValue.Boolean(true)
        val value2 = ConfigValue.Boolean(false)

        // When
        val result = value1 == value2

        // Then
        assert(!result) {
            "Expected result is false, but was true"
        }
    }

    @Test
    fun `2 same long value should be same by equals`() {
        // Given
        val value1 = ConfigValue.Long(1)
        val value2 = ConfigValue.Long(1)

        // When
        val result = value1 == value2

        // Then
        assert(result) {
            "Expected result is true, but was false"
        }
    }

    @Test
    fun `2 different long value should be different by equals`() {
        // Given
        val value1 = ConfigValue.Long(1)
        val value2 = ConfigValue.Long(2)

        // When
        val result = value1 == value2

        // Then
        assert(!result) {
            "Expected result is false, but was true"
        }
    }

    @Test
    fun `2 same text values should have same hash code`() {
        // Given
        val value1 = ConfigValue.Text("value")
        val value2 = ConfigValue.Text("value")

        // When
        val result = value1.hashCode() == value2.hashCode()

        // Then
        assert(result) {
            "Expected result is true, but was false"
        }
    }

    @Test
    fun `2 different text values should have different hash code`() {
        // Given
        val value1 = ConfigValue.Text("value1")
        val value2 = ConfigValue.Text("value2")

        // When
        val result = value1.hashCode() == value2.hashCode()

        // Then
        assert(!result) {
            "Expected result is false, but was true"
        }
    }

    @Test
    fun `2 same boolean values should have same hash code`() {
        // Given
        val value1 = ConfigValue.Boolean(true)
        val value2 = ConfigValue.Boolean(true)

        // When
        val result = value1.hashCode() == value2.hashCode()

        // Then
        assert(result) {
            "Expected result is true, but was false"
        }
    }

    @Test
    fun `2 different boolean values should have different hash code`() {
        // Given
        val value1 = ConfigValue.Boolean(true)
        val value2 = ConfigValue.Boolean(false)

        // When
        val result = value1.hashCode() == value2.hashCode()

        // Then
        assert(!result) {
            "Expected result is false, but was true"
        }
    }

    @Test
    fun `2 same long values should have same hash code`() {
        // Given
        val value1 = ConfigValue.Long(1)
        val value2 = ConfigValue.Long(1)

        // When
        val result = value1.hashCode() == value2.hashCode()

        // Then
        assert(result) {
            "Expected result is true, but was false"
        }
    }

    @Test
    fun `2 different long values should have different hash code`() {
        // Given
        val value1 = ConfigValue.Long(1)
        val value2 = ConfigValue.Long(2)

        // When
        val result = value1.hashCode() == value2.hashCode()

        // Then
        assert(!result) {
            "Expected result is false, but was true"
        }
    }

    @Test
    fun `2 same double values should have same hash code`() {
        // Given
        val value1 = ConfigValue.Double(1.0)
        val value2 = ConfigValue.Double(1.0)

        // When
        val result = value1.hashCode() == value2.hashCode()

        // Then
        assert(result) {
            "Expected result is true, but was false"
        }
    }

    @Test
    fun `2 different double values should have different hash code`() {
        // Given
        val value1 = ConfigValue.Double(1.0)
        val value2 = ConfigValue.Double(2.0)

        // When
        val result = value1.hashCode() == value2.hashCode()

        // Then
        assert(!result) {
            "Expected result is false, but was true"
        }
    }

    @Test
    fun `text value should have correct string representation`() {
        // Given
        val value = ConfigValue.Text("value")

        // When
        val result = value.toString()

        // Then
        assert(result == "value") {
            "Expected result is [value], but was [$result]"
        }
    }

    @Test
    fun `double value should have correct string representation`() {
        // Given
        val value = ConfigValue.Double(1.0)

        // When
        val result = value.toString()

        // Then
        assert(result == "1.0") {
            "Expected result is [1.0], but was [$result]"
        }
    }

    @Test
    fun `long value should have correct string representation`() {
        // Given
        val value = ConfigValue.Long(1)

        // When
        val result = value.toString()

        // Then
        assert(result == "1") {
            "Expected result is [1], but was [$result]"
        }
    }

    @Test
    fun `boolean value should have correct string representation`() {
        // Given
        val value = ConfigValue.Boolean(true)

        // When
        val result = value.toString()

        // Then
        assert(result == "true") {
            "Expected result is [true], but was [$result]"
        }
    }
}
