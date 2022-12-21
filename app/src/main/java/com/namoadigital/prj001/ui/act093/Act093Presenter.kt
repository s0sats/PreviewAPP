package com.namoadigital.prj001.ui.act093


import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.IResult.Companion.isFailed
import com.namoadigital.prj001.core.IResult.Companion.isLoading
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.ui.act093.usecases.InfoSerialUseCase
import com.namoadigital.prj001.ui.act093.util.Act093Event
import com.namoadigital.prj001.ui.act093.util.Act093State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class Act093Presenter constructor(
    private val infoUseCase: InfoSerialUseCase,
) : Contract.Presenter {

    private lateinit var view: Contract.View

    private var infoJob: Job? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getInfoSerial()
        }
    }


    private suspend fun getInfoSerial() {
        infoJob?.cancel()
        infoJob = infoUseCase.getInfoSerial(Unit).onEach {

            it.isLoading { isLoading, message ->
                _state.value = _state.value.copy(isLoading = isLoading)
            }

            it.isSuccess { serial ->

                _state.value = _state.value.copy(
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

                state = _state
                view.onState(Act093Event.onUpdateScreen)
            }

            it.isFailed { exception ->

            }


        }.launchIn(CoroutineScope(Dispatchers.IO))

    }

    private var _state = MutableStateFlow(Act093State())
    override var state: StateFlow<Act093State> = _state


    override fun setView(view: Contract.View) {
        this.view = view
    }

    override fun loadTranslation(): HMAux {
        return HMAux()
    }
}