package com.namoadigital.prj001.ui.act005.trip.fragment.home

import androidx.lifecycle.ViewModel
import com.namoadigital.prj001.core.domain.usecase.form_local.HasFormInProcessUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.CreateTripUseCase
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.ui.act095.event_manual.domain.usecases.GetEventManualUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripHomeViewModel @Inject constructor(
    private val createTrip: CreateTripUseCase,
    private val getEventManualUseCase: GetEventManualUseCase,
    private val formInProcessUseCase: HasFormInProcessUseCase
) : ViewModel() {


    fun hasEventManual(): Boolean {
        val event = getEventManualUseCase(Unit)
        return event != null
    }

    fun createNewTrip(coordinates: Coordinates? = null) {
        createTrip(coordinates)
    }

    fun hasFormInProcess(): Boolean {
        val form = formInProcessUseCase(Unit)
        return form.isNotEmpty()
    }

}