package com.namoadigital.prj001.core.trip.domain.usecase.destination

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.AvailableDestinationFilter

class SaveDestinationFilterUseCase constructor(
    private val repository: TripDestinationRepository
) : UseCaseWithoutFlow<AvailableDestinationFilter, Unit> {
    override fun invoke(input: AvailableDestinationFilter) {
        repository.saveFilterPreference(input)
    }

}
