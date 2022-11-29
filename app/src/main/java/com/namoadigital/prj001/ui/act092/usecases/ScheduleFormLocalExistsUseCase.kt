package com.namoadigital.prj001.ui.act092.usecases

import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository

class ScheduleFormLocalExistsUseCase constructor(
    private val repository: ActionSerialRepository
) {
    operator fun invoke(input: MD_Schedule_Exec): Pair<Boolean, GE_Custom_Form_Local?> {
        repository.scheduleFormLocalExists(input)?.let {
            return Pair(true, it)
        } ?: return Pair(false, null)
    }
}
