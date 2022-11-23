package com.namoadigital.prj001.ui.act092

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.IResult.Companion.isFailed
import com.namoadigital.prj001.core.IResult.Companion.isLoading
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.action_serial.ActionsCache
import com.namoadigital.prj001.service.WS_Sync
import com.namoadigital.prj001.service.WS_TK_Ticket_Download
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act006.Act006_Main
import com.namoadigital.prj001.ui.act009.Act009_Main
import com.namoadigital.prj001.ui.act016.Act016_Main
import com.namoadigital.prj001.ui.act068.Act068_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.ui.act083.Act083_Main
import com.namoadigital.prj001.ui.act091.mvp.model.TranslateResource
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.act092.usecases.ActionUseCases
import com.namoadigital.prj001.ui.act092.usecases.ValidateNewFormUseCase.ValidateNewFormParam
import com.namoadigital.prj001.ui.act092.utils.Act092Translate
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.util.*
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
    private val translateResource: TranslateResource,
) : Act092_Contract.Presenter {

    private lateinit var view: Act092_Contract.View

    private var _serialModel = MutableStateFlow(SerialModel())
    val serialModel: StateFlow<SerialModel> = _serialModel

    var newActionClick = false

    private val _actionList = mutableListOf<ActionsCache>()
    val actionList: MutableList<ActionsCache> = _actionList


    init {
        loadFilters()
    }

    private fun loadFilters() {
        _serialModel.value = _serialModel.value.copy(
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
        )
    }

    override fun verifyProductOutdateForForm(hmAux: HMAux, context: Context): Boolean {
        val ticketPrefix = hmAux[TK_TicketDao.TICKET_PREFIX]?.let { Integer.valueOf(it) } ?: -1
        val ticketCode = hmAux[TK_TicketDao.TICKET_CODE]?.let { Integer.valueOf(it) } ?: -1
        //
        return ToolBox_Inf.hasFormProductOutdate(context, ticketPrefix, ticketCode)
    }

    override fun getCacheTicketBundle(hmAuxTicketDownloaded: HMAux): Bundle {
        val ticketPrefix =
            hmAuxTicketDownloaded[TK_TicketDao.TICKET_PREFIX]?.let { Integer.valueOf(it) } ?: -1
        val ticketCode =
            hmAuxTicketDownloaded[TK_TicketDao.TICKET_CODE]?.let { Integer.valueOf(it) } ?: -1
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(
            MyActions.MY_ACTION_TYPE_TICKET, "$ticketPrefix.$ticketCode"
        )
        //
        return ticketBundle(ticketPrefix, ticketCode)
    }

    override fun getMyActionList(mainFocus: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            actionUseCases.localTicket(
                Pair(
                    serialModel.value.copy(
                        userFocus = view.focusState.value.userFocusInt
                    ), mainFocus
                )
            )
                .catch { e ->
                    emit(loading(false))
                    view.onState(Act092UiEvent.ShowSnackbar(e.message ?: "not found"))
                }
                .collect {

                    it.isSuccess { list ->
                        view.onState(Act092UiEvent.ListingSerialSteels(list))
                    }

                    it.isFailed { throwable ->
                        view.onState(Act092UiEvent.ShowSnackbar(throwable.toString()))
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
                    putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, _serialModel.value.serialId)
                }))
            }
            ConstantBaseApp.ACT016 -> {
                view.onState(Act092UiEvent.CallAct(Act016_Main::class.java, bundle.apply {
                    putString(ConstantBaseApp.ACT_SELECTED_DATE, _serialModel.value.calendarDate)
                }))
            }
            ConstantBaseApp.ACT068 -> view.onState(Act092UiEvent.CallAct(Act068_Main::class.java))
            ConstantBaseApp.ACT083 -> view.onState(
                Act092UiEvent.CallAct(
                    Act083_Main::class.java,
                    bundle.apply {

                        myActionFilterParam = ToolBox_Inf.getMyActionFilterParam(bundle)

                        myActionFilterParam.productCode = null
                        myActionFilterParam.productId = null
                        myActionFilterParam.productDesc = null
                        myActionFilterParam.serialId = null

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
                    hmAux[Act092Translate.ALERT_SEND_FINISH_TTL],
                    hmAux[Act092Translate.ALERT_SEND_FINISH_MSG]
                )
            )
            actionUseCases.syncFiles(hmAux)
        } else {
            ToolBox_Inf.showNoConnectionDialog(context)
        }

    }

    override fun syncFilesForm(productCode: Long) {
        actionUseCases.syncFilesForm(hmAux, productCode)
    }

    override fun processActionClick(action: MyActions, context: Context) {
        when (action.actionType) {
            MyActions.MY_ACTION_TYPE_TICKET -> {
                val slippedPk = action.getSplippedPk()
                setSeletedActionInfosIntoFilterParam(
                    action.actionType,
                    action.processPk,
                    action.isMainUserTicket
                )

                view.onState(
                    Act092UiEvent.CallAct(
                        Act070_Main::class.java,
                        ticketBundle(slippedPk[0].toInt(), slippedPk[1].toInt())
                    )
                )
            }

            MyActions.MY_ACTION_TYPE_TICKET_CACHE -> {
                if (ToolBox_Con.isOnline(context)) {
                    view.wsProcess.value = WS_TK_Ticket_Download::class.java.name
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            true,
                            hmAux[Act092Translate.DIALOG_DOWNLOAD_TICKET_TTL],
                            hmAux[Act092Translate.DIALOG_DOWNLOAD_TICKET_START]
                        )
                    )

                    actionUseCases.downloadTicket(action.processPk.replace(".", "|"))

                } else {
                    ToolBox_Inf.showNoConnectionDialog(context)
                }
            }

        }
    }

    override fun processWsReturnSync(hmAuxTicketDownload: HMAux) {
        CoroutineScope(Dispatchers.Main).launch {
            if (newActionClick) {
                actionUseCases.updateSyncCheckist(_serialModel.value.productCode?.toLong() ?: -1L)
                ToolBox_Inf.scheduleAllDownloadWorkers(translateResource.context)
                validateCreateNewForm()
            } else {
                view.onState(
                    Act092UiEvent.CallAct(
                        Act070_Main::class.java,
                        getCacheTicketBundle(hmAuxTicketDownload)
                    )
                )
            }
        }
    }

    override fun processNewFormClick(context: Context) {
        if (ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context)) {
            view.onState(
                Act092UiEvent.OpenDialog(
                    false,
                    hmAux[Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_TTL],
                    hmAux[Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_MSG]
                )
            )
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            if (actionUseCases.checkSyncChecklist(_serialModel.value.productCode ?: -1)
                    .isNotEmpty()
            ) {
                validateCreateNewForm()
            } else {
                if (ToolBox_Con.isOnline(context) && !ToolBox_Con.getBooleanPreferencesByKey(
                        context,
                        ConstantBaseApp.PREFERENCE_SERIAL_OFFLINE_FLOW,
                        false
                    )
                ) {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            true,
                            hmAux[Act092Translate.PROGRESS_SYNC_TTL],
                            hmAux[Act092Translate.PROGRESS_SYNC_MSG]
                        )
                    )
                    newActionClick = true
                    view.wsProcess.value = WS_Sync::class.java.name
                    actionUseCases.syncFilesForm(
                        hmAux,
                        _serialModel.value.productCode?.toLong() ?: -1L
                    )
                }
            }
        }

    }

    private suspend fun validateCreateNewForm() {
        actionUseCases.validateNewForm(ValidateNewFormParam(_serialModel.value, hmAux)).collect {
            it.isSuccess { bundle ->
                myActionFilterParam.paramTextFilter = view.filterText.value
                myActionFilterParam.mainUserFilterState = view.focusState.value.mainUser
                myActionFilterParam.paramItemSelectedTab = view.focusState.value.userFocusInt
                myActionFilterParam.paramItemSelectedPk = null
                myActionFilterParam.paramItemSelectedType = null

                view.onState(Act092UiEvent.CallAct(
                    Act009_Main::class.java,
                    bundle.apply {
                        putSerializable(
                            MyActionFilterParam.MY_ACTION_FILTER_PARAM,
                            myActionFilterParam
                        )
                    }
                ))
            }

            it.isFailed { exception ->
                when (exception) {
                    is ValidateNewFormUseCaseException -> {
                        if (exception.message != "ALERT_PRODUCT_OR_SERIAL") {
                            view.onState(
                                Act092UiEvent.OpenDialog(
                                    title = hmAux[Act092Translate.ALERT_NO_FORM_TTL],
                                    message = exception.message
                                )
                            )
                        } else {
                            view.onState(
                                Act092UiEvent.OpenDialog(
                                    title = hmAux[Act092Translate.ALERT_PRODUCT_OR_SERIAL_NOT_FOUND_TTL],
                                    message = hmAux[Act092Translate.ALERT_PRODUCT_OR_SERIAL_NOT_FOUND_MSG]
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setSeletedActionInfosIntoFilterParam(
        myActionType: String,
        myActionPk: String,
        mainFocus: Boolean = view.focusState.value.mainUser
    ) {
        myActionFilterParam.originFlow = originFlow ?: ConstantBaseApp.ACT005
        myActionFilterParam.setSelectedItemParams(
            view.filterText.value,
            view.focusState.value.userFocusInt,
            myActionType,
            myActionPk,
            null,
            mainFocus
        )
    }

    private fun ticketBundle(ticketPrefix: Int, ticketCode: Int): Bundle {
        val bundle = Bundle()
        bundle.putString(ConstantBaseApp.ACT092, ConstantBaseApp.ACT092)
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticketPrefix)
        bundle.putInt(TK_TicketDao.TICKET_CODE, ticketCode)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, originFlow)
        return bundle
    }


    override fun setView(view: Act092_Contract.View) {
        this.view = view
    }

    override fun loadTranslation(): HMAux {
        mutableListOf(
            Act092Translate.DIALOG_UPDATE_TTL,
            Act092Translate.DIALOG_UPDATE_MSG,
            Act092Translate.EMPTY_LIST,
            Act092Translate.ALERT_SEND_FINISH_TTL,
            Act092Translate.ALERT_SEND_FINISH_MSG,
            Act092Translate.DIALOG_DOWNLOAD_TICKET_TTL,
            Act092Translate.DIALOG_DOWNLOAD_TICKET_START,
            Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_TTL,
            Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_MSG,
            Act092Translate.PROGRESS_SYNC_TTL,
            Act092Translate.PROGRESS_SYNC_MSG,
            Act092Translate.ALERT_NO_FORM_TTL,
            Act092Translate.ALERT_PRODUCT_OR_SERIAL_NOT_FOUND_TTL,
            Act092Translate.ALERT_PRODUCT_OR_SERIAL_NOT_FOUND_MSG,
            Act092Translate.ALERT_NO_FORM_PRODUCT_MSG,
            Act092Translate.ALERT_NOT_FORM_OPERATION_MSG,
            Act092Translate.ALERT_NO_FORM_FOR_SITE_MSG,
            Act092Translate.ALERT_SITE_RESTRICTION_VIOLATION_MSG,
        ).let {
            return ToolBox_Inf.setLanguage(
                translateResource.context,
                translateResource.mModule_code,
                translateResource.nResoure_code,
                ToolBox_Con.getPreference_Translate_Code(translateResource.context),
                it
            )
        }
    }
}