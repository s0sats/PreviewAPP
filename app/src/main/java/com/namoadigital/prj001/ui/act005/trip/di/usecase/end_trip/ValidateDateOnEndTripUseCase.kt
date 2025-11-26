package com.namoadigital.prj001.ui.act005.trip.di.usecase.end_trip

import com.namoadigital.prj001.adapter.trip.model.ExtractType
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository
import com.namoadigital.prj001.util.ToolBox_Inf
import javax.inject.Inject

class ValidateDateOnEndTripUseCase @Inject constructor(
    private val tripDestinationRepository: TripDestinationRepository,
    private val tripRepository: TripRepository,
    private val tripEventRepository: TripEventRepository,
) : UseCaseWithoutFlow<ValidateDateOnEndTripUseCase.InputParam, Pair<String, ExtractType>?> {

    data class InputParam(
        val customerCode: Long,
        val tripPrefix: Int,
        val tripCode: Int,
        val tripDate: String,
    )

    override fun invoke(input: InputParam): Pair<String, ExtractType>? {
        val tripDateMs = ToolBox_Inf.dateToMilliseconds(input.tripDate)

        return getStartDate(tripDateMs)?.let { it to ExtractType.START_TRIP }
            ?: getEventDate(
                tripPrefix = input.tripPrefix,
                tripCode = input.tripCode,
                tripDateMs = tripDateMs
            )?.let { it to ExtractType.EVENT }
            ?: getUserDate(tripDateMs)?.let { it to ExtractType.USER }
            ?: getFirstDestinationDate(input)?.let { it to ExtractType.DESTINATION }
    }

    private fun getStartDate(tripDateMs: Long) = tripRepository.getTrip()?.startDate?.let {
        if (tripDateMs < ToolBox_Inf.dateToMilliseconds(it)) {
            it
        } else {
            null
        }
    }

    private fun getEventDate(
        tripPrefix: Int,
        tripCode: Int,
        tripDateMs: Long,
    ): String? =
        tripEventRepository.getAllEvents(
            tripPrefix = tripPrefix,
            tripCode = tripCode
        ).firstOrNull { event ->
            event.eventEnd?.let { tripDateMs < ToolBox_Inf.dateToMilliseconds(it) } == true
        }?.eventEnd

    private fun getUserDate(tripDateMs: Long): String? {
        val trip = tripRepository.getTrip() ?: return null

        return tripRepository.getUsersCurrentTrip(
            tripPrefix = trip.tripPrefix,
            tripCode = trip.tripCode
        ).firstOrNull { user ->
            if (user.dateEnd.isNullOrEmpty()) return@firstOrNull false
            user.dateEnd.let { tripDateMs < ToolBox_Inf.dateToMilliseconds(it) }
        }?.dateEnd
    }

    private fun getFirstDestinationDate(input: InputParam) =
        tripDestinationRepository.getLastDestination(
            prefix = input.tripPrefix,
            code = input.tripCode
        )?.departedDate?.let {
            val tripDateMs = ToolBox_Inf.dateToMilliseconds(input.tripDate)
            if (tripDateMs < ToolBox_Inf.dateToMilliseconds(it)) {
                it
            } else {
                null
            }
        }
}