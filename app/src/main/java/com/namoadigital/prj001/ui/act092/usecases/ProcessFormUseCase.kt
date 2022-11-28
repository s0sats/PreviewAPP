package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.hasScheduleSiteAccess
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.isScheduleSiteDifferentThanLogged
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProcessFormUseCase constructor(
    private val context: Context,
    private val repository: ActionSerialRepository,
    private val scheduleFormLocalExistsUseCase: ScheduleFormLocalExistsUseCase
) : UseCases<ProcessFormUseCase.ProcessFormParam, FlowScheduleFromMyActionUseCase.FlowScheduleParamReturn> {


    data class ProcessFormParam(
        var actions: MyActions,
        var scheduleExec: MD_Schedule_Exec
    )

    override suspend fun invoke(input: ProcessFormParam): Flow<IResult<FlowScheduleFromMyActionUseCase.FlowScheduleParamReturn>> {
        return flow {

            val scheduleExec = input.scheduleExec
            val actions = input.actions

            if (Constant.SYS_STATUS_SCHEDULE == scheduleExec.status) {
                if (isScheduleSiteDifferentThanLogged(actions.siteCode, context)) {
                    if (hasScheduleSiteAccess(actions.siteCode.toString(), repository)) {
                        emit(IResult.failed(ScheduleFormException(SITE_RESTRICTION_CONFIRM))) //fazer acao na tela principal
                    } else {
                        emit(IResult.failed(ScheduleFormException(SITE_RESTRICTION_NO_ACCESS)))
                    }
                } else if (isAnyFormInProcessing(scheduleExec)) {
                    emit(IResult.failed(ScheduleFormException(MODULE_CHECKLIST_FORM_IN_PROCESSING)))
                } else {
                    if (ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context)) {
                        emit(IResult.failed(ScheduleFormException(FREE_EXECUTION_BLOCKED)))
                    } else {
                        emit(IResult.failed(ScheduleFormException(MODULE_CHECKLIST_START_FORM)))
                    }
                }
            } else {
                if (isStatusPossibleToOpen(scheduleExec)) {
                    scheduleFormLocalExists(scheduleExec, actions)
                    emit(
                        IResult.success(
                            FlowScheduleFromMyActionUseCase.FlowScheduleParamReturn(
                                scheduleExec = scheduleExec,
                                actType = Constant.ACT011,
                                action = actions,
                            )
                        )
                    )
                } else {
                    emit(
                        IResult.failed(
                            ScheduleFormException(
                                MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN
                            )
                        )
                    )
                }
            }
        }
    }


    fun scheduleFormLocalExists(scheduleExec: MD_Schedule_Exec, actions: MyActions): Boolean {

        val scheduleForm = scheduleFormLocalExistsUseCase(scheduleExec)

        if (scheduleForm.first) {
            actions.scheduleCustomFormData = scheduleForm.second?.custom_form_data.toString()
        }
        return scheduleForm.first
    }

    private fun isStatusPossibleToOpen(scheduleExec: MD_Schedule_Exec): Boolean {
        return (scheduleExec.status != null
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_CANCELLED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_REJECTED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_IGNORED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_NOT_EXECUTED)
    }

    private suspend fun isAnyFormInProcessing(scheduleExec: MD_Schedule_Exec): Boolean {
        with(scheduleExec) {
            return repository.getCustomFormLocal(
                customer_code.toString(),
                custom_form_type.toString(),
                custom_form_code.toString(),
                custom_form_version.toString(),
                product_code.toString(),
                serial_id.toString()
            ) != null
        }
    }


    companion object {

        const val SITE_RESTRICTION_CONFIRM = "SITE_RESTRICTION_CONFIRM"
        const val SITE_RESTRICTION_NO_ACCESS = "SITE_RESTRICTION_NO_ACCESS"
        const val MODULE_CHECKLIST_FORM_IN_PROCESSING = "CHECKLIST_FORM_IN_PROCESSING"
        const val FREE_EXECUTION_BLOCKED = "FREE_EXECUTION_BLOCKED"
        const val MODULE_CHECKLIST_START_FORM = "MODULE_CHECKLIST_START_FORM"
        const val MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN =
            "MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN"
    }
}
