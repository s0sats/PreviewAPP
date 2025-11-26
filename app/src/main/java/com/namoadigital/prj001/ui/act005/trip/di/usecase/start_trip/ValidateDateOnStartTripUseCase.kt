package com.namoadigital.prj001.ui.act005.trip.di.usecase.start_trip

import com.namoadigital.prj001.adapter.trip.model.ExtractType
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


//choice do passado que fez essa cagada e no futuro ainda culpou o luquinha, mdsss...
@ViewModelScoped
class ValidateDateOnStartTripUseCase @Inject constructor(
    private val tripDestinationRepository: TripDestinationRepository,
    private val tripRepository: TripRepository
) : UseCaseWithoutFlow<ValidateDateOnStartTripUseCase.InputParam, Pair<String?, ExtractType>> {

    data class InputParam(
        val customerCode: Long,
        val tripPrefix: Int,
        val tripCode: Int,
        val tripDate: String,
    )

    override fun invoke(input: InputParam): Pair<String?, ExtractType> {

        if (ToolBox_Inf.dateToMilliseconds(input.tripDate) <= ToolBox_Inf.dateToMilliseconds(
                getOriginDate()
            )
        ) {
            return Pair(getOriginDate(), ExtractType.ORIGIN)
        }

        return Pair(getFirstDestinationDate(input), ExtractType.DESTINATION)
    }

    private fun getOriginDate(): String? = tripRepository.getTrip()?.originDate

    private fun getFirstDestinationDate(input: InputParam): String? {
        return tripDestinationRepository.getNextValidDestination(
            input.customerCode,
            input.tripPrefix,
            input.tripCode,
            null,
            GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.NEXT
        )?.arrivedDate
    }
}
