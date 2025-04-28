package io.proteus.core.data

import io.proteus.core.domain.Feature
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class StaticFeatureBookDataSourceTest {

    @Test
    fun `test getFeatureBook returns provided features`() = runBlocking {
        // Given
        val features = listOf(
            Feature("test_string", "default", String::class),
            Feature("test_long", 42L, Long::class),
            Feature("test_boolean", true, Boolean::class)
        )
        val dataSource = StaticFeatureBookDataSource(features)

        // When
        val result = dataSource.getFeatureBook()

        // Then
        assertTrue(result.isSuccess)
        val returnedFeatures = result.getOrNull()
        assertNotNull(returnedFeatures)
        assertEquals(features.size, returnedFeatures.size)
        assertEquals(features, returnedFeatures)
    }
} 