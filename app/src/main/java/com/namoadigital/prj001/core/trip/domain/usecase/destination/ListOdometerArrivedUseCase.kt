package com.namoadigital.prj001.core.trip.domain.usecase.destination

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.domain.model.OdometerArrivedDestination

class ListOdometerArrivedUseCase constructor(
    private val repository: TripDestinationRepository
): UseCaseWithoutFlow<Unit, List<OdometerArrivedDestination>>{
    override fun invoke(input: Unit): List<OdometerArrivedDestination> {
        return repository.getListOdometerArrived()
    }

}
