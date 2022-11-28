package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.success
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

class ProcessTicketUseCase constructor(
    private val context: Context,
    private val repository: ActionSerialRepository
) : UseCases<ProcessTicketUseCase.ProcessTicketParam, FlowScheduleFromMyActionUseCase.FlowScheduleParamReturn> {

    data class ProcessTicketParam(
        val actions: MyActions,
        val scheduleExec: MD_Schedule_Exec
    )


    override suspend fun invoke(input: ProcessTicketParam): Flow<IResult<FlowScheduleFromMyActionUseCase.FlowScheduleParamReturn>> {
        return flow {

            val actions = input.actions
            val scheduleExec = input.scheduleExec

            val splippedPk = actions.getSplippedPk()
            val scheduleTicket = repository.getTicketBySchedule(
                splippedPk[0].toInt(),
                splippedPk[1].toInt(),
                splippedPk[2].toInt()
            )

            val ticketPrefix = scheduleTicket?.ticket_prefix ?: 0
            val ticketCode = scheduleTicket?.ticket_code ?: 0


            if (ConstantBaseApp.SYS_STATUS_SCHEDULE != scheduleExec.status) {
                if (isScheduleStatusPossibleToOpen(scheduleExec)) {
                    scheduleTicket?.let {
                        if (ticketPrefix > 0 && ticketCode > 0) {
                            emit(
                                success(
                                    FlowScheduleFromMyActionUseCase.FlowScheduleParamReturn(
                                        scheduleExec = scheduleExec,
                                        actType = Constant.ACT070,
                                        ticketPrefix = ticketPrefix,
                                        ticketCode = ticketCode,
                                        action = actions
                                    )
                                )
                            )
                        } else {
                            emit(
                                success(
                                    FlowScheduleFromMyActionUseCase.FlowScheduleParamReturn(
                                        scheduleExec = scheduleExec,
                                        actType = Constant.ACT071,
                                        ticketPrefix = ticketPrefix,
                                        ticketCode = ticketCode,
                                        action = actions
                                    )
                                )
                            )
                        }
                    }
                } else {
                    emit(failed(ScheduleFormException(MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN)))
                }
            } else {
                if (isScheduleSiteDifferentThanLogged(actions.siteCode, context)) {
                    if (hasScheduleSiteAccess(actions.siteCode.toString(), repository)) {
                        emit(failed(ScheduleFormException(ProcessFormUseCase.SITE_RESTRICTION_CONFIRM))) //fazer acao na tela principal
                    } else {
                        emit(failed(ScheduleFormException(ProcessFormUseCase.SITE_RESTRICTION_NO_ACCESS)))
                    }
                } else {
                    if (ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context)) {
                        emit(IResult.failed(ScheduleFormException(ProcessFormUseCase.FREE_EXECUTION_BLOCKED)))
                    } else {
                        emit(IResult.failed(ScheduleFormException(ProcessFormUseCase.MODULE_CHECKLIST_START_FORM)))
                    }
                }
            }
        }
    }


    private fun isScheduleStatusPossibleToOpen(scheduleExec: MD_Schedule_Exec): Boolean {
        //
        return (!scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_CANCELLED)
                && !scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_REJECTED)
                && !scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_IGNORED)
                && !scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_NOT_EXECUTED))
    }

    companion object {
        const val MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN =
            "MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN"
    }

}
