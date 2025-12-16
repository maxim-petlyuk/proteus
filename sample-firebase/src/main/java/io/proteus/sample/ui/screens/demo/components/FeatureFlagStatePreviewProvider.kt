package io.proteus.sample.ui.screens.demo.components

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.proteus.sample.data.FeatureSource
import io.proteus.sample.data.FeatureFlagState

class FeatureFlagStatePreviewProvider : PreviewParameterProvider<FeatureFlagState> {

    override val values: Sequence<FeatureFlagState> = sequenceOf(
        FeatureFlagState(
            name = "AI Assistant Mode",
            key = "ai_assistant_enabled",
            value = "ADVANCED",
            type = "String",
            source = FeatureSource.REMOTE,
            lastUpdated = System.currentTimeMillis(),
            description = "Controls the behavior and capabilities of the AI assistant feature in the application."
        ),
        FeatureFlagState(
            name = "AI Assistant Mode",
            key = "ai_assistant_enabled",
            value = "BASIC",
            type = "String",
            source = FeatureSource.MOCK,
            lastUpdated = System.currentTimeMillis(),
            description = "Controls the behavior and capabilities of the AI assistant feature in the application."
        ),
        FeatureFlagState(
            name = "Chat History Limit",
            key = "chat_history_limit",
            value = "100",
            type = "Number",
            source = FeatureSource.REMOTE,
            lastUpdated = System.currentTimeMillis(),
            description = "Maximum number of chat messages stored in local history for performance optimization."
        )
    )
}