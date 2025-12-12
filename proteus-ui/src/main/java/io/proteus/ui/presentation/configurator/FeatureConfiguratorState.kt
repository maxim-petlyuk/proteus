package io.proteus.ui.presentation.configurator

import io.proteus.ui.domain.entity.FeatureNote
import io.proteus.ui.presentation.catalog.UiState

internal data class FeatureConfiguratorState(
    val isLoading: Boolean = false,
    val featureNote: FeatureNote<*>? = null,
    val mockInputType: MockInputType? = null,
    val originalMockInputType: MockInputType? = null,
    val errorMessage: String? = null,
    val isOverrideActivated: Boolean = false,
    val originalIsOverrideActivated: Boolean = false,
    val operationState: OperationState = OperationState.Idle
) {

    val uiState: UiState
        get() = when {
            isLoading -> UiState.Loading
            errorMessage != null || featureNote == null -> UiState.Error
            else -> UiState.Loaded
        }

    val hasUnsavedChanges: Boolean
        get() {
            if (isLoading || featureNote == null || mockInputType == null || originalMockInputType == null) {
                return false
            }

            return isOverrideActivated != originalIsOverrideActivated ||
                   (isOverrideActivated && mockInputType != originalMockInputType)
        }

    val isSaveButtonEnabled: Boolean
        get() = hasUnsavedChanges && operationState !is OperationState.ProcessingChanges

    sealed class OperationState {

        data object Idle : OperationState()

        data object ProcessingChanges : OperationState()

        data object Ready : OperationState()

        data class Failure(val message: String) : OperationState()
    }

    sealed class MockInputType {

        data class Toggle(val isActivated: Boolean) : MockInputType()

        data class TextInput(
            val text: String,
            val textInputType: TextType
        ) : MockInputType() {

            val keyboardType = textInputType.asKeyboardType()
        }
    }

    enum class TextType {
        INTEGER,
        DECIMAL,
        RAW
    }

    companion object {

        fun idle(): FeatureConfiguratorState {
            return FeatureConfiguratorState()
        }
    }
}