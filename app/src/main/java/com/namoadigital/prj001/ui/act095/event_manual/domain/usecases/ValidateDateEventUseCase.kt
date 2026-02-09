package com.namoadigital.prj001.ui.act095.event_manual.domain.usecases

import android.content.Context
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import com.namoadigital.prj001.ui.act095.event_manual.domain.repository.EventManualRepository
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflictType
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

enum class EventManualDateType(val key: EventManualKey) {
    EVENT(EventManualKey.ErrorInvalidEventDateMsg),
    FORM(EventManualKey.ErrorInvalidFormDateMsg),
    TRIP(EventManualKey.ErrorInvalidTripDateMsg)
}

class ValidateDateEventUseCase @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val repository: EventManualRepository,
    private val formRepository: GeCustomFormRepository,
    private val tripRepository: TripRepository
) : UseCaseWithoutFlow<ValidateDateEventUseCase.Input, ValidateDateEventUseCase.Output?> {


    data class Input(
        val currentSeq: Int?,
        val eventDay: Int?,
        val startDate: String,
        val endDate: String?
    )

    data class Output(
        val type: EventManualDateType,
        val date: String,
        val fieldWithError: FieldErrorType? = null
    )

    enum class FieldErrorType {
        START_DATE,
        END_DATE,
        BOTH
    }

    override fun invoke(input: Input): Output? {

        val eventConflict = repository.getEventConflict(
            currentSeq = input.currentSeq,
            startDate = input.startDate,
            endDate = input.endDate,
            eventDay = input.eventDay
        )

        val formConflict = formRepository.getFormConflict(
            startDate = input.startDate,
            endDate = input.endDate,
        )

        val tripConflict = tripRepository.getTripConflict(
            startDate = input.startDate,
            endDate = input.endDate
        )

        val typeConflict = when {
            eventConflict != null -> EventManualDateType.EVENT to eventConflict
            formConflict != null -> EventManualDateType.FORM to formConflict
            tripConflict != null -> EventManualDateType.TRIP to tripConflict
            else -> null
        }

        if (typeConflict == null) return null

        val (type, conflict) = typeConflict

        val (message, field) = when (conflict.type) {
            EventConflictType.OPEN_EVENT_EXISTS -> Pair(
                conflict.range(appContext),
                FieldErrorType.BOTH
            )

            EventConflictType.START_OVERLAP -> Pair(
                conflict.range(appContext),
                FieldErrorType.START_DATE
            )

            EventConflictType.END_OVERLAP -> Pair(
                conflict.range(appContext),
                FieldErrorType.END_DATE
            )

            EventConflictType.RANGE_OVERLAP -> Pair(
                conflict.range(appContext),
                FieldErrorType.BOTH
            )
        }

        return Output(
            type = type,
            date = message,
            fieldWithError = field
        )
    }


}