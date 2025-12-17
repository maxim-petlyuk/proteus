package io.proteus.sample.data

data class FeatureFlagState(
    val name: String,
    val key: String,
    val value: String,
    val type: String,
    val owner: String,
    val source: FeatureSource,
    val description: String
)

enum class FeatureSource {
    REMOTE,
    MOCK
}