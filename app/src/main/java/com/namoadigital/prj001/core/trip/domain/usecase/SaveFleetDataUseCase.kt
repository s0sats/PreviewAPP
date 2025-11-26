package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.trip.TripTarget
import kotlinx.coroutines.flow.Flow

class SaveFleetDataUseCase(
    private val repository: TripRepository
) : UseCases<SaveFleetDataUseCase.SaveFleetParams, Unit> {

    data class SaveFleetParams(
        val licensePlate: String? = null,
        val odometer: Long?,
        val pathPhoto: String?,
        val changePhoto: Int,
        val target: TripTarget,
        val destinationSeq: Int? = null,
        val deletePhoto: Boolean = false,
    )

    override suspend fun invoke(input: SaveFleetParams): Flow<IResult<Unit>> {
        return repository.execSaveFleetData(
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
