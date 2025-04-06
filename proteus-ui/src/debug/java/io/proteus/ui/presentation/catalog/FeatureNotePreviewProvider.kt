package io.proteus.ui.presentation.catalog

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.proteus.core.domain.ConfigValue
import io.proteus.core.domain.Feature
import io.proteus.ui.domain.entity.FeatureNote

internal class StringFeatureNotePreviewProvider : PreviewParameterProvider<FeatureNote<*>> {

    override val values: Sequence<FeatureNote<*>> = sequenceOf(
        FeatureNote<String>(
            feature = Feature<String>(
                key = "primary_server",
                defaultValue = "http://example.com",
                valueClass = String::class,
            ),
            serviceOwner = "firebase",
            remoteConfigValue = "http://google.com"
        )
    )
}

internal class LongFeatureNotePreviewProvider : PreviewParameterProvider<FeatureNote<*>> {

    override val values: Sequence<FeatureNote<*>> = sequenceOf(
        FeatureNote<Long>(
            feature = Feature<Long>(
                key = "max_group_chat_size",
                defaultValue = 24,
                valueClass = Long::class,
            ),
            serviceOwner = "firebase",
            remoteConfigValue = "12"
        )
    )
}

internal class FeatureBookPreviewProvider : PreviewParameterProvider<List<FeatureNote<*>>> {

    override val values: Sequence<List<FeatureNote<*>>> = sequenceOf(
        listOf(
            FeatureNote<String>(
                feature = Feature<String>(
                    key = "primary_server",
                    defaultValue = "http://example.com",
                    valueClass = String::class,
                ),
                serviceOwner = "firebase",
                remoteConfigValue = "http://google.com",
                localConfigValue = ConfigValue.Text("http://override.com")
            ),
            FeatureNote<Long>(
                feature = Feature<Long>(
                    key = "max_group_chat_size",
                    defaultValue = 24,
                    valueClass = Long::class,
                ),
                serviceOwner = "firebase",
                remoteConfigValue = "12"
            ),
            FeatureNote<String>(
                feature = Feature<String>(
                    key = "secondary_server",
                    defaultValue = "http://example.com",
                    valueClass = String::class,
                ),
                serviceOwner = "firebase",
                remoteConfigValue = "http://google.com"
            ),
            FeatureNote<String>(
                feature = Feature<String>(
                    key = "primary_domain",
                    defaultValue = "http://example.com",
                    valueClass = String::class,
                ),
                serviceOwner = "firebase",
                remoteConfigValue = "http://google.com"
            ),
            FeatureNote<String>(
                feature = Feature<String>(
                    key = "optional_domain",
                    defaultValue = "http://example.com",
                    valueClass = String::class,
                ),
                serviceOwner = "firebase",
                remoteConfigValue = "http://google.com"
            ),
            FeatureNote<String>(
                feature = Feature<String>(
                    key = "optional_domain",
                    defaultValue = "http://example.com",
                    valueClass = String::class,
                ),
                serviceOwner = "firebase",
                remoteConfigValue = "http://google.com"
            )
        )
    )
}
