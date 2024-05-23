package com.namoadigital.prj001.ui.act005.trip.fragment.home

import androidx.lifecycle.ViewModel
import com.namoadigital.prj001.core.trip.domain.usecase.CreateTripUseCase
import com.namoadigital.prj001.model.location.Coordinates
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripHomeViewModel @Inject constructor(
    private val createTrip: CreateTripUseCase
):ViewModel() {


    fun createNewTrip(coordinates: Coordinates? = null) {
        createTrip(coordinates)
    }

}