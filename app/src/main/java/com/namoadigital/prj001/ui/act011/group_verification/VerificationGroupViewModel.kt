package com.namoadigital.prj001.ui.act011.group_verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.masterdata.ge_os.ProcessVg
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.ERROR_SAVE_SWITCH_LBL
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.LOADING_UPDATE_INSPECTION_LIST_LBL
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.TOAST_ERROR_UPDATE_INSPECTION_LIST
import com.namoadigital.prj001.ui.act011.group_verification.domain.model.VerificationGroupEvent
import com.namoadigital.prj001.ui.act011.group_verification.domain.model.VerificationGroupState
import com.namoadigital.prj001.ui.act011.group_verification.domain.usecase.MapToVerificationGroupsUseCase
import com.namoadigital.prj001.ui.act011.group_verification.domain.usecase.MapToVerificationGroupsUseCase.Input
import com.namoadigital.prj001.ui.act011.group_verification.domain.usecase.UpdateGroupActiveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationGroupViewModel @Inject constructor(
    private val mapToVerificationGroups: MapToVerificationGroupsUseCase,
    private val updateGroupActiveUseCase: UpdateGroupActiveUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(VerificationGroupState())
    val state = _state.asStateFlow()

    private val _snackbarMessages = MutableSharedFlow<String>()
    val snackbarMessages = _snackbarMessages.asSharedFlow()

    fun onEvent(event: VerificationGroupEvent) {
        when (event) {
            is VerificationGroupEvent.OnHandleListVerificationGroup -> {
                handleListVerificationGroup(
                    processType = event.processType,
                    formPks = event.formPKs,
                    hasProcessVg = event.hasProcessVg,
                    isReadOnly = event.isReadOnly
                )
            }

            is VerificationGroupEvent.onGroupSwitchChange -> {
                onGroupSwitchChange(event.isContinousForm, event.vgCode, event.isChecked)
            }

            is VerificationGroupEvent.OnRetry -> {
                handleListVerificationGroup()
            }

            is VerificationGroupEvent.OnUpdateScreens -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            updateScreens = false,
                            stateLoading = it.disableLoading
                        )
                    }

                    if (event.isFailed == true) {
                        showSnackbar(TOAST_ERROR_UPDATE_INSPECTION_LIST)
                    }
                }
            }

        }
    }

    private fun onGroupSwitchChange(isContinuousForm: Boolean, vgCode: Int, switchState: Boolean) {
        val uiState = _state.value
        viewModelScope.launch {
            updateGroupActiveUseCase(
                UpdateGroupActiveUseCase.Input(
                    customerCode = uiState.formPK.customerCode,
                    customFormType = uiState.formPK.customFormType,
                    customFormCode = uiState.formPK.customFormCode,
                    customFormVersion = uiState.formPK.customFormVersion,
                    customFormData = uiState.formPK.customFormData,
                    productCode = uiState.formPK.productCode,
                    serialCode = uiState.formPK.serialCode,
                    isContinuousForm = isContinuousForm,
                    vgCode = vgCode,
                    isActive = switchState
                )
            ).collect {

                it.watchStatus(
                    success = { item ->
                        _state.update {
                            it.copy(
                                listGroups = it.listGroups.map { map ->
                                    if (map.vgCode == vgCode) {
                                        map.copy(isActive = item.isActive())
                                    } else {
                                        map
                                    }
                                },
                                updateScreens = true,
                                stateLoading = it.stateLoading.copy(
                                    message = LOADING_UPDATE_INSPECTION_LIST_LBL
                                )
                            )
                        }
                    },
                    loading = { _, message ->
                        _state.update {
                            it.copy(
                                stateLoading = it.stateLoading.copy(
                                    isLoading = true,
                                    message = message
                                ),
                            )
                        }
                    },
                    failed = { throwable ->

                        _state.update { currentState ->
                            currentState.copy(
                                listGroups = currentState.listGroups.map { map ->
                                    if (map.vgCode == vgCode) {
                                        map.copy(isActive = !switchState)
                                    } else {
                                        map
                                    }
                                },
                                stateLoading = currentState.disableLoading
                            )
                        }
                        showSnackbar(ERROR_SAVE_SWITCH_LBL)
                    }
                )
            }
        }

    }

    private fun handleListVerificationGroup(
        processType: String = _state.value.processType,
        formPks: VerificationGroupState.FormPK = _state.value.formPK,
        hasProcessVg: ProcessVg? = _state.value.hasProcessVg,
        isReadOnly: Boolean = _state.value.isReadOnly
    ) {
        viewModelScope.launch {
            mapToVerificationGroups(
                Input(
                    formPks,
                    isReadOnly,
                    hasProcessVg,
                    processType
                )
            ).collect { result ->
                result.watchStatus(
                    success = { groups ->
                        _state.update {
                            it.copy(
                                listGroups = groups,
                                stateLoading = it.disableLoading,
                                error = null,
                                formPK = formPks,
                                hasProcessVg = hasProcessVg,
                                processType = processType
                            )
                        }
                    },
                    loading = { _, _ ->
                        _state.update {
                            it.copy(
                                stateLoading = it.enableLoading,
                                error = null,
                            )
                        }
                    },
                    failed = { failed ->
                        _state.update {
                            it.copy(
                                stateLoading = it.disableLoading,
                                error = failed.message
                            )
                        }
                    }
                )
            }
        }
    }


    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarMessages.emit(message)
        }
    }
}
