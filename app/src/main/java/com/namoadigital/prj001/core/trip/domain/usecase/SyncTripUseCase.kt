package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository

class SyncTripUseCase constructor(
    private val repository: TripRepository
) : UseCaseWithoutFlow<String, Unit> {


    override fun invoke(input: String) {
        repository.execSyncTrip()
    }
}