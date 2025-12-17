package io.proteus.sample.ui.screens.demo

import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import io.proteus.core.data.MockConfigRepository
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory
import io.proteus.sample.data.FeatureFlagState
import io.proteus.sample.ui.screens.demo.components.AnimatedBackground
import io.proteus.sample.ui.screens.demo.components.ConfiguratorButton
import io.proteus.sample.ui.screens.demo.components.FeatureFlagCard
import io.proteus.sample.ui.screens.demo.components.FeatureFlagStatePreviewProvider
import io.proteus.sample.ui.screens.demo.components.fadingEdgesWithShader
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
 * - ConfiguratorButton: Button to open the configurator (Phase 6)
 *
 * @param featureConfigProvider Provider for feature flag values (optional for previews)
 * @param modifier Modifier to be applied to the root Box
 * @param onOpenConfigurator Callback when the configurator button is clicked
 */

@Composable
fun DemoScreen(
    modifier: Modifier = Modifier,
    provider: FeatureConfigProvider,
    remoteConfigProviderFactory: FeatureConfigProviderFactory,
    mockConfigRepository: MockConfigRepository,
    onOpenConfigurator: () -> Unit = { }
) {
    val factory = remember(provider, remoteConfigProviderFactory, mockConfigRepository) {
        DemoScreenViewModelFactory(provider, remoteConfigProviderFactory, mockConfigRepository)
    }

    DemoScreen(
        modifier = modifier,
        factory = factory,
        onOpenConfigurator = onOpenConfigurator
    )
}

@Composable
fun DemoScreen(
    modifier: Modifier = Modifier,
    factory: DemoScreenViewModelFactory,
    viewModel: DemoScreenViewModel = viewModel(factory = factory),
    onOpenConfigurator: () -> Unit = { }
) {
    val state by viewModel.featureFlagState.collectAsState()

    LifecycleResumeEffect(Unit) {
        viewModel.refreshFeatureFlagState()
        onPauseOrDispose { }
    }

    DemoScreen(
        modifier = modifier,
        state = state,
        onOpenConfigurator = onOpenConfigurator
    )
}

@Composable
fun DemoScreen(
    modifier: Modifier = Modifier,
    state: FeatureFlagState,
    onOpenConfigurator: () -> Unit = { }
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AnimatedBackground(
            scrollState = scrollState,
            modifier = Modifier.fillMaxSize()
        )

        LookaheadScope {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .fadingEdgesWithShader(scrollState)
                        .verticalScroll(scrollState)
                        .animateContentSize()
                ) {
                    FeatureFlagCard(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .animateBounds(
                                lookaheadScope = this@LookaheadScope,
                                boundsTransform = { _, _ ->
                                    spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                                }
                            ),
                        state = state
                    )
                }

                ConfiguratorButton(
                    onClick = onOpenConfigurator,
                    showTooltip = true,
                )
            }
        }
    }
}

@Preview(name = "Demo Screen Dark", showBackground = true, showSystemUi = true)
@Composable
private fun DemoScreenPreview(
    @PreviewParameter(FeatureFlagStatePreviewProvider::class)
    state: FeatureFlagState
) {
    SampleConfigTheme {
        DemoScreen(state = state)
    }
}

@Preview(name = "Demo Screen Light", showBackground = true, showSystemUi = true)
@Composable
private fun DemoScreenLightPreview(
    @PreviewParameter(FeatureFlagStatePreviewProvider::class)
    state: FeatureFlagState
) {
    SampleConfigTheme {
        DemoScreen(state = state)
    }
}