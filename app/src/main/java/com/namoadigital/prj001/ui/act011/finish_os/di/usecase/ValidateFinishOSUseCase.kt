package com.namoadigital.prj001.ui.act011.finish_os.di.usecase

import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.date.isDateBefore
import com.namoadigital.prj001.extensions.date.isDateBeforeOrEquals
import com.namoadigital.prj001.extensions.date.isDateEquals
import com.namoadigital.prj001.extensions.suspendResults
import com.namoadigital.prj001.model.MeMeasureTp
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.ui.act011.finish_os.di.model.NewServiceChoose
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os.GetGeOsByIdUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.measure.GetMeasureTpByCode
import com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component.MachinesStatus
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishState
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation.Component
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation.ComponentError
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.ValidateResult
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ValidateFinishOSUseCase constructor(
    private val getGeOsByIdUseCase: GetGeOsByIdUseCase,
    private val getMeasureTpByCode: GetMeasureTpByCode,
) : UseCases<ValidateFinishOSUseCase.Param, ValidateResult> {

    data class Param(
        val validation: FinishValidation,
        val primaryKey: FinishState.FormPrimaryKey
    )

    override suspend fun invoke(input: Param): Flow<IResult<ValidateResult>> {
        return flow {
            with(input.validation) {
                val map = mutableMapOf<Component, ComponentError>()

                getGeOsByIdUseCase.invoke(
                    GetGeOsByIdUseCase.Param(
                        formType = input.primaryKey.typeCode,
                        formCode = input.primaryKey.code,
                        formVersion = input.primaryKey.versionCode,
                        formData = input.primaryKey.formData,
                    )
                ).suspendResults(
                    success = { geOs ->
                        getMeasureTpByCode.invoke(geOs?.measure_tp_code ?: -1)
                            .suspendResults(
                                success = { measureTp ->
                                    //Valida o componente InitialMachineComponent
                                    validateComponentInitialMachine(map)

                                    //Valida o componente InfoOSComponent
                                    validateBackupMachine(map, input.validation.backupMachine)

                                    //Valida o componente InfoOSComponent
                                    validateComponentInfoOS(map, geOs, measureTp, input)

                                    //Valida o componente AfterMachineComponent
                                    validateAfterMachineComponent(map)

                                    emit(success(ValidateResult(map.isEmpty(), map)))
                                }
                            )
                    }
                )

            }
        }
    }

    private fun FinishValidation.validateBackupMachine(
        map: MutableMap<Component, ComponentError>,
        backupMachine: FinishValidation.BackupMachine?
    ) {
        backupMachine?.let {
            if (it.hasBackupMachine && it.serialId == null && it.productCode == null) {
                map[Component.BackupMachine] = Component.BackupMachine.BackupMachineEmpty
            }
        }
    }

    private fun FinishValidation.validateAfterMachineComponent(map: MutableMap<Component, ComponentError>) {
        if (validAfterMachineStopped) {

            when {
                finalMachineStopped == null && hasNewService == null -> {
                    map[Component.AfterMachineStopped] = Component.Empty
                    map[Component.ScheduleReturnForm] = Component.Empty
                }

                finalMachineStopped != null && hasNewService == null -> {
                    map[Component.ScheduleReturnForm] = Component.Empty
                }

                finalMachineStopped == null && hasNewService != null -> {
                    map[Component.AfterMachineStopped] = Component.Empty
                }

                else -> {
                    hasNewServiceValidation(hasNewService, map)
                }
            }

        } else {
            if (hasNewService != null) {
                hasNewServiceValidation(hasNewService, map)
            } else {
                map[Component.ScheduleReturnForm] = Component.Empty
            }
        }
    }

    private fun FinishValidation.validateComponentInfoOS(
        map: MutableMap<Component, ComponentError>,
        geOs: GeOs?,
        measureTp: MeMeasureTp?,
        input: Param
    ) {
        if (infoOs.dateStart != null && infoOs.dateEnd != null) {

            when {
                isDateBefore(infoOs.dateEnd, infoOs.dateStart) -> {
                    map[Component.InfoOS] = Component.InfoOS.InvalidBothDate
                }
                //
                isDateBefore(
                    ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"),
                    infoOs.dateEnd
                ) -> {
                    map[Component.InfoOS] = Component.InfoOS.InvalidEndDate
                }
                //
                isDateBefore(
                    ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"),
                    infoOs.dateStart
                ) -> {
                    map[Component.InfoOS] = Component.InfoOS.InvalidStartDate
                }
                //
                partialExecutionOS == null && geOs?.measure_value != null -> {

                    val validationMeasure = measureTp?.isMeasureRestrictionInvalid(
                        bypassMinValidation = geOs.allowFormInThePast == 1,
                        measureValue = geOs.measure_value?.toDouble() ?: -1.0,
                        lastMeasureValue = geOs.last_measure_value?.toDouble() ?: -1.0,
                        lastMeasureDate = geOs.last_measure_date,
                        measureDate = input.validation.infoOs.dateStart,
                    ) ?: false

                    if (validationMeasure) {
                        map[Component.InfoOS] =
                            Component.InfoOS.DateExceededLastMeasureDate
                    }
                }
                //
                partialExecutionOS != null && isDateBeforeOrEquals(
                    infoOs.dateStart,
                    partialExecutionOS
                ) -> {
                    map[Component.InfoOS] = Component.InfoOS.PartialExecutionOS
                }
                //
                else -> {}
            }

        } else {
            map[Component.InfoOS] = Component.Empty
        }
    }

    private fun FinishValidation.validateComponentInitialMachine(map: MutableMap<Component, ComponentError>) {
        if (initialMachineStatus != null) {
            when (val status = initialMachineStatus) {
                is MachinesStatus.STOPPED_FOR -> {

                    if (status.responsibleStop == null || status.responsibleStop == ResponsibleStop.NO_STOPPED) {
                        map[Component.InitialMachine] =
                            Component.InitialMachine.ResponsibleStopEmpty
                    }

                    if (!status.date.isNullOrEmpty()) {
                        if (!isDateBefore(
                                status.date,
                                infoOs.dateStart
                            ) || isDateEquals(status.date, infoOs.dateStart)
                        ) {
                            map[Component.InitialMachine] =
                                Component.InitialMachine.DateExceededFormStartDate
                            map[Component.InfoOS] =
                                Component.InfoOS.DateExceededMachineDateStopped
                        }
                    } else {
                        map[Component.InitialMachine] = Component.InitialMachine.DateEmpty
                    }
                }

                else -> {}
            }
        }
    }

    private fun hasNewServiceValidation(
        hasNewService: NewServiceChoose?,
        map: MutableMap<Component, ComponentError>
    ) {
        when (hasNewService) {
            is NewServiceChoose.RETURN -> {
                if (isDateBefore(hasNewService.date, getCurrentDateApi()) ||
                    isDateEquals(hasNewService.date, getCurrentDateApi()) ||
                    ToolBox_Inf.getDateDiferenceInDays(hasNewService.date, getCurrentDateApi()) > 14
                ) {
                    map[Component.ScheduleReturnForm] = Component.ScheduleReturnForm.DateIncorrectOS
                }
            }

            else -> {}
        }
    }
}