package com.namoadigital.prj001.core.trip.domain.usecase.destination

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.AvailableDestinationFilter

class GetFilterPreferenceUseCase constructor(
    private val repository: TripDestinationRepository
) : UseCaseWithoutFlow<Unit, AvailableDestinationFilter> {
    override fun invoke(input: Unit): AvailableDestinationFilter {
        return repository.getDestinationFilterPreference()
    }

}
