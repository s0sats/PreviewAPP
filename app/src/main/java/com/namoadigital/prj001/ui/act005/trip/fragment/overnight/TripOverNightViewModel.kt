package com.namoadigital.prj001.ui.act005.trip.fragment.overnight

import androidx.lifecycle.ViewModel
import com.namoadigital.prj001.core.trip.domain.usecase.destination.DestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripOverNightViewModel  @Inject constructor(
    private val destinationUseCase: DestinationUseCase,
): ViewModel() {

}