package com.namoadigital.prj001.ui.act005.trip.fragment.pending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namoadigital.prj001.core.trip.domain.usecase.SetDestinationStatusUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.DestinationUseCase
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeEnv
import com.namoadigital.prj001.model.trip.toDescription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripPendingViewModel @Inject constructor(
    private val destinationUseCase: DestinationUseCase,
) : ViewModel() {


    private fun checkFleetData(trip: FSTrip): Boolean {
        return !trip.isRequiredFleetData
                ||
                (trip.fleetStartOdometer != null
                        && trip.fleetStartPhotoName != null)
    }

    private fun checkDestinationData(trip: FSTrip, destination: FsTripDestination?): Boolean {
        return !trip.isRequireDestinationFleetData
                ||
                (destination?.destinationStatus == DestinationStatus.PENDING.toDescription())
    }

    fun checkStartTrip(trip: FSTrip?, destination: FsTripDestination?): Boolean {
        trip?.let {
            if (checkFleetData(it)
                && checkDestinationData(it, destination)
            ) {
                return true
            }
        }
        return false
    }

    fun startTrip(trip: FSTrip?, destination: FsTripDestination) {
        viewModelScope.launch {
            destinationUseCase.setDestinationStatusUseCase?.invoke(
                SetDestinationStatusUseCase.Params(
                    destination.destinationSeq,
                    DestinationStatus.TRANSIT,
                )
            )
        }

    }
}