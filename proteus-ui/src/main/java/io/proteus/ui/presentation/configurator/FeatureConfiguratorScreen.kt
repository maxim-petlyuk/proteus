package io.proteus.ui.presentation.configurator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.proteus.ui.R
import io.proteus.ui.domain.entity.FeatureNote
import io.proteus.ui.presentation.catalog.FeatureCard
import io.proteus.ui.presentation.catalog.UiState
import io.proteus.ui.presentation.configurator.FeatureConfiguratorState.MockInputType
import io.proteus.ui.presentation.theme.ProteusTheme

@Composable
internal fun FeatureConfiguratorScreen(
    viewModel: FeatureConfiguratorViewModel,
    onBack: () -> Unit = {}
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val state: FeatureConfiguratorState by viewModel.screenState.collectAsStateWithLifecycle()

    OperationStateHandler(
        operationState = state.operationState,
        snackBarHostState = snackBarHostState,
        onConsumeFailureMessage = { viewModel.onAction(FeatureConfiguratorAction.ConsumeFailureMessage) },
        onBack = onBack
    )

    FeatureConfiguratorScreen(
        state = state,
        onBack = onBack,
        snackBarHostState = snackBarHostState,
        onChangeBooleanMockedValue = { viewModel.onAction(FeatureConfiguratorAction.ChangeBooleanFeatureValue(it)) },
        onChangeTextMockedValue = { viewModel.onAction(FeatureConfiguratorAction.ChangeTextFeatureValue(it)) },
        onToggleMockConfig = { viewModel.onAction(FeatureConfiguratorAction.ToggleMockConfiguration(it)) },
        onSaveChanges = { viewModel.onAction(FeatureConfiguratorAction.SaveChanges) }
    )
}

@Composable
private fun OperationStateHandler(
    operationState: FeatureConfiguratorState.OperationState,
    onBack: () -> Unit = {},
    onConsumeFailureMessage: () -> Unit = {},
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    LaunchedEffect(operationState) {
        when (operationState) {
            is FeatureConfiguratorState.OperationState.Ready -> {
                onBack.invoke()
            }

            is FeatureConfiguratorState.OperationState.Failure -> {
                snackBarHostState.showSnackbar(operationState.message)
                onConsumeFailureMessage.invoke()
            }

            else -> {
                /* no op */
            }
        }
    }
}

@Composable
internal fun FeatureConfiguratorScreen(
    state: FeatureConfiguratorState,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBack: () -> Unit = {},
    onToggleMockConfig: (Boolean) -> Unit = {},
    onChangeBooleanMockedValue: (Boolean) -> Unit = {},
    onChangeTextMockedValue: (String) -> Unit = {},
    onSaveChanges: () -> Unit = {},
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
        ContentSwitcher(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onChangeBooleanMockedValue = onChangeBooleanMockedValue,
            onChangeTextMockedValue = onChangeTextMockedValue,
            onToggleMockConfig = onToggleMockConfig,
            onSaveChanges = onSaveChanges,
        )
    }
}

@Composable
private fun ContentSwitcher(
    modifier: Modifier = Modifier,
    state: FeatureConfiguratorState,
    onToggleMockConfig: (Boolean) -> Unit = {},
    onChangeBooleanMockedValue: (Boolean) -> Unit = {},
    onChangeTextMockedValue: (String) -> Unit = {},
    onSaveChanges: () -> Unit = {},
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
                    featureNote = state.featureNote!!,
                    mockInputType = state.mockInputType!!,
                    isOverrideActivated = state.isOverrideActivated,
                    onToggleMockConfig = onToggleMockConfig,
                    onChangeBooleanMockedValue = onChangeBooleanMockedValue,
                    onChangeTextMockedValue = onChangeTextMockedValue,
                    onSaveChanges = onSaveChanges,
                )
            }

            UiState.Error -> {
                ErrorContent(
                    modifier = Modifier,
                    message = state.errorMessage
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(
    modifier: Modifier = Modifier,
    message: String?
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
    featureNote: FeatureNote<*>,
    mockInputType: MockInputType,
    isOverrideActivated: Boolean = true,
    onToggleMockConfig: (Boolean) -> Unit = {},
    onChangeBooleanMockedValue: (Boolean) -> Unit = {},
    onChangeTextMockedValue: (String) -> Unit = {},
    onSaveChanges: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .animateContentSize()
            ) {
                FeatureCard(featureNote = featureNote)

                MockFeatureActivationToggle(
                    isOverrideActivated = isOverrideActivated,
                    onToggle = onToggleMockConfig
                )

                if (isOverrideActivated) {
                    MockSetupLayout(
                        modifier = Modifier,
                        mockInputType = mockInputType,
                        onChangeBooleanMockedValue = onChangeBooleanMockedValue,
                        onChangeTextMockedValue = onChangeTextMockedValue,
                    )
                }
            }
        }

        FilledTonalButton(
            onClick = onSaveChanges,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Save")
        }
    }
}

