package io.proteus.ui.presentation.configurator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.proteus.core.exceptions.IllegalConfigDataTypeException
import io.proteus.core.provider.ConfigValue
import io.proteus.core.provider.Proteus
import io.proteus.ui.data.FeatureBookRepository
import io.proteus.ui.di.ProteusPresentationInjection
import io.proteus.ui.domain.entity.FeatureNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import io.proteus.ui.presentation.configurator.FeatureConfiguratorState.MockInputType as MockDataType

internal class FeatureConfiguratorViewModel(
    private val featureKey: String,
    private val featureBookRepository: FeatureBookRepository
) : ViewModel() {

    val screenState = MutableStateFlow(FeatureConfiguratorState.idle())

    private val currentState: FeatureConfiguratorState
        get() = screenState.value

    init {
        loadState()
    }

    fun onAction(action: FeatureConfiguratorAction) {
        when (action) {
            is FeatureConfiguratorAction.ChangeBooleanFeatureValue -> {
                processChangeBooleanMockedValue(action.isActivated)
            }

            is FeatureConfiguratorAction.ChangeTextFeatureValue -> {
                processChangeTextMockedValue(action.mockedValue)
            }

            is FeatureConfiguratorAction.ToggleMockConfiguration -> {
                processToggleMockConfiguration(action.isActivated)
            }

            is FeatureConfiguratorAction.SaveChanges -> {
                processSaveChangesAction()
            }
        }
    }

    private fun processChangeBooleanMockedValue(activated: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            currentState.featureNote?.let { featureNote ->
                rebuild {
                    copy(
                        mockInputType = MockDataType.Toggle(activated)
                    )
                }
            }
        }
    }

    private fun processChangeTextMockedValue(mockedValue: String) {
        currentState.featureNote?.let { featureNote ->
            val currentMockedValue = currentState.mockInputType as? MockDataType.TextInput
                ?: MockDataType.TextInput(
                    text = featureNote.localConfigValue?.toString() ?: "",
                    textInputType = featureNote.asInputType()
                )

            rebuildAsync {
                copy(
                    mockInputType = currentMockedValue.copy(text = mockedValue)
                )
            }
        }
    }

    private fun processToggleMockConfiguration(activated: Boolean) {
        rebuildAsync {
            copy(
                isOverrideActivated = activated
            )
        }
    }

    private fun processSaveChangesAction() {
        viewModelScope.launch(Dispatchers.Default) {
            currentState.featureNote?.let { featureNote ->
                val mockInputType = currentState.mockInputType

                if (mockInputType == null) {
                    rebuild {
                        copy(
                            operationState = FeatureConfiguratorState.OperationState.Failure(
                                message = "State is missing mocked config input"
                            )
                        )
                    }
                    return@launch
                }

                rebuild {
                    copy(
                        operationState = FeatureConfiguratorState.OperationState.ProcessingChanges
                    )
                }

                try {
                    if (currentState.isOverrideActivated) {
                        featureBookRepository.saveMockedConfig(
                            feature = featureNote.feature,
                            configValue = mockInputType.asConfigValue()
                        )
                    } else {
                        featureBookRepository.removeMockedConfig(featureNote.feature)
                    }

                    rebuild {
                        copy(
                            operationState = FeatureConfiguratorState.OperationState.Ready
                        )
                    }
                } catch (e: IllegalConfigDataTypeException) {
                    rebuild {
                        copy(
                            operationState = FeatureConfiguratorState.OperationState.Failure(e.message)
                        )
                    }
                }
            }
        }
    }

    private fun loadState() {
        rebuildAsync {
            copy(
                isLoading = true
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            featureBookRepository.getFeatureNote(featureKey)
                .onSuccess {
                    rebuild {
                        copy(
                            featureNote = it,
                            mockInputType = determineMockSetupType(it),
                            isLoading = false,
                            isOverrideActivated = it.isOverrideActivated == true
                        )
                    }
                }.onFailure {
                    rebuild {
                        copy(
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun determineMockSetupType(featureNote: FeatureNote<*>): MockDataType {
        return when (featureNote.feature.valueClass) {
            Boolean::class -> {
                MockDataType.Toggle((featureNote.localConfigValue as? ConfigValue.Boolean)?.value == true)
            }

            Double::class,
            Long::class,
            String::class -> {
                MockDataType.TextInput(
                    text = featureNote.localConfigValue?.toString() ?: "",
                    textInputType = featureNote.asInputType()
                )
            }

            else -> {
                throw IllegalArgumentException("Unsupported data type: ${featureNote.feature.valueClass}")
            }
        }
    }

    private fun rebuildAsync(stateBuilder: FeatureConfiguratorState.() -> FeatureConfiguratorState) {
        screenState.tryEmit(stateBuilder.invoke(screenState.value))
    }

    private suspend fun rebuild(stateBuilder: FeatureConfiguratorState.() -> FeatureConfiguratorState) {
        screenState.emit(stateBuilder.invoke(screenState.value))
    }

    class Factory(val featureKey: String) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FeatureConfiguratorViewModel(
                featureKey = featureKey,
                featureBookRepository = ProteusPresentationInjection.provideFeatureBookRepository(
                    featureBookDataSource = Proteus.getInstance().getFeatureBookDataSource(),
                    remoteConfigProviderFactory = Proteus.getInstance().getRemoteConfigProviderFactory(),
                    mockConfigRepository = Proteus.getInstance().getMockConfigRepository()
                ),
            ) as T
        }
    }
}
