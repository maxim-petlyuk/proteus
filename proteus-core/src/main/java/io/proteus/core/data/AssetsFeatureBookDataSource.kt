package io.proteus.core.data

import android.content.Context
import io.proteus.core.domain.FeatureContext
import io.proteus.core.domain.FeatureMetadata
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

class AssetsFeatureBookDataSource(
    private val context: Context,
    private val jsonFilePath: String = "featurebook.json",
    private val featureMetadataMapper: FeatureMetadataMapper = FeatureMetadataMapper()
) : FeatureBookDataSource {

    override suspend fun getFeatureBook(): Result<List<FeatureContext<*>>> {
        return runCatching {
            val rawJson = readJsonFromAssets(context, jsonFilePath)
            val assetFeatures: List<FeatureMetadata> = Json.decodeFromString(rawJson)
            assetFeatures.map { featureMetadataMapper.toFeatureContext(it) }
        }
    }

    private fun readJsonFromAssets(context: Context, fileName: String): String {
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        return bufferedReader.use { it.readText() }
    }
}
