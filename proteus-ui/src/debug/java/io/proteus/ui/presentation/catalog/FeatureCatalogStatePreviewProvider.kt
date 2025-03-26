package io.proteus.ui.presentation.catalog

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.proteus.core.domain.ConfigValue
import io.proteus.core.domain.Feature
import io.proteus.ui.domain.entity.FeatureNote

internal class LoadingStatePreviewProvider : PreviewParameterProvider<FeatureCatalogState> {

    override val values: Sequence<FeatureCatalogState> = sequenceOf(
        FeatureCatalogState(isLoading = true)
    )
}

internal class LoadedStatePreviewProvider : PreviewParameterProvider<FeatureCatalogState> {

    override val values: Sequence<FeatureCatalogState> = sequenceOf(
        FeatureCatalogState(
            isLoading = false,
            searchQuery = "feed",
            originalFeatureBook = listOf(
                FeatureNote<String>(
                    feature = Feature<String>(
                        key = "primary_server",
                        defaultValue = "http://example.com",
                        valueClass = String::class,
                        owner = "Firebase"
                    ),
                    remoteConfigValue = "http://google.com",
                    localConfigValue = ConfigValue.Text("http://override.com")
                ),
                FeatureNote<Long>(
                    feature = Feature<Long>(
                        key = "max_group_chat_size",
                        defaultValue = 24,
                        valueClass = Long::class,
                        owner = "Firebase"
                    ),
                    remoteConfigValue = "12"
                ),
                FeatureNote<String>(
                    feature = Feature<String>(
                        key = "secondary_server",
                        defaultValue = "http://example.com",
                        valueClass = String::class,
                        owner = "Firebase"
                    ),
                    remoteConfigValue = "http://google.com"
                ),
                FeatureNote<String>(
                    feature = Feature<String>(
                        key = "primary_domain",
                        defaultValue = "http://example.com",
                        valueClass = String::class,
                        owner = "Firebase"
                    ),
                    remoteConfigValue = "http://google.com"
                ),
                FeatureNote<String>(
                    feature = Feature<String>(
                        key = "optional_domain",
                        defaultValue = "http://example.com",
                        valueClass = String::class,
                        owner = "Firebase"
                    ),
                    remoteConfigValue = "http://google.com"
                ),
                FeatureNote<String>(
                    feature = Feature<String>(
                        key = "optional_domain",
                        defaultValue = "http://example.com",
                        valueClass = String::class,
                        owner = "Firebase"
                    ),
                    remoteConfigValue = "http://google.com"
                )
            )
        )
    )
}
