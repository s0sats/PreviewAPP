package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.trip.TripStatus
import kotlinx.coroutines.flow.Flow

class TripStatusChangeUseCase constructor(
    private val repository: TripRepository,
    private val checkNextDestinationStatusTripUseCase: CheckNextDestinationStatusTripUseCase
) : UseCases<TripStatusChangeUseCase.Input, Unit> {

    data class Input(
        val tripStatus: TripStatus,
    )

    override suspend fun invoke(
        input: Input
    ): Flow<IResult<Unit>> {
        //
        val invoke = checkNextDestinationStatusTripUseCase.invoke(
            CheckNextDestinationStatusTripUseCase.Input(
                tripStatus = input.tripStatus,
            )
        )
        //
        return repository.setTripStatus(
            input.tripStatus,
            invoke.nextTripStatus,
            invoke.destinationSeq,
            invoke.destinationStatus,
            invoke.nextDestinationSeq,
            invoke.nextDestinationStatus,
        )
    }
}