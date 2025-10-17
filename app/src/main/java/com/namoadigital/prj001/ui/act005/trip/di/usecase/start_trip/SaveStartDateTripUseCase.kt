package com.namoadigital.prj001.ui.act005.trip.di.usecase.start_trip

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SaveStartDateTripUseCase @Inject constructor(
    private val tripRepository: TripRepository
) : UseCases<String, Unit> {
    override suspend fun invoke(input: String): Flow<IResult<Unit>> {
        return tripRepository.saveStartDateSet(input)
    }

}