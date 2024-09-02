package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class SaveDestinationDateUseCase constructor(
    private val repository: TripDestinationRepository
): UseCases<SaveDestinationDateUseCase.SaveDestinationDateParams, Unit> {


    data class SaveDestinationDateParams(
        val dateStart: String,
        val dateEnd: String,
        val destinationSeq: Int
    )

    override suspend fun invoke(input: SaveDestinationDateParams): Flow<IResult<Unit>> {
        return repository.saveDestinationDate(
                input.dateStart,
                input.dateEnd,
                input.destinationSeq
            )
    }


}
