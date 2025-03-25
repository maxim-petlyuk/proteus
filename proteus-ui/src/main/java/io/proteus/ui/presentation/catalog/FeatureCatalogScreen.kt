package io.proteus.ui.presentation.catalog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.proteus.ui.domain.entity.FeatureNote
import io.proteus.ui.presentation.theme.ProteusTheme

@Composable
internal fun FeatureCatalogScreen(
    viewModel: FeatureCatalogViewModel,
    onBack: () -> Unit = {},
    openFeatureConfigurator: (String) -> Unit = {},
    forceRefresh: Boolean = false
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val state: FeatureCatalogState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(forceRefresh) {
        if (forceRefresh) {
            viewModel.onAction(FeatureCatalogAction.RefreshCatalog)
        }
    }

    FeatureCatalogScreen(
        state = state,
        snackBarHostState = snackBarHostState,
        onBack = onBack,
        openFeatureConfigurator = openFeatureConfigurator,
        onQueryChanged = { viewModel.onAction(FeatureCatalogAction.QueryChanged(it)) },
        onClearSearch = { viewModel.onAction(FeatureCatalogAction.QueryChanged("")) },
    )
}

@Composable
internal fun FeatureCatalogScreen(
    state: FeatureCatalogState,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBack: () -> Unit = {},
    onQueryChanged: (String) -> Unit = {},
    onClearSearch: () -> Unit = {},
    openFeatureConfigurator: (String) -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            SearchActionBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                searchQuery = state.searchQuery,
                onBack = onBack,
                isLoading = state.isLoading,
                onQueryChanged = onQueryChanged,
                onClearSearch = onClearSearch,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) {
        ContentSwitcher(
            modifier = Modifier.padding(it),
            state = state,
            openFeatureConfigurator = openFeatureConfigurator
        )
    }
}

@Composable
private fun ContentSwitcher(
    modifier: Modifier = Modifier,
    state: FeatureCatalogState,
    openFeatureConfigurator: (String) -> Unit = {},
) {
    AnimatedContent(
        targetState = state.uiState,
        transitionSpec = {
            fadeIn(animationSpec = tween(700)) togetherWith fadeOut(animationSpec = tween(500))
        },
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) { targetState ->
        when (targetState) {
            UiState.Loading -> {
                LoadingContent(
                    modifier = Modifier
                )
            }

            UiState.Loaded -> {
                LoadedContent(
                    modifier = Modifier,
                    state = state,
                    onFeatureNoteClick = { openFeatureConfigurator(it.feature.key) }
                )
            }

            UiState.Error -> {
                ErrorContent(
                    modifier = Modifier,
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
    onFeatureNoteClick: (FeatureNote<*>) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = WindowInsets.navigationBars.asPaddingValues()
    ) {
        featureCatalog(
            featureBook = state.featureBook,
            onFeatureNoteClick = onFeatureNoteClick
        )
    }
}

@Preview
@Composable
private fun LoadingStatePreview(
    @PreviewParameter(LoadingStatePreviewProvider::class)
    state: FeatureCatalogState
) {
    ProteusTheme {
        FeatureCatalogScreen(
            state = state
        )
    }
}

@Preview
@Composable
private fun LoadedStatePreview(
    @PreviewParameter(LoadedStatePreviewProvider::class)
    state: FeatureCatalogState
) {
    ProteusTheme {
        FeatureCatalogScreen(
            state = state
        )
    }
}
