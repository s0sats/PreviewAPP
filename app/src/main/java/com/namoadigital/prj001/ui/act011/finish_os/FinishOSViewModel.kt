package com.namoadigital.prj001.ui.act011.finish_os

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.extensions.results
import com.namoadigital.prj001.extensions.suspendResults
import com.namoadigital.prj001.model.BaseSerialSearchItem
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishFormBackupMachineList
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishFormField
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishOsData
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FormSaveOs
import com.namoadigital.prj001.ui.act011.finish_os.di.model.NewServiceChoose
import com.namoadigital.prj001.ui.act011.finish_os.di.modules.FinishOsTranslate
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.BackupMachineSearchUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.FinishOSUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.GetFinishOsDataUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ValidateFinishOSUseCase
import com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component.MachinesStatus
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.EditedField
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishState
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishOSViewModel @Inject constructor(
    private val useCase: FinishOSUseCase,
    private val validateUseCase: ValidateFinishOSUseCase,
    @FinishOsTranslate private val finishTranslate: TranslateMap
) : ViewModel() {

    private val _state = MutableStateFlow(FinishState(translateMap = finishTranslate))
    val state = _state.asStateFlow()


    suspend fun saveFinish(
        finishValidation: FinishValidation
    ) {

        useCase.saveOs(
            FormSaveOs(
                formTypeCode = _state.value.formPrimaryKey?.typeCode ?: -1,
                formCode = _state.value.formPrimaryKey?.code ?: -1,
                formVersionCode = _state.value.formPrimaryKey?.versionCode ?: -1,
                formData = _state.value.formPrimaryKey?.formData ?: -1L,
                finishOsData = FinishOsData(
                    infoOs = FinishFormField.ExpectedTimeOS(
                        dateStart = finishValidation.infoOs.dateStart ?: "",
                        dateEnd = finishValidation.infoOs.dateEnd ?: "",
                        elapsedTime = null,
                        isEditDate = false,
                    ),
                    machineOsInitial = when (finishValidation.initialMachineStatus) {
                        is MachinesStatus.NO_STOPPED -> FinishFormField.MachineOSInitial()
                        is MachinesStatus.STOPPED_FOR -> FinishFormField.MachineOSInitial(
                            date = finishValidation.initialMachineStatus.date,
                            responsibleStop = finishValidation.initialMachineStatus.responsibleStop
                        )

                        null -> FinishFormField.MachineOSInitial()
                    },
                    machineOsFinal = FinishFormField.MachineOSFinal(
                        option = finishValidation.finalMachineStopped
                    ),
                    backupMachine = FinishFormField.BackupMachine(
                        hasBackupMachine = finishValidation.backupMachine?.let { true } ?: false,
                        serialCode = finishValidation.backupMachine?.serialCode,
                        serialId = finishValidation.backupMachine?.serialId,
                        productCode = finishValidation.backupMachine?.productCode,
                        productId = finishValidation.backupMachine?.productId,
                        productDesc = finishValidation.backupMachine?.productDesc,
                    ),
                    hasNewService = when (finishValidation.hasNewService) {
                        is NewServiceChoose.FINALIZED -> FinishFormField.HasNewService(
                            option = finishValidation.hasNewService
                        )

                        is NewServiceChoose.PLANNING -> FinishFormField.HasNewService(
                            option = finishValidation.hasNewService
                        )

                        is NewServiceChoose.RETURN -> FinishFormField.HasNewService(option = finishValidation.hasNewService)
                        null -> FinishFormField.HasNewService()
                    }
                )
            )
        ).results(
            success = {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isError = false,
                        saveFinishOS = true
                    )
                }
            },
            failed = {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isError = true
                    )
                }
            },
            loading = { isLoading, message ->
                _state.update {
                    it.copy(
                        isLoading = isLoading,
                        isError = false
                    )
                }
            }
        )

    }

    fun validateForm(
        finishValidation: FinishValidation,
        editedField: EditedField? = null,
        isReadOnly: Boolean = false
    ) {
        viewModelScope.launch {
            validateUseCase(
                input = ValidateFinishOSUseCase.Param(
                    validation = finishValidation,
                    primaryKey = _state.value.formPrimaryKey!!,
                    editedField = editedField,
                    isReadOnly = isReadOnly
                )
            ).results(
                success = { response ->
                    _state.update {
                        it.copy(
                            isValidForm = response,
                        )
                    }
                }
            )
        }
    }

    fun getFinishData(
        typeCode: Int,
        code: Int,
        versionCode: Int,
        formData: Long
    ) {

        viewModelScope.launch {
            useCase.getFinishOsData(
                input = GetFinishOsDataUseCase.Param(
                    formTypeCode = typeCode,
                    formCode = code,
                    formVersionCode = versionCode,
                    formData = formData
                )
            ).suspendResults(
                success = { response ->
                    response?.let {
                        _state.update {
                            it.copy(
                                data = response,
                                isLoading = false,
                                formPrimaryKey = FinishState.FormPrimaryKey(
                                    typeCode = typeCode,
                                    code = code,
                                    versionCode = versionCode,
                                    formData = formData
                                )
                            )
                        }
                    }
                }
            )
        }
    }

    fun getBackupSerial(
        serialId: String?,
        autoSelection: Boolean,
    ) {
        viewModelScope.launch {
            useCase.backupMachineSearch(
                BackupMachineSearchUseCase.Param(
                    serialId = serialId,
                    autoSelection = autoSelection,
                    formPk = state.value.formPrimaryKey!!
                )
            ).suspendResults(
                loading = { isLoading, message ->
                    _state.update {
                        it.copy(
                            backupMachineWSProgress = true
                        )
                    }
                },
                success = { resultado ->
                    if (autoSelection
                        && resultado.backupList?.size == 1
                    ) {
                        setBackupSerial(resultado.backupList!![0])
                    } else {
                        val finishOsData = _state.value.data?.copy(
                            backupMachineListState = resultado
                        )
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isError = false,
                                backupMachineWSProgress = false,
                                data = finishOsData
                            )
                        }
                    }
                },
                failed = {
                    val finishOsData = _state.value.data?.copy(
                        backupMachineListState = FinishFormBackupMachineList(false, null)
                    )
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            backupMachineWSProgress = false,
                            data = finishOsData
                        )
                    }
                }
            )
        }
    }

    fun setBackupSerial(
        backupMachine: BaseSerialSearchItem.BackupMachineSerialItem
    ) {
        viewModelScope.launch {
            _state.update {
                val data = it.data
                it.copy(
                    isLoading = false,
                    isError = false,
                    backupMachineWSProgress = false,
                    data = data?.copy(
                        backupMachineListState = FinishFormBackupMachineList(false, null),
                        backupMachine = data.backupMachine?.copy(
                            hasBackupMachine = true,
                            serialCode = backupMachine.serialCode,
                            serialId = backupMachine.serialId,
                            productCode = backupMachine.productCode,
                            productId = backupMachine.productId,
                            productDesc = backupMachine.productDesc,
                        )?:run {
                            FinishFormField.BackupMachine(
                                hasBackupMachine = true,
                                serialCode = backupMachine.serialCode,
                                serialId = backupMachine.serialId,
                                productCode = backupMachine.productCode,
                                productId = backupMachine.productId,
                                productDesc = backupMachine.productDesc,
                            )
                        }
                    )
                )
            }
        }
    }

    fun clearBackupMachine() {
        viewModelScope.launch {
            _state.update {
                val data = it.data
                it.copy(
                    data = data?.copy(
                        backupMachine = data.backupMachine?.copy(
                            serialCode = null,
                            serialId = null,
                            productCode = null,
                            productId = null,
                            productDesc = null,
                        )
                    )
                )
            }
        }
    }
}