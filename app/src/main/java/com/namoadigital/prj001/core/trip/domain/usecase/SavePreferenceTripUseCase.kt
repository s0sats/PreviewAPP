package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.trip.preference.CurrentTripPrefModel

class SavePreferenceTripUseCase constructor(
    private val repository: TripRepository
) : UseCaseWithoutFlow<CurrentTripPrefModel, Unit> {
    override fun invoke(input: CurrentTripPrefModel) {
        repository.setPreference(
            input.customer_code,
            input.trip_prefix,
            input.trip_code,
            input.trip_scn
        )
    }

}
