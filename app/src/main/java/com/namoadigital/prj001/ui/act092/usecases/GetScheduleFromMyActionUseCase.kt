package com.namoadigital.prj001.ui.act092.usecases

import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository

class GetScheduleFromMyActionUseCase constructor(
    private val repository: ActionSerialRepository
) {

    operator fun invoke(action: MyActions): MD_Schedule_Exec? {
        return repository.getScheduleFromMyAction(
            action.getSplippedPk()[0].toInt(),
            action.getSplippedPk()[1].toInt(),
            action.getSplippedPk()[2].toInt(),
        )
    }
}