@Composable
private fun MockFeatureActivationToggle(
    modifier: Modifier = Modifier,
    isOverrideActivated: Boolean = false,
    onToggle: (Boolean) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = R.string.feature_editor_activate_mock_label),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        ToggleInput(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            isActivated = isOverrideActivated,
            onToggle = onToggle,
            text = stringResource(id = R.string.feature_editor_activate_mock_status)
        )

        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.5f)
        )
    }
}

@Composable
private fun MockSetupLayout(
    modifier: Modifier = Modifier,
    mockInputType: MockInputType,
    onChangeBooleanMockedValue: (Boolean) -> Unit = {},
    onChangeTextMockedValue: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = R.string.feature_editor_replace_label),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        when (mockInputType) {
            is MockInputType.Toggle -> {
                ToggleInput(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    isActivated = mockInputType.isActivated,
                    onToggle = onChangeBooleanMockedValue,
                    text = stringResource(id = R.string.feature_editor_activated_label)
                )
            }

            is MockInputType.TextInput -> {
                TextInput(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text = mockInputType.text,
                    keyboardType = mockInputType.keyboardType,
                    onTextChanged = onChangeTextMockedValue
                )
            }
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.5f)
        )
    }
}

@Composable
private fun ToggleInput(
    modifier: Modifier = Modifier,
    isActivated: Boolean = false,
    onToggle: (Boolean) -> Unit = {},
    text: String = ""
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Switch(
            modifier = Modifier.align(Alignment.CenterVertically),
            checked = isActivated,
            onCheckedChange = {
                onToggle.invoke(it)
            }
        )
    }
}

@Composable
private fun TextInput(
    modifier: Modifier = Modifier,
    text: String,
    keyboardType: KeyboardType,
    enabled: Boolean = true,
    onTextChanged: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = text,
        onValueChange = {
            onTextChanged(it)
        },
        modifier = modifier
            .fillMaxWidth(),
        singleLine = true,
        textStyle = MaterialTheme.typography.titleSmall,
        enabled = enabled,
        readOnly = !enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
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
private fun TextFeaturePreview(
    @PreviewParameter(TextFeatureConfiguratorStatePreview::class)
    state: FeatureConfiguratorState
) {
    ProteusTheme {
        FeatureConfiguratorScreen(state = state)
    }
}

@Preview
@Composable
private fun DoubleFeaturePreview(
    @PreviewParameter(DecimalFeatureConfiguratorStatePreview::class)
    state: FeatureConfiguratorState
) {
    ProteusTheme {
        FeatureConfiguratorScreen(state = state)
    }
}

@Preview
@Composable
private fun LongFeaturePreview(
    @PreviewParameter(LongFeatureConfiguratorStatePreview::class)
    state: FeatureConfiguratorState
) {
    ProteusTheme {
        FeatureConfiguratorScreen(state = state)
    }
}

@Preview
@Composable
private fun BooleanFeaturePreview(
    @PreviewParameter(BooleanFeatureConfiguratorStatePreview::class)
    state: FeatureConfiguratorState
) {
    ProteusTheme {
        FeatureConfiguratorScreen(state = state)
    }
}
