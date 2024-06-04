package com.namoadigital.prj001.ui.act005.home.di.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.ui.act005.home.data.repository.RoutineCleaningRepository

class CheckRoutineCleaningUseCase constructor(
    private val repository: RoutineCleaningRepository
) : UseCaseWithoutFlow<Unit, Unit> {
    override fun invoke(input: Unit) {
        repository.routineCleaningTables()
    }
}