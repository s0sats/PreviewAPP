package com.namoadigital.prj001.ui.act092

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.IResult.Companion.isFailed
import com.namoadigital.prj001.core.IResult.Companion.isLoading
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.action_serial.ActionsCache
import com.namoadigital.prj001.service.WS_UnfocusAndHistoric
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act006.Act006_Main
import com.namoadigital.prj001.ui.act016.Act016_Main
import com.namoadigital.prj001.ui.act068.Act068_Main
import com.namoadigital.prj001.ui.act083.Act083_Main
import com.namoadigital.prj001.ui.act092.model.LocalTicketsModel
import com.namoadigital.prj001.ui.act092.usecases.ActionUseCases
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Act092Presenter constructor(
    private var myActionFilterParam: MyActionFilterParam,
    private val originFlow: String?,
    private val hmAux: HMAux,
    private val actionUseCases: ActionUseCases,
) : Act092_Contract.Presenter {

    private lateinit var view: Act092_Contract.View

    private var _ticketModel = MutableStateFlow(LocalTicketsModel())
    private val ticketModel: StateFlow<LocalTicketsModel> = _ticketModel


    private val _actionList = mutableListOf<ActionsCache>()
    val actionList: MutableList<ActionsCache> = _actionList


    init {
        loadFilters()
        getMyActionList()
    }

    private fun loadFilters() {
        _ticketModel.value = ticketModel.value.copy(
            originFlow = originFlow,
            tagOperCode = myActionFilterParam.tagFilterCode,
            productCode = myActionFilterParam.productCode,
            serialId = myActionFilterParam.serialId,
            ticketId = myActionFilterParam.ticketId,
            calendarDate = myActionFilterParam.calendarDate,
            lastSelectedPk = myActionFilterParam.paramItemSelectedPk,
            lastSelectActionType = myActionFilterParam.paramItemSelectedType,
            hmAux = hmAux,
        )
    }

    override fun getMyActionList(userFocus: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val isFocus = if (userFocus) 1 else 0
            actionUseCases.localTicket(ticketModel.value.copy(userFocus = isFocus))
                .catch { e ->
                    emit(loading(false))
                    view.onState(Act092UiEvent.ShowSnackbar(e.message ?: "not found"))
                }
                .collect {

                    it.isSuccess { list ->
                        view.onState(Act092UiEvent.ListingSerialSteels(list))
                    }

                    it.isFailed {
                        view.onState(Act092UiEvent.ShowSnackbar(it.toString()))
                    }

                    it.isLoading { isLoading, message ->
                        view.onState(Act092UiEvent.IsLoading(isLoading, message))
                    }

                }
        }
    }

    override fun onBackPressedClicked(bundle: Bundle) {
        when (myActionFilterParam.originFlow) {
            ConstantBaseApp.ACT006 -> {
                view.onState(Act092UiEvent.CallAct(Act006_Main::class.java, bundle.apply {
                    putString(
                        Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER,
                        myActionFilterParam.productId
                    )
                    putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, myActionFilterParam.serialId)
                }))
            }
            ConstantBaseApp.ACT016 -> {
                view.onState(Act092UiEvent.CallAct(Act016_Main::class.java, bundle.apply {
                    putString(ConstantBaseApp.ACT_SELECTED_DATE, myActionFilterParam.calendarDate)
                }))
            }
            ConstantBaseApp.ACT068 -> view.onState(Act092UiEvent.CallAct(Act068_Main::class.java))
            ConstantBaseApp.ACT083 -> view.onState(
                Act092UiEvent.CallAct(
                    Act083_Main::class.java,
                    bundle.apply {

                        val productCode: Int? =
                            if (ConstantBaseApp.ACT083 == originFlow) myActionFilterParam.productCode else null
                        val productId: String? =
                            if (ConstantBaseApp.ACT083 == originFlow) myActionFilterParam.productId else null
                        val productDesc: String? =
                            if (ConstantBaseApp.ACT083 == originFlow) myActionFilterParam.productDesc else null
                        val serialId: String? =
                            if (ConstantBaseApp.ACT083 == originFlow) myActionFilterParam.serialId else null


                        myActionFilterParam = ToolBox_Inf.getMyActionFilterParam(bundle)

                        myActionFilterParam.productCode = productCode
                        myActionFilterParam.productId = productId
                        myActionFilterParam.productDesc = productDesc
                        myActionFilterParam.serialId = serialId

                        putSerializable(
                            MyActionFilterParam.MY_ACTION_FILTER_PARAM,
                            myActionFilterParam
                        )
                        putString(
                            getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW),
                            ConstantBaseApp.ACT006
                        )
                    })
            )
            else -> view.onState(Act092UiEvent.CallAct(Act005_Main::class.java))
        }
    }


    override fun syncFiles(context: Context) {
        if (ToolBox_Con.isOnline(context)) {
            view.wsProcess.value = Act005_Main.WS_PROCESS_SYNC
            view.onState(
                Act092UiEvent.OpenDialog(
                    true,
                    "alert_send_finish_ttl",
                    "alert_send_finish_msg"
                )
            )
            actionUseCases.syncFiles(hmAux)
        } else {
            view.onState(
                Act092UiEvent.OpenDialog(
                    title = "Sem conexão",
                    message = "Sem conexão com a internet."
                )
            )
        }

    }

    override fun getUnfocusHistoricalList(context: Context, serialCode: Long) {
        view.wsProcess.value = WS_UnfocusAndHistoric.javaClass.simpleName
        view.onState(
            Act092UiEvent.OpenDialog(
                true,
                "alert_send_finish_ttl",
                "alert_send_finish_msg"
            )
        )
        actionUseCases.unfocusHistoricalActionUseCases(myActionFilterParam.productCode!!, serialCode)
    }


    override fun setView(view: Act092_Contract.View) {
        this.view = view
    }

    override fun loadTranslation(): HMAux {
        TODO("Not yet implemented")
    }
}