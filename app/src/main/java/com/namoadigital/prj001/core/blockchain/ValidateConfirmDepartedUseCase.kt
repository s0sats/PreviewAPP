package com.namoadigital.prj001.core.blockchain

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.util.ToolBox_Inf.dateToMilliseconds
import javax.inject.Inject

class ValidateConfirmDepartedUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    private val tripDestinationRepository: TripDestinationRepository
) : UseCaseWithoutFlow<Unit, Boolean> {

    override fun invoke(input: Unit): Boolean {
        val trip = tripRepository.getTrip()!!
        val startDate = trip.startDate

        val referenceTimeMs = tripDestinationRepository.getLastDestinationArrived(trip.tripPrefix, trip.tripCode)
            ?.arrivedDate
            ?.let(ToolBox_Inf::dateToMilliseconds)
            ?: dateToMilliseconds(startDate)

        return isWithinTimeWindow(referenceTimeMs)
    }

    private fun isWithinTimeWindow(referenceTimeMs: Long): Boolean {
        val diffMinutes = kotlin.math.abs(referenceTimeMs - System.currentTimeMillis()) / 60_000
        return diffMinutes < 1L
    }
}