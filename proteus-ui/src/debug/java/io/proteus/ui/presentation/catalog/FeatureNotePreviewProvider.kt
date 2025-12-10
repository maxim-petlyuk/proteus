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
        FeatureNote(
            feature = Feature(
                key = "max_group_chat_size",
                defaultValue = "",
                valueClass = String::class,
            ),
            serviceOwner = "firebase",
            remoteConfigValue = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            localConfigValue = ConfigValue.Text("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
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
