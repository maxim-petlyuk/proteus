package io.proteus.sample.ui.screens.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.proteus.sample.R
import io.proteus.sample.data.FeatureFlagState
import io.proteus.sample.data.FeatureSource
import io.proteus.sample.ui.screens.demo.components.AnimatedBackground
import io.proteus.sample.ui.screens.demo.components.FeatureFlagCard
import io.proteus.sample.ui.theme.SampleConfigTheme

/**
 * Main demo screen showing the feature flag configuration demonstration.
 *
 * This screen serves as the main showcase for the Proteus library capabilities,
 * featuring an elegant dark theme with animations and glassmorphism effects.
 *
 * Structure:
 * - AnimatedBackground: Provides the dark animated background
 * - FeatureFlagCard: Main card showing feature flag information (Phase 3)
 * - ConfiguratorButton: FAB to open the configurator (Phase 6)
 *
 * @param modifier Modifier to be applied to the root Box
 * @param onOpenConfigurator Callback when the configurator button is clicked
 */
@Composable
fun DemoScreen(
    modifier: Modifier = Modifier,
    onOpenConfigurator: () -> Unit = { }
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Layer 1: Animated background (covers entire screen including status bars)
        AnimatedBackground(
            modifier = Modifier.fillMaxSize()
        )

        // Layer 2: Main content with proper insets handling
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FeatureFlagCard(
                state = FeatureFlagState(
                    name = stringResource(R.string.demo_feature_name),
                    key = stringResource(R.string.demo_feature_key),
                    value = stringResource(R.string.demo_feature_value_advanced),
                    type = "String",
                    source = FeatureSource.REMOTE,
                    lastUpdated = System.currentTimeMillis(),
                    description = stringResource(R.string.demo_feature_description)
                )
            )
        }

        // Layer 3: Bottom action button with navigation bar insets
        // TODO: Phase 6 - Add ConfiguratorButton here
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(24.dp)
        ) {
            // Placeholder for ConfiguratorButton - will be added in Phase 6
        }
    }
}

@Preview(
    name = "Demo Screen Dark",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DemoScreenPreview() {
    SampleConfigTheme {
        DemoScreen(
            onOpenConfigurator = { }
        )
    }
}

@Preview(
    name = "Demo Screen Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DemoScreenLightPreview() {
    SampleConfigTheme {
        DemoScreen(
            onOpenConfigurator = { }
        )
    }
}