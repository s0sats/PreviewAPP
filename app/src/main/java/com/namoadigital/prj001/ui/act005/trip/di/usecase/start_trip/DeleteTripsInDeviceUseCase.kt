package com.namoadigital.prj001.ui.act005.trip.di.usecase.start_trip

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import javax.inject.Inject

class DeleteTripsInDeviceUseCase @Inject constructor(
    private val tripRepository: TripRepository
) : UseCaseWithoutFlow<Unit, Unit> {

    override fun invoke(input: Unit) {
        tripRepository.deleteTripsInDevice()
    }
}