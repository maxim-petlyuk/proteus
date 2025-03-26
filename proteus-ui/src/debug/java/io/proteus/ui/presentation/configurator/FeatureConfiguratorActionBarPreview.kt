package io.proteus.ui.presentation.configurator

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.proteus.ui.presentation.theme.ProteusTheme

@Preview
@Composable
private fun ActionBarPreview() {
    ProteusTheme {
        FeatureConfiguratorActionBar(
            title = "Feature Editor",
        )
    }
}
