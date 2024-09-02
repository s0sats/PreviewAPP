package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.domain.usecase.destination.CheckNextStatusTripUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.DestinationUseCase
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeEnv
import com.namoadigital.prj001.model.trip.toDescription
import kotlinx.coroutines.flow.Flow

class SetDestinationStatusUseCase constructor(
    private val repository: TripDestinationRepository,
    private val checkNextStatusTripUseCase: CheckNextStatusTripUseCase
) : UseCases<SetDestinationStatusUseCase.Params, Unit> {

    data class Params(
        val destinationSeq: Int,
        val status: DestinationStatus
    )

    override suspend fun invoke(input: Params): Flow<IResult<Unit>> {

        val (destinationSeq, status) = input
        val nextTripStatus = checkNextStatusTripUseCase(status)
        return repository.setTripDestinationStatusChange(
            destinationSeq,
            status.toDescription(),
            nextTripStatus.toDescription()
        )
    }

}