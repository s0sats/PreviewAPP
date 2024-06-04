package com.namoadigital.prj001.ui.act005.home.di.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.ui.act005.home.data.repository.RoutineCleaningRepository

class ChangeDateRoutineCleaningUseCase constructor(
    private val repository: RoutineCleaningRepository
) : UseCaseWithoutFlow<String, Unit>{
    override fun invoke(input: String) {
        repository.changeDateInPreference(input)
    }
}