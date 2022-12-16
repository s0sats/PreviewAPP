package com.namoadigital.prj001.ui.act092.usecases

import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.ui.act092.model.SerialModel

class GetSerialModelPreferencesUseCase constructor(
    private val repository: ActionSerialRepository
) {

    operator fun invoke(): SerialModel {
        return repository.getPreferences()
    }

}
