package com.namoadigital.prj001.ui.act093

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.namoadigital.prj001.core.IResult.Companion.isFailed
import com.namoadigital.prj001.core.IResult.Companion.isLoading
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.ui.act093.ui.util.Act093Event
import com.namoadigital.prj001.ui.act093.ui.util.Act093State
import com.namoadigital.prj001.ui.act093.usecases.InfoSerialUseCase
import com.namoadigital.prj001.ui.act093.usecases.InfoSerialUseCase.Companion.InfoSerialUseCasesFactory
import com.namoadigital.prj001.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class InfoSerialViewModel constructor(
    private val infoSerialUseCase: InfoSerialUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<Act093State, Act093Event>(Act093State()) {


    private var infoJob: Job? = null

    init {
        viewModelScope.launch {
            getInfoSerial()
        }
    }

    override fun onEvent(event: Act093Event) {
        TODO("Not yet implemented")
    }

    private suspend fun getInfoSerial() {
        infoJob?.cancel()
        infoJob = infoSerialUseCase.getInfoSerial(Unit).onEach {

            it.isLoading { isLoading, message ->
                state = state.copy(isLoading = isLoading)
            }

            it.isSuccess { serial ->

                state = state.copy(
                    serialInfo = Act093State.SerialInfo(
                        iconColor = serial.iconColor,
                        serialId = serial.serialId,
                        product = serial.productDesc,
                        model = serial.convertForBrandModelColor(),
                        trackings = serial.tracklist,
                        last_measure_value = serial.formatMeasureValue,
                        last_measure_date = serial.last_measure_date,
                        last_cycle_value = serial.formatCycleValue
                    ),
                )
            }

            it.isFailed { exception ->
                showSnackbar(exception.message ?: "Ocorreu um erro porem nao tem mensagem.")
            }


        }.launchIn(viewModelScope)

    }


    companion object {

        fun Factory(context: Context): ViewModelProvider.Factory = viewModelFactory {

            initializer {
                val infoUseCase = InfoSerialUseCasesFactory(context).build()
                val savedStateHandle = createSavedStateHandle()
                InfoSerialViewModel(infoUseCase, savedStateHandle)
            }

        }
    }
}