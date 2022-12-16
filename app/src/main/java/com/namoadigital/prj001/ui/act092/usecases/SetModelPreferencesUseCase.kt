package com.namoadigital.prj001.ui.act092.usecases

import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.ui.act092.model.SerialModel

class SetModelPreferencesUseCase constructor(
    private val repository: ActionSerialRepository
) {
    operator fun invoke(input: SerialModel) {
        repository.setPreferences(input)
    }

}
