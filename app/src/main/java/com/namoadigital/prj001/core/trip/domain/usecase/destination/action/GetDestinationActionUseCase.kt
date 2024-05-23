package com.namoadigital.prj001.core.trip.domain.usecase.destination.action

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.action.TripDestinationActionRepository
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.FsTripDestinationAction

class GetDestinationActionUseCase  constructor(
    private val repository: TripDestinationActionRepository
) : UseCaseWithoutFlow<FsTripDestination, List<FsTripDestinationAction>> {
    override fun invoke(input: FsTripDestination):List<FsTripDestinationAction> {
        return repository.getDestinationActions(
            input.customerCode,
            input.tripPrefix,
            input.tripCode,
            input.destinationSeq,
        )
    }
}

