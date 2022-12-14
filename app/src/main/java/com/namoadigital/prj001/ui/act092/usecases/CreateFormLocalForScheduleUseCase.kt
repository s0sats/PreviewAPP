package com.namoadigital.prj001.ui.act092.usecases

import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository

class CreateFormLocalForScheduleUseCase constructor(
    private val repository: ActionSerialRepository,
) {

    operator fun invoke(localExists: Boolean, scheduleExec: MD_Schedule_Exec, actions: MyActions): Boolean {
        return repository.createFormLocalForSchedule(
            localExists,
            scheduleExec,
            actions
        )
    }

}
