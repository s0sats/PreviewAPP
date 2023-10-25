package com.namoadigital.prj001.ui.act092.usecases

import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository

class ResetSerialModelPreferenceUseCase constructor(
    private val repository: ActionSerialRepository
) {

    operator fun invoke() {
        repository.clearPreference()
    }

}
