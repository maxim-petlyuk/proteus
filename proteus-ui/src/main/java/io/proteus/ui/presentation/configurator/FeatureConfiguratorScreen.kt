package io.proteus.ui.presentation.configurator

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.proteus.ui.presentation.theme.ProteusTheme

@Composable
internal fun FeatureConfiguratorScreen(viewModel: FeatureConfiguratorViewModel) {
    val snackBarHostState = remember { SnackbarHostState() }
    val state: FeatureConfiguratorState by viewModel.screenState.collectAsStateWithLifecycle()

    FeatureConfiguratorScreen(
        state = state,
        snackBarHostState = snackBarHostState,
    )
}

@Composable
internal fun FeatureConfiguratorScreen(
    state: FeatureConfiguratorState,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBack: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            FeatureConfiguratorActionBar(
                isLoading = state.isLoading,
                onBack = onBack
            )
        }
    ) { innerPadding ->

    }
}

@Preview
@Composable
private fun LoadingPreview(
    @PreviewParameter(ConfiguratorLoadingStatePreview::class)
    state: FeatureConfiguratorState
) {
    ProteusTheme {
        FeatureConfiguratorScreen(state = state)
    }
}

@Preview
@Composable
private fun LoadedPreview(
    @PreviewParameter(ConfiguratorLoadedStatePreview::class)
    state: FeatureConfiguratorState
) {
    ProteusTheme {
        FeatureConfiguratorScreen(state = state)
    }
}
