package com.namoadigital.prj001.ui.act092.usecases

import com.namoadigital.prj001.model.TK_Ticket_Ctrl
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository

class GetScheduleCtrlIfExistsUseCase constructor(
    private val repository: ActionSerialRepository
) {

    data class GetScheduleCtrlIfExistsParam(
        val schedulePrefix: String,
        val scheduleCode: String,
        val scheduleExec: String,
        val ticketPrefix: String,
        val ticketCode: String,
    )

    operator fun invoke(input: GetScheduleCtrlIfExistsParam): TK_Ticket_Ctrl? {
        return repository.getScheduleCtrlIFExists(
            input.schedulePrefix,
            input.scheduleCode,
            input.scheduleExec,
            input.ticketPrefix,
            input.ticketCode
        )
    }
}
