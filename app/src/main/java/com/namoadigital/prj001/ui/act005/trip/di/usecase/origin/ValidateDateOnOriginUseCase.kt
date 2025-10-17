package com.namoadigital.prj001.ui.act005.trip.di.usecase.origin

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository
import com.namoadigital.prj001.ui.act005.trip.repository.users.TripUserRepository
import com.namoadigital.prj001.util.ToolBox_Inf

class ValidateDateOnOriginUseCase(
    private val tripEventRepository: TripEventRepository,
    private val tripUserRepository: TripUserRepository,
    private val tripRepository: TripRepository
) : UseCaseWithoutFlow<ValidateDateOnOriginUseCase.InputParam, ValidateDateOnOriginUseCase.OutputParam> {

    data class InputParam(
        val customerCode: Long,
        val tripPrefix: Int,
        val tripCode: Int,
    )

    data class OutputParam(
        val dateError: String? = null,
    )

    override fun invoke(input: InputParam): OutputParam {

        val earliestDate = findEarliestDate(input)
        return OutputParam(dateError = earliestDate)
    }

    private fun findEarliestDate(input: InputParam): String? {
        val dates = mutableListOf<String>()
        getStartTripDate()?.let(dates::add)
        getFirstEventDate(input)?.let(dates::add)
        getFirstUserDate(input)?.let(dates::add)

        return if (dates.isEmpty()) null
        else dates.minBy(ToolBox_Inf::dateToMilliseconds)
    }

    private fun getStartTripDate(): String? = tripRepository.getTrip()?.startDate

    private fun getFirstEventDate(input: InputParam): String? {
        return tripEventRepository.getFirstEventOnTrip(
            input.customerCode,
            input.tripPrefix,
            input.tripCode
        )?.eventStart
    }

    private fun getFirstUserDate(input: InputParam): String? {
        return tripUserRepository.getFirstUserOnTrip(
            input.customerCode,
            input.tripPrefix,
            input.tripCode
        )?.dateStart
    }
}
