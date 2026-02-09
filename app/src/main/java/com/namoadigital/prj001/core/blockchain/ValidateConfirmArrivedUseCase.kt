package com.namoadigital.prj001.core.blockchain

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.util.ToolBox_Inf
import javax.inject.Inject

class ValidateConfirmArrivedUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    private val tripDestinationRepository: TripDestinationRepository
) : UseCaseWithoutFlow<Unit, Boolean> {

    override fun invoke(input: Unit): Boolean {
        val trip = tripRepository.getTrip()!!
        val startDate = trip.startDate

        val referenceTimeMs = tripDestinationRepository.getLastDestinationDeparted(trip.tripPrefix, trip.tripCode)
            ?.departedDate
            ?.let(ToolBox_Inf::dateToMilliseconds)
            ?: ToolBox_Inf.dateToMilliseconds(startDate)

        return isWithinTimeWindow(referenceTimeMs)
    }

    private fun isWithinTimeWindow(referenceTimeMs: Long): Boolean {
        val diffMinutes = kotlin.math.abs(referenceTimeMs - System.currentTimeMillis()) / 60_000
        return diffMinutes <= 1L
    }
}