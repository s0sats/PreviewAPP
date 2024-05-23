package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.trip.TripTarget

class SaveFleetDataUseCase constructor(
    private val repository: TripRepository
) : UseCaseWithoutFlow<SaveFleetDataUseCase.SaveFleetParams, Unit> {

    data class SaveFleetParams(
        val licensePlate: String? = null,
        val odometer: Long?,
        val pathPhoto: String?,
        val changePhoto: Int,
        val target: TripTarget,
        val destinationSeq: Int? = null,
        val deletePhoto: Boolean=false,
    )

    override fun invoke(input: SaveFleetParams) {
        repository.execSaveFleetData(
            input.licensePlate,
            input.odometer,
            input.pathPhoto,
            input.changePhoto,
            input.target.type,
            input.destinationSeq,
            input.deletePhoto
        )
    }


}
