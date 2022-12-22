package com.namoadigital.prj001.ui.act093


import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.IResult.Companion.isFailed
import com.namoadigital.prj001.core.IResult.Companion.isLoading
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.ui.act091.mvp.model.TranslateResource
import com.namoadigital.prj001.ui.act093.usecases.InfoSerialUseCase
import com.namoadigital.prj001.ui.act093.usecases.InfoSerialUseCase.Companion.InfoSerialUseCasesFactory
import com.namoadigital.prj001.ui.act093.util.Act093Event
import com.namoadigital.prj001.ui.act093.util.Act093State
import com.namoadigital.prj001.ui.base.NamoaFactory
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Act093Presenter constructor(
    private val infoUseCase: InfoSerialUseCase,
    private val translateResource: TranslateResource,
) : Contract.Presenter {

    private lateinit var view: Contract.View


    private suspend fun getDeviceList() {
        infoUseCase.getDeviceList(Unit)
            .catch { e ->
                view.onState(Act093Event.Toast(e.message ?: ""))
            }
            .collect {
                it.isLoading { isLoading, message ->
                    _state.value = _state.value.copy(isLoading = isLoading)
                    state = _state
                    view.onState(Act093Event.OnLoading)
                }

                it.isSuccess { response ->
                    _state.value = _state.value.copy(list = response)
                    state = _state
                    view.onState(Act093Event.OnUpdateList)
                }

                it.isFailed { exception ->
                    view.onState(Act093Event.Toast(exception.message ?: ""))
                }
            }
    }


    private suspend fun getInfoSerial() {
        infoUseCase.getInfoSerial(Unit)
            .catch { e ->
                view.onState(Act093Event.Toast(e.message ?: ""))
            }.collect {

                it.isSuccess { serial ->

                    _state.value = _state.value.copy(
                        serialInfo = Act093State.SerialInfo(
                            iconColor = serial.iconColor,
                            serialId = serial.serialId,
                            product = serial.productDesc,
                            model = serial.convertForBrandModelColor(),
                            trackings = serial.tracklist,
                            infoAdd = serial.infoAdd,
                            value_suffix = serial.value_suffix,
                            last_measure_value = serial.last_measure_value,
                            last_measure_date = serial.last_measure_date,
                            last_cycle_value = serial.last_cycle_value,
                            lastUpdateSerial = serial.lastUpdateSerial,
                        ),
                    )

                    state = _state
                    view.onState(Act093Event.OnUpdateScreen)
                }

            it.isFailed { exception ->
                view.onState(Act093Event.Toast(exception.message ?: ""))
            }
        }
    }

    private var _state = MutableStateFlow(Act093State())
    override var state: StateFlow<Act093State> = _state


    override fun setView(view: Contract.View) {
        this.view = view

        CoroutineScope(Dispatchers.IO).launch {
            getInfoSerial()
            getDeviceList()

            if (_state.value.list.isEmpty() &&
                _state.value.serialInfo.last_measure_value == null &&
                _state.value.serialInfo.model == null &&
                _state.value.serialInfo.trackings == null
            ) {
                view.onState(Act093Event.OpenDialog(
                    Act093Event.OpenDialog.DialogType.ACTION(
                        "alert_no_data_warning_title",
                        "alert_no_data_warning_msg",
                        action = { dialog, i ->
                            view.onBack()
                        }
                    )))
            }
        }
    }

    override fun loadTranslation(): HMAux {
        mutableListOf(
            "act093_title",
            "last_measure_lbl",
            "last_cycle_lbl",
            "last_update_serial_lbl",
            "item_with_problem_lbl",
            "item_with_change_expired_lbl",
            "alert_no_data_warning_title",
            "alert_no_data_warning_msg"
        ).let {
            return ToolBox_Inf.setLanguage(
                translateResource.context,
                translateResource.mModule_code,
                translateResource.mResoure_code,
                ToolBox_Con.getPreference_Translate_Code(translateResource.context),
                it
            )
        }
    }


    companion object {


        class Act093PresenterFactory(
            val context: Context,
            val mModule_code: String,
            val mResource_code: String,
        ) : NamoaFactory<Act093Presenter>() {
            override fun build(): Act093Presenter {
                return Act093Presenter(
                    InfoSerialUseCasesFactory(context).build(),
                    TranslateResource(
                        context,
                        mModule_code,
                        mResource_code
                    )
                )
            }
        }


    }
}