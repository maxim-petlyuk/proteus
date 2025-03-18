package io.proteus.ui.presentation.catalog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter

@Composable
internal fun FeatureCatalogScreen(viewModel: FeatureCatalogViewModel) {
}

@Composable
internal fun FeatureCatalogScreen(
    state: FeatureCatalogState,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBack: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            SearchActionBar(
                searchQuery = state.searchQuery,
                onBack = onBack,
                isLoading = state.isLoading
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) {
        ContentSwitcher(
            modifier = Modifier.padding(it),
            state = state,
        )
    }
}

@Composable
private fun ContentSwitcher(
    modifier: Modifier = Modifier,
    state: FeatureCatalogState,
) {
    AnimatedContent(
        targetState = state.uiState,
        transitionSpec = {
            fadeIn(animationSpec = tween(700)) togetherWith fadeOut(animationSpec = tween(500))
        },
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) { targetState ->
        when (targetState) {
            FeatureCatalogState.UiState.Loading -> {
                LoadingContent(
                    modifier = modifier
                )
            }

            FeatureCatalogState.UiState.Loaded -> {
                LoadedContent(
                    modifier = modifier,
                    state = state,
                )
            }

            FeatureCatalogState.UiState.Error -> {
                ErrorContent(
                    modifier = modifier,
                    message = state.errorMessage!!
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(
    modifier: Modifier = Modifier,
    message: String
) {

}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {

}

@Composable
private fun LoadedContent(
    modifier: Modifier = Modifier,
    state: FeatureCatalogState,
) {

}

@Preview
@Composable
private fun LoadingStatePreview(
    @PreviewParameter(LoadingStatePreviewProvider::class)
    state: FeatureCatalogState
) {
    FeatureCatalogScreen(
        state = state
    )
}
