package com.namoadigital.prj001.service.location.usecase

import android.util.Log
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.position.TripPositionRepository
import com.namoadigital.prj001.core.trip.data.preference.CurrentTripPref
import javax.inject.Inject

class ExecLastPositionPackageUseCase @Inject constructor(
    private val currentTripPref: CurrentTripPref,
    private val positionRepository: TripPositionRepository,
) : UseCaseWithoutFlow<Unit, Unit> {
    //Verifica necessidade de enviar posições pos termino de viagem.
    override fun invoke(input: Unit) {
        if (positionRepository.isUpdateRequired()) {
            val currentTripPrefModel = currentTripPref.read()
            currentTripPrefModel.isRef = 1
            currentTripPrefModel.position_counter = 0
            currentTripPref.write(
                currentTripPrefModel
            )
            //
            positionRepository.execPositionSave()
        }
    }
}