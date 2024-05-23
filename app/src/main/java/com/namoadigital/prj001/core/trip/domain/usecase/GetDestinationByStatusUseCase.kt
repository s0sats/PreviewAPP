package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FsTripDestination


class GetDestinationByStatusUseCase constructor(
    private val repository: TripDestinationRepository
) : UseCaseWithoutFlow<GetDestinationByStatusUseCase.GetDestinationParams, FsTripDestination?> {
    data class GetDestinationParams (
        val customerCode: Long,
        val tripPrefix: Int,
        val tripCode:Int,
        val destinationStatus:DestinationStatus,
    )

    override fun invoke(input: GetDestinationParams): FsTripDestination? {
        return repository.getDestinationByStatus(
            input.customerCode,
            input.tripPrefix,
            input.tripCode,
            input.destinationStatus,
        )
    }


}