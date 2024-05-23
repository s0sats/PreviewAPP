package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.model.trip.FsTripDestination

class GetDestinationUseCase constructor(
    private val repository: TripDestinationRepository
) : UseCaseWithoutFlow<GetDestinationUseCase.GetDestinationParams, FsTripDestination?>{
    override fun invoke(input: GetDestinationParams): FsTripDestination? {
        return repository.getDestination(
            input.customerCode,
            input.tripPrefix,
            input.tripCode,
            input.destinationSeq,
        )
    }
    //
    data class GetDestinationParams(
        val customerCode: Long,
        val tripPrefix: Int,
        val tripCode:Int,
        val destinationSeq: Int
    )
    //
}
