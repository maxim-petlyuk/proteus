package io.proteus.sample.ui.screens.demo.components

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.proteus.sample.data.DemoUiState
import io.proteus.sample.data.FeatureFlagState
import io.proteus.sample.data.FeatureSource

class ScreenLoadedStatePreviewProvider : PreviewParameterProvider<DemoUiState> {

    override val values: Sequence<DemoUiState> = sequenceOf(
        DemoUiState.Success(
            featureFlag = FeatureFlagStatePreviewProvider().values.first()
        )
    )
}

class FeatureFlagStatePreviewProvider : PreviewParameterProvider<FeatureFlagState> {

    override val values: Sequence<FeatureFlagState> = sequenceOf(
        FeatureFlagState(
            name = "AI Assistant Mode",
            key = "ai_assistant_enabled",
            value = "ADVANCED",
            type = "String",
            owner = "Firebase",
            source = FeatureSource.REMOTE,
            description = "Controls the behavior and capabilities of the AI assistant feature in the application."
        ),
        FeatureFlagState(
            name = "AI Assistant Mode",
            key = "ai_assistant_enabled",
            value = "BASIC",
            type = "String",
            owner = "Firebase",
            source = FeatureSource.MOCK,
            description = "Controls the behavior and capabilities of the AI assistant feature in the application."
        ),
        FeatureFlagState(
            name = "Chat History Limit",
            key = "chat_history_limit",
            value = "100",
            type = "Integer",
            owner = "CleverTap",
            source = FeatureSource.REMOTE,
            description = "Maximum number of chat messages stored in local history for performance optimization."
        )
    )
}