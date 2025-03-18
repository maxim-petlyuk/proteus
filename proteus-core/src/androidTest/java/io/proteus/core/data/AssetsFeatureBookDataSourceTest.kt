package io.proteus.core.data

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class AssetsFeatureBookDataSourceTest {

    @Test
    fun readFeatureBookFromAssets() {
        runTest {
            // Given
            val dataSource = AssetsFeatureBookDataSource(
                context = ApplicationProvider.getApplicationContext(),
                jsonFilePath = "featurebook.json",
                featureMetadataMapper = FeatureMetadataMapper()
            )

            // When
            val result = dataSource.getFeatureBook()
            val featureBook = result.getOrNull()

            // Then
            assert(result.isSuccess) {
                "Expected features size: 8,\n" +
                    "Actual features size: ${featureBook?.size}"
            }
        }
    }
}