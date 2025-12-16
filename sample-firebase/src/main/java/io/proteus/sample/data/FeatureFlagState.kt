package io.proteus.sample.data

/**
 * Represents the state of a feature flag for the demo screen.
 */
data class FeatureFlagState(
    val name: String,
    val key: String,
    val value: String,
    val type: String,
    val source: FeatureSource,
    val lastUpdated: Long,
    val description: String
)

/**
 * Indicates the source of the feature flag value.
 */
enum class FeatureSource {
    /**
     * Value comes from remote configuration (e.g., Firebase Remote Config)
     */
    REMOTE,

    /**
     * Value has been overridden locally for testing/debugging
     */
    MOCK
}