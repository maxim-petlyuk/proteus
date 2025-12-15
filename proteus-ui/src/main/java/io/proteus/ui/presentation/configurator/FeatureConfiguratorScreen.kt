package io.proteus.ui.presentation.configurator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.proteus.ui.R
import io.proteus.ui.domain.entity.FeatureNote
import io.proteus.ui.presentation.catalog.EmptyFeatureState
import io.proteus.ui.presentation.catalog.FeatureCard
import io.proteus.ui.presentation.catalog.UiState
import io.proteus.ui.presentation.configurator.FeatureConfiguratorState.MockInputType
import io.proteus.ui.utils.rememberHapticFeedback
import io.proteus.ui.utils.safeSharedTransition

@Composable
internal fun FeatureConfiguratorScreen(
    viewModel: FeatureConfiguratorViewModel,
    onBack: () -> Unit = {},
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
        onSaveChanges = { viewModel.onAction(FeatureConfiguratorAction.SaveChanges) },
        onResetOverrides = { viewModel.onAction(FeatureConfiguratorAction.ResetOverrides) }
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
    onResetOverrides: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            FeatureConfiguratorActionBar(
                modifier = Modifier.shadow(4.dp),
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
            onResetOverrides = onResetOverrides,
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
    onResetOverrides: () -> Unit = {},
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
                    isSaveButtonEnabled = state.isSaveButtonEnabled,
                    isResetButtonEnabled = state.isResetButtonEnabled,
                    onToggleMockConfig = onToggleMockConfig,
                    onChangeBooleanMockedValue = onChangeBooleanMockedValue,
                    onChangeTextMockedValue = onChangeTextMockedValue,
                    onSaveChanges = onSaveChanges,
                    onResetOverrides = onResetOverrides,
                )
            }

            UiState.Empty -> {
                EmptyFeatureState(
                    modifier = Modifier,
                    message = stringResource(R.string.feature_editor_placeholder_msg)
                )
            }

            UiState.Error -> {
                EmptyFeatureState(
                    modifier = Modifier,
                    message = state.errorMessage ?: stringResource(R.string.feature_editor_placeholder_msg)
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    /* here might be shimmer */
}

@Composable
private fun LoadedContent(
    modifier: Modifier = Modifier,
    featureNote: FeatureNote<*>,
    mockInputType: MockInputType,
    isOverrideActivated: Boolean = true,
    isSaveButtonEnabled: Boolean = false,
    isResetButtonEnabled: Boolean = false,
    onToggleMockConfig: (Boolean) -> Unit = {},
    onChangeBooleanMockedValue: (Boolean) -> Unit = {},
    onChangeTextMockedValue: (String) -> Unit = {},
    onSaveChanges: () -> Unit = {},
    onResetOverrides: () -> Unit = {},
) {
    val haptic = rememberHapticFeedback()
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
                FeatureCard(
                    featureNote = featureNote,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 12.dp, end = 12.dp)
                        .safeSharedTransition(key = "feature-${featureNote.feature.key}")
                )

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

        var showResetDialog by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { showResetDialog = true },
                enabled = isResetButtonEnabled,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = BorderStroke(
                    1.dp,
                    if (isResetButtonEnabled) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.outline
                )
            ) {
                Text(
                    text = "Reset",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            FilledTonalButton(
                onClick = {
                    haptic.success()
                    onSaveChanges()
                },
                enabled = isSaveButtonEnabled,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                elevation = ButtonDefaults.filledTonalButtonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.feature_editor_btn_save),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = {
                    Text(
                        text = "Reset Override",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.feature_editor_reset_confirmation_msg),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showResetDialog = false
                            haptic.success()
                            onResetOverrides()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.feature_editor_btn_reset),
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showResetDialog = false }
                    ) {
                        Text(stringResource(R.string.feature_editor_btn_cancel))
                    }
                }
            )
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
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Switch(
            checked = isActivated,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                checkedBorderColor = MaterialTheme.colorScheme.primary,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline
            )
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
