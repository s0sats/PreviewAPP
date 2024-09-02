package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository

class CheckExistsTripUpdateUseCase constructor(
    private val repository: TripRepository
) : UseCaseWithoutFlow<Unit, Boolean> {
    override fun invoke(input: Unit): Boolean {
        return repository.existsTripWithUpdateRequired()
    }

}
