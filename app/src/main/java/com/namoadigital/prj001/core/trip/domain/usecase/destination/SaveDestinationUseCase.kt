package com.namoadigital.prj001.core.trip.domain.usecase.destination

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable

class SaveDestinationUseCase constructor(
    private val repository: TripDestinationRepository
) : UseCaseWithoutFlow<SaveDestinationUseCase.GetDestinationParams, Boolean> {
    data class GetDestinationParams (
        val customerCode: Long,
        val response: String?,
        val destination: SelectionDestinationAvailable,
    )

    override fun invoke(input: GetDestinationParams):Boolean {
        return repository.saveDestination(
            input.customerCode,
            input.response,
            input.destination,
        )
    }

}