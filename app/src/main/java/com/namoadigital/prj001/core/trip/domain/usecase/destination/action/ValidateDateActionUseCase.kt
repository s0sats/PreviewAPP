package com.namoadigital.prj001.core.trip.domain.usecase.destination.action

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.action.TripDestinationActionRepository
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflict

import javax.inject.Inject

class ValidateDateActionUseCase @Inject constructor(
    private val repository: TripDestinationActionRepository
) : UseCaseWithoutFlow<ValidateDateActionUseCase.Input, EventConflict?> {


    data class Input(
        val tripPrefix: Int,
        val tripCode: Int,
        val destinationSeq: Int,
        val newStart: String,
        val newEnd: String?
    )


    override fun invoke(input: Input): EventConflict? {
        val (prefix, code, seq, newStart, newEnd) = input

        return repository.checkDateConflict(
            tripPrefix = prefix,
            tripCode = code,
            destinationSeq = seq,
            newStart = newStart,
            newEnd = newEnd
        )
    }
}