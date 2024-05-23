package com.namoadigital.prj001.core.trip.domain.usecase.destination

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.model.trip.FsTripDestination

class GetDestinationForThresholdValidationUseCase constructor(
    private val repository: TripDestinationRepository
): UseCaseWithoutFlow<GetDestinationForThresholdValidationUseCase.InputParam, GetDestinationForThresholdValidationUseCase.OutputParam> {

    data class InputParam(
        val customerCode: Long,
        val tripPrefix: Int,
        val tripCode: Int,
        val destinationSeq: Int?,
        val type: TripDestinationValidationType,

    )

    data class OutputParam(
        val previousDestination: FsTripDestination?,
        val nextDestination: FsTripDestination?,
    )

    enum class TripDestinationValidationType{
        PREVIOUS,
        NEXT,
        BOTH,
        ODOMETER_PREVIOUS,
        ODOMETER_NEXT,
        ODOMETER_BOTH,

    }


    override fun invoke(input: InputParam):OutputParam {
        when(input.type){
            TripDestinationValidationType.PREVIOUS,
            TripDestinationValidationType.ODOMETER_PREVIOUS,
            ->{
                return OutputParam(
                    repository.getPreviousValidDestination(
                        input.customerCode,
                        input.tripPrefix,
                        input.tripCode,
                        input.destinationSeq,
                        input.type
                    ),
                    null
                )
            }
            TripDestinationValidationType.NEXT,
            TripDestinationValidationType.ODOMETER_NEXT
            ->{
                return OutputParam(
                    null,
                    repository.getNextValidDestination(
                        input.customerCode,
                        input.tripPrefix,
                        input.tripCode,
                        input.destinationSeq,
                        input.type
                    )

                )
            }
            TripDestinationValidationType.BOTH,
            TripDestinationValidationType.ODOMETER_BOTH
            ->{
                return OutputParam(
                    repository.getPreviousValidDestination(
                        input.customerCode,
                        input.tripPrefix,
                        input.tripCode,
                        input.destinationSeq,
                        input.type
                    ),
                    repository.getNextValidDestination(
                        input.customerCode,
                        input.tripPrefix,
                        input.tripCode,
                        input.destinationSeq,
                        input.type
                    )
                )
            }
        }
    }

}
