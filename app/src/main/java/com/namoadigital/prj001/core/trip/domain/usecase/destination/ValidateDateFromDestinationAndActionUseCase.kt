package com.namoadigital.prj001.core.trip.domain.usecase.destination

import android.content.Context
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.action.TripDestinationActionRepository
import com.namoadigital.prj001.core.trip.domain.usecase.destination.ValidateDateFromDestinationAndActionUseCase.Input
import com.namoadigital.prj001.core.trip.domain.usecase.destination.ValidateDateFromDestinationAndActionUseCase.Output
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflictType
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ValidateDateFromDestinationAndActionUseCase @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val validateDateFromDestinationUseCase: GetDestinationForThresholdValidationUseCase,
    private val actionRepository: TripDestinationActionRepository
) : UseCaseWithoutFlow<Input, Output?> {


    data class Input(
        val prefix: Int,
        val code: Int,
        val seq: Int,
        val newStart: String,
        val newEnd: String?,
        val destinationType: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType
    )

    data class Output(
        val dateStart: String? = null,
        val dateEnd: String? = null,
        val conflict: DestinationConflict,
        val message: String? = null,
    )


    override fun invoke(input: Input): Output? {
        val (prefix, code, seq, newStart, newEnd, destinationType) = input

        val validateDestination = validateDateFromDestinationUseCase(
            input = GetDestinationForThresholdValidationUseCase.InputParam(
                customerCode = appContext.getCustomerCode(),
                tripPrefix = prefix,
                tripCode = code,
                type = destinationType,
                destinationSeq = seq
            )
        )

        val (previous, next) = validateDestination

        when {
            previous != null && next != null -> {
                val previousMs = ToolBox_Inf.dateToMilliseconds(previous.arrivedDate)
                val nextMs = ToolBox_Inf.dateToMilliseconds(next.departedDate)
                val newStartMs = ToolBox_Inf.dateToMilliseconds(newStart)
                val newEndMs = ToolBox_Inf.dateToMilliseconds(newEnd ?: "")
                if (previousMs < newStartMs || nextMs < newEndMs) {
                    return Output(
                        dateStart = "${previous.arrivedDate}",
                        dateEnd = " ${next.departedDate}",
                        conflict = DestinationConflict.RANGE_OVERLAP
                    )
                }

            }

            previous != null -> {
                val previousMs = ToolBox_Inf.dateToMilliseconds(previous.departedDate)
                val newStartMs = ToolBox_Inf.dateToMilliseconds(newStart)

                if (newStartMs < previousMs) {
                    return Output(
                        dateStart = previous.arrivedDate ?: "",
                        dateEnd = previous.departedDate ?: "",
                        conflict = DestinationConflict.START_DESTINATION_OVERLAP
                    )
                }
            }

            next != null -> {
                val nextMs = ToolBox_Inf.dateToMilliseconds(next.arrivedDate)
                val newEndMs = ToolBox_Inf.dateToMilliseconds(newEnd ?: "")

                if (nextMs < newEndMs) {
                    return Output(
                        dateStart = next.arrivedDate ?: "",
                        dateEnd = next.departedDate ?: "",
                        conflict = DestinationConflict.END_DESTINATION_OVERLAP
                    )
                }
            }
        }

        val validateAction = actionRepository.checkDateConflict(
            tripPrefix = prefix,
            tripCode = code,
            newStart = newStart,
            newEnd = newEnd,
            destinationSeq = seq
        )

        if (validateAction != null) {
            return Output(
                dateStart = validateAction.dateStart,
                dateEnd = validateAction.dateEnd,
                conflict = DestinationConflict.fromAction(validateAction.type)
            )
        }


        return null
    }
}

enum class DestinationConflict {
    START_DESTINATION_OVERLAP,
    END_DESTINATION_OVERLAP,
    START_OVERLAP,
    END_OVERLAP,
    RANGE_OVERLAP;

    companion object {
        fun fromAction(conflict: EventConflictType) = when (conflict) {
            EventConflictType.START_OVERLAP -> START_OVERLAP
            EventConflictType.END_OVERLAP -> END_OVERLAP
            else -> RANGE_OVERLAP
        }
    }
}