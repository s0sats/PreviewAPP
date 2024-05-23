package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository

class SaveDestinationDateUseCase constructor(
    private val repository: TripDestinationRepository
): UseCaseWithoutFlow<SaveDestinationDateUseCase.SaveDestinationDateParams, Unit>{


    data class SaveDestinationDateParams(
        val dateStart: String,
        val dateEnd: String,
        val destinationSeq: Int
    )

    override fun invoke(input: SaveDestinationDateParams) {
        repository.saveDestinationDate(
            input.dateStart,
            input.dateEnd,
            input.destinationSeq
        )
    }

}
