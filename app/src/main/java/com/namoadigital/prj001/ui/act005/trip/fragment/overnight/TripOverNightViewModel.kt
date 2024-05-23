package com.namoadigital.prj001.ui.act005.trip.fragment.overnight

import androidx.lifecycle.ViewModel
import com.namoadigital.prj001.core.trip.domain.usecase.GetDestinationByStatusUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.DestinationUseCase
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeEnv
import com.namoadigital.prj001.model.trip.toDescription
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripOverNightViewModel  @Inject constructor(
    private val destinationUseCase: DestinationUseCase,
): ViewModel() {

    fun endOverNightPeriod(destination: FsTripDestination, scn: Int) {
        //
        val modelEnv = destination.let{
            TripDestinationStatusChangeEnv(
                it.tripPrefix,
                it.tripCode,
                it.destinationSeq,
                scn,
                DestinationStatus.DEPARTED.toDescription(),
            )
        }
        //
        modelEnv.let {
            destinationUseCase.setDestinationStatusUseCase?.invoke(it)
        }
        //
    }

    fun getOverNightDestination(fsTrip: FSTrip): FsTripDestination? {
        return destinationUseCase.destinationByStatus?.invoke(
            GetDestinationByStatusUseCase.GetDestinationParams(
                fsTrip.customerCode,
                fsTrip.tripPrefix,
                fsTrip.tripCode,
                DestinationStatus.ARRIVED
            )
        )
    }
}