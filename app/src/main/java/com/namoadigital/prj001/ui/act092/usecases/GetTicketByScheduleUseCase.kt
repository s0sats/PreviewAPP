package com.namoadigital.prj001.ui.act092.usecases

import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository

class GetTicketByScheduleUseCase constructor(
    private val repository: ActionSerialRepository
) {


    operator fun invoke(
        schedulePrefix: Int,
        scheduleCode: Int,
        scheduleExec: Int
    ): TK_Ticket? {
        return repository.getTicketBySchedule(
            schedulePrefix,
            scheduleCode,
            scheduleExec
        )
    }

}
