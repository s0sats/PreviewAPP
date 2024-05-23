package com.namoadigital.prj001.ui.act005.trip.di.usecase.origin

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository
import com.namoadigital.prj001.ui.act005.trip.repository.users.TripUserRepository
import com.namoadigital.prj001.util.ToolBox_Inf

class GetFirstDateOnTripUseCase constructor(
    private val event: TripEventRepository,
    private val destination: TripDestinationRepository,
    private val user: TripUserRepository,
): UseCaseWithoutFlow<GetFirstDateOnTripUseCase.InputParam, GetFirstDateOnTripUseCase.OutputParam> {

    data class InputParam(
        val customerCode: Long,
        val tripPrefix: Int,
        val tripCode: Int,
    )

    data class OutputParam(
        val dateError: String?=null,
    )

    override fun invoke(input: InputParam): OutputParam {
        //
        val event = event.getFirstEventOnTrip(input.customerCode, input.tripPrefix, input.tripCode)
        //
        val firstDestination = destination.getNextValidDestination(input.customerCode, input.tripPrefix, input.tripCode, null, GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.NEXT)
        //
        val firstUser = user.getFirstUserOnTrip(input.customerCode, input.tripPrefix, input.tripCode)
        //
        val minValue = when {
            event?.eventStart != null
                    && (firstDestination?.arrivedDate == null
                        || ToolBox_Inf.dateToMilliseconds(event.eventStart) <= ToolBox_Inf.dateToMilliseconds(firstDestination.arrivedDate))
                    && (firstUser?.dateStart == null
                        || ToolBox_Inf.dateToMilliseconds(event.eventStart) <= ToolBox_Inf.dateToMilliseconds(firstUser.dateStart)) -> event.eventStart
            firstDestination?.arrivedDate != null
                    && (event?.eventStart == null
                        || ToolBox_Inf.dateToMilliseconds(firstDestination.arrivedDate) <= ToolBox_Inf.dateToMilliseconds(event.eventStart))
                    && (firstUser?.dateStart == null
                        || ToolBox_Inf.dateToMilliseconds(firstDestination.arrivedDate) <= ToolBox_Inf.dateToMilliseconds(firstUser.dateStart)) -> firstDestination.arrivedDate
            firstUser?.dateStart != null -> firstUser.dateStart
            else -> null
        }

        return OutputParam(
            dateError = minValue
        )
    }

}