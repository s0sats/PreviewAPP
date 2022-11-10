package com.namoadigital.prj001.ui.act092

import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.action_serial.ActionsCache
import com.namoadigital.prj001.ui.act092.core.IResult.Companion.isFailed
import com.namoadigital.prj001.ui.act092.core.IResult.Companion.isLoading
import com.namoadigital.prj001.ui.act092.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.ui.act092.core.IResult.Companion.loading
import com.namoadigital.prj001.ui.act092.model.LocalTicketsModel
import com.namoadigital.prj001.ui.act092.usecases.ActionUseCases
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class Act092Presenter constructor(
    private val myActionFilterParam: MyActionFilterParam,
    private val originFlow: String?,
    private val hmAux: HMAux,
    private val actionUseCases: ActionUseCases,
) : Act092_Contract.Presenter {


    private lateinit var view: Act092_Contract.View

    private var _ticketModel = MutableStateFlow(LocalTicketsModel())
    private val ticketModel: StateFlow<LocalTicketsModel> = _ticketModel


    private val _actionList = mutableListOf<ActionsCache>()
    val actionList: MutableList<ActionsCache> = _actionList


    private var actionJob: Job? = null


    init {
        loadFilters()
        CoroutineScope(Dispatchers.Default).launch {
            getMyActionList()
        }

    }

    private fun loadFilters() {
        _ticketModel.value = ticketModel.value.copy(
            originFlow = originFlow,
            tagOperCode = myActionFilterParam.tagFilterCode,
            productCode = myActionFilterParam.productCode,
            serialId = myActionFilterParam.serialId,
            ticketId = myActionFilterParam.ticketId,
            clientId = myActionFilterParam.clientId,
            contractId = myActionFilterParam.contractId,
            calendarDate = myActionFilterParam.calendarDate,
            lastSelectedPk = myActionFilterParam.paramItemSelectedPk,
            lastSelectActionType = myActionFilterParam.paramItemSelectedType,
            hmAux = hmAux,
            userFocus = 1
        )
    }

    override fun getMyActionList() {
        CoroutineScope(Dispatchers.IO).launch {
            actionUseCases.localTicket(ticketModel.value)
                .onEach {

                    it.isSuccess {
                        view.onState(Act092UiEvent.ListingSerialSteels(it))
                    }

                    it.isFailed {
                        view.onState(Act092UiEvent.ShowSnackbar(it.toString()))
                    }

                    it.isLoading { isLoading, message ->
                        view.onState(Act092UiEvent.IsLoading(isLoading, message))
                    }

                }.catch { e ->
                    emit(loading(false))
                    view.onState(Act092UiEvent.ShowSnackbar(e.message ?: "not found"))
                }.launchIn(CoroutineScope(Dispatchers.IO))
        }
    }

    override fun onBackPressedClicked() {
        when (myActionFilterParam.originFlow) {
            ConstantBaseApp.ACT006 -> view.callAct006(getBundleToAssetsAndLocalOrigin())
            ConstantBaseApp.ACT016 -> view.callAct016(getBundleToCalendarOrigin())
            ConstantBaseApp.ACT068 -> view.callAct068()
            else -> view.callAct005()
        }
    }


    private fun getBundleToAssetsAndLocalOrigin() = Bundle().apply {
        putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, myActionFilterParam.productId)
        putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, myActionFilterParam.serialId)
    }

    private fun getBundleToCalendarOrigin() = Bundle().apply {
        putString(ConstantBaseApp.ACT_SELECTED_DATE, myActionFilterParam.calendarDate)
    }

    override fun setView(view: Act092_Contract.View) {
        this.view = view
    }

    override fun loadTranslation(): HMAux {
        TODO("Not yet implemented")
    }
}