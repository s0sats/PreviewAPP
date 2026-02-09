package com.namoadigital.prj001.ui.act011.finish_os.di.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.blockchain.ValidateTimelineBlockUseCase
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TimelineValidationAction
import com.namoadigital.prj001.core.trip.domain.model.blockchain.ValidationResult
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
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.FORM_OS_INFO_END_DATE_FUTURE_ERROR_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.EditedField
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishState
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation.Component
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation.ComponentError
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.ValidateResult
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ValidateFinishOSUseCase @Inject constructor(
    private val getGeOsByIdUseCase: GetGeOsByIdUseCase,
    private val getMeasureTpByCode: GetMeasureTpByCode,
    private val validateTimeline: ValidateTimelineBlockUseCase
) : UseCases<ValidateFinishOSUseCase.Param, ValidateResult> {

    data class Param(
        val validation: FinishValidation,
        val primaryKey: FinishState.FormPrimaryKey,
        val editedField: EditedField? = null,
        val isReadOnly: Boolean = false
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

            val action: ValidationResult = if (!input.isReadOnly) {
                validateTimeline(
                    input = TimelineValidationAction.ValidateForm(
                        startDate = infoOs.dateStart,
                        endDate = infoOs.dateEnd,
                        formPK = geOs?.getFormPK(),
                    )
                )
            } else {
                ValidationResult.Success
            }

            when {
                action is ValidationResult.Conflict -> {
                    map[Component.InfoOS] = Component.InfoOS.InvalidBothDateAction(action)
                }
                //
                partialExecutionOS != null && isDateBeforeOrEquals(
                    infoOs.dateStart,
                    partialExecutionOS
                ) -> {
                    map[Component.InfoOS] = Component.InfoOS.PartialExecutionOS
                }

                ToolBox_Inf.isFutureDate(infoOs.dateStart) || ToolBox_Inf.isFutureDate(infoOs.dateEnd) -> {
                    if (input.editedField == EditedField.DATE_END) {
                        map[Component.InfoOS] = Component.InfoOS.InvalidFutureEndDate(
                            FORM_OS_INFO_END_DATE_FUTURE_ERROR_LBL
                        )
                    }
                    if (input.editedField == EditedField.DATE_START) {
                        map[Component.InfoOS] = Component.InfoOS.InvalidFutureStartDate(
                            FORM_OS_INFO_END_DATE_FUTURE_ERROR_LBL
                        )
                    }
                }

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
                    ToolBox_Inf.getDateDiferenceInDays(
                        hasNewService.date,
                        getCurrentDateApi()
                    ) > 14
                ) {
                    map[Component.ScheduleReturnForm] = Component.ScheduleReturnForm.DateIncorrectOS
                }
            }

            else -> {}
        }
    }
}