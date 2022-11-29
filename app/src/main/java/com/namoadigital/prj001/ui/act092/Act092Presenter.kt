package com.namoadigital.prj001.ui.act092

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.IResult.Companion.isFailed
import com.namoadigital.prj001.core.IResult.Companion.isLoading
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.action_serial.ActionsCache
import com.namoadigital.prj001.service.WS_Serial_Search
import com.namoadigital.prj001.service.WS_Sync
import com.namoadigital.prj001.service.WS_TK_Ticket_Download
import com.namoadigital.prj001.service.WS_UnfocusAndHistoric
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act006.Act006_Main
import com.namoadigital.prj001.ui.act009.Act009_Main
import com.namoadigital.prj001.ui.act011.Act011_Main
import com.namoadigital.prj001.ui.act016.Act016_Main
import com.namoadigital.prj001.ui.act020.Act020_Main
import com.namoadigital.prj001.ui.act068.Act068_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.ui.act071.Act071_Main
import com.namoadigital.prj001.ui.act083.Act083_Main
import com.namoadigital.prj001.ui.act091.mvp.model.TranslateResource
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.act092.usecases.*
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.*
import com.namoadigital.prj001.ui.act092.usecases.ValidateNewFormUseCase.ValidateNewFormParam
import com.namoadigital.prj001.ui.act092.utils.Act092Translate
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent.Companion.DialogType
import com.namoadigital.prj001.util.*
import com.namoadigital.prj001.view.dialog.ScheduleRequestSerialDialog2
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
    private val iconColor: String,
    private val hmAux: HMAux,
    private val actionUseCases: ActionUseCases,
    private val translateResource: TranslateResource,
) : Act092_Contract.Presenter {

    private lateinit var view: Act092_Contract.View

    private var serialDialog: ScheduleRequestSerialDialog2? = null


    private var _serialModel = MutableStateFlow(SerialModel())
    override val serialModel: StateFlow<SerialModel> = _serialModel

    var newActionClick = false

    private val _actionList = mutableListOf<ActionsCache>()
    val actionList: MutableList<ActionsCache> = _actionList


    init {
        if (originFlow == ConstantBaseApp.ACT006 || originFlow == ConstantBaseApp.ACT083) {
            cleanUnfocusAndHistoricalFile()
        }
    }

    private fun cleanUnfocusAndHistoricalFile() {
        ToolBox_Inf.deleteAllFOD(Constant.OTHER_ACTIONS_JSON_PATH)
    }

    private suspend fun loadFilters() {

        if (originFlow == ConstantBaseApp.ACT006 || originFlow == ConstantBaseApp.ACT083) {
            actionUseCases.setPreferences(
                SerialModel(
                    originFlow = originFlow,
                    tagOperCode = myActionFilterParam.tagFilterCode,
                    productId = myActionFilterParam.productId,
                    productCode = myActionFilterParam.productCode,
                    productDesc = myActionFilterParam.productDesc,
                    serialCode = myActionFilterParam.serialCode,
                    serialId = myActionFilterParam.serialId,
                    ticketId = myActionFilterParam.ticketId,
                    calendarDate = myActionFilterParam.calendarDate,
                    lastSelectedPk = myActionFilterParam.paramItemSelectedPk,
                    lastSelectActionType = myActionFilterParam.paramItemSelectedType,
                    classColor = iconColor
                )
            )
        }

        _serialModel.value = actionUseCases.getPreferences().copy(hmAux = hmAux)
        view.onState(Act092UiEvent.UpdateTitleActionSerial)
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
            loadFilters()
            fileOtherActionExists()
            actionUseCases.localTicket(
                Pair(
                    _serialModel.value.copy(
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
        when (_serialModel.value.originFlow) {
            ConstantBaseApp.ACT006 -> {
                view.onState(Act092UiEvent.CallAct(Act006_Main::class.java, bundle.apply {
                    putString(
                        Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER,
                        _serialModel.value.productId
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
                    DialogType.PROCESS(
                        hmAux[Act092Translate.ALERT_SEND_FINISH_TTL],
                        hmAux[Act092Translate.ALERT_SEND_FINISH_MSG]
                    )
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

    private suspend fun fileOtherActionExists() {
        view.onState(
            Act092UiEvent.CheckIfFileExists(
                actionUseCases.checkIfFileExists(
                    _serialModel.value.productCode ?: -1,
                    _serialModel.value.serialCode ?: -1L
                )
            )
        )
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
                            DialogType.PROCESS(
                                hmAux[Act092Translate.DIALOG_DOWNLOAD_TICKET_TTL],
                                hmAux[Act092Translate.DIALOG_DOWNLOAD_TICKET_START]
                            )
                        )
                    )

                    actionUseCases.downloadTicket(action.processPk.replace(".", "|"))

                } else {
                    ToolBox_Inf.showNoConnectionDialog(context)
                }
            }

            MyActions.MY_ACTION_TYPE_SCHEDULE -> {
                CoroutineScope(Dispatchers.IO).launch {
                    actionUseCases.flowScheduleFromMyAction(action)
                        .collect {


                            it.isSuccess { transform ->
                                processActSchedule(
                                    transform.action,
                                    transform.actType,
                                    transform.scheduleExec,
                                    transform.ticketCode,
                                    transform.ticketPrefix,
                                    transform.ticketSeq
                                )
                            }


                            it.isFailed { exception ->

                                if (exception is ScheduleFormException) {
                                    showMsg(
                                        exception.message,
                                        action
                                    )
                                }
                            }

                        }
                }
            }
        }
    }


    private fun showMsg(type: String, item: MyActions) {
        var title: String? = ""
        var msg: String? = ""
        var listener: DialogInterface.OnClickListener? = null
        var btnNegative: Int? = null

        when (type) {
            Act083_Main.MODULE_CHECKLIST_FORM_IN_PROCESSING -> {
                view.onState(
                    Act092UiEvent.OpenDialog(
                        DialogType.DEFAULT_OK(
                            hmAux["alert_ttl_exists_in_processing"],
                            hmAux["alert_msg_exists_in_processing"]
                        )
                    )
                )
            }
            Act083_Main.MODULE_CHECKLIST_START_FORM -> {
                title = hmAux["alert_ttl_start_new_processing"]
                msg = hmAux["alert_msg_start_new_processing"]
                btnNegative = 1
                listener = DialogInterface.OnClickListener { dialogInterface, i ->
                    checkForm(
                        item,
                        actionUseCases.getScheduleFromMyAction(item)
                    )
                }
            }
            Act083_Main.MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR -> {
                title = hmAux["alert_error_on_create_form_ttl"]
                msg = hmAux["alert_error_on_create_form_msg"]
                btnNegative = 0
            }
            Act083_Main.EMPTY_SERIAL_SEARCH -> {
                title = hmAux["alert_no_serial_found_ttl"]
                msg = hmAux["alert_no_serial_found_msg"]
                btnNegative = 0
            }
            Act083_Main.SERIAL_CREATION_DENIED -> {
                title = hmAux["alert_no_serial_found_ttl"]
                msg = hmAux["alert_product_no_allow_new_serial_msg"]
                btnNegative = 0
            }
            Act083_Main.MODULE_TICKET_EXEC_CONFIRM -> {
                title = hmAux["alert_ticket_action_start_ttl"]
                msg = hmAux["alert_ticket_action_start_confirm"]
                btnNegative = 1
                listener = DialogInterface.OnClickListener { dialog, which ->
//                    mPresenter.checkTicketFlow(item)
                }
            }
            Act083_Main.MODULE_SCHEDULE_TICKET_CREATION_ERROR -> {
                title = hmAux["alert_error_on_create_ticket_action_ttl"]
                msg = hmAux["alert_error_on_create_ticket_action_msg"]
                btnNegative = 0
            }
            Act083_Main.MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN -> {
                title = hmAux["alert_schedule_status_prevents_to_open_ttl"]
                msg = hmAux["alert_schedule_status_prevents_to_open_msg"]
                btnNegative = 0
            }
            Act083_Main.PROFILE_PRJ001_AP_NOT_FOUND -> {
                title = hmAux["alert_menu_app_profile_not_found_ttl"]
                msg = hmAux["alert_form_ap_menu_profile_not_found_msg"]
                btnNegative = 0
            }
            Act083_Main.PROFILE_MENU_TICKET_NOT_FOUND -> {
                title = hmAux["alert_menu_app_profile_not_found_ttl"]
                msg = hmAux["alert_ticket_menu_profile_not_found_msg"]
                btnNegative = 0
            }
            Act083_Main.FREE_EXECUTION_BLOCKED -> {
                title = hmAux["alert_free_execution_blocked_ttl"]
                msg = hmAux["alert_free_execution_blocked_msg"]
                btnNegative = 0
            }
        }

        if (btnNegative != null) {
            listener?.let { action ->
                view.onState(
                    Act092UiEvent.OpenDialog(
                        DialogType.ACTION(
                            title,
                            msg,
                            action = action,
                            negativeBtn = btnNegative ?: 0
                        )
                    )
                )
            }
            view.onState(
                Act092UiEvent.OpenDialog(
                    DialogType.DEFAULT_OK(
                        title,
                        msg,
                    )
                )
            )
        }
    }


    private fun checkForm(
        action: MyActions,
        scheduleExec: MD_Schedule_Exec?
    ) {
        scheduleExec?.let {
            if (Constant.SYS_STATUS_SCHEDULE != it.status) {
                view.onState(
                    Act092UiEvent.CallAct(
                        Act011_Main::class.java,
                        getFormFlowBundle(action, scheduleExec)
                    )
                )
            } else if (!it.serial_id.isNullOrBlank() &&
                !it.serial_id.isNullOrEmpty()
            ) {
                buildRequestSerialDialog(
                    scheduleExec,
                    action,
                    false
                )

                executeSerialSearch(
                    action.productCode,
                    action.productId,
                    action.serialId ?: "",
                    true
                )
            } else {
                buildRequestSerialDialog(
                    scheduleExec,
                    action,
                    true
                )
            }
        }
    }


    private fun executeSerialSearch(
        productCode: Int?,
        productId: String?,
        serialId: String,
        searchExact: Boolean,
        myAction: MyActions? = null
    ) {
        if (ToolBox_Con.isOnline(translateResource.context)
            && !ToolBox_Con.getBooleanPreferencesByKey(
                translateResource.context,
                ConstantBaseApp.PREFERENCE_SERIAL_OFFLINE_FLOW,
                false
            )
        ) {
            view.wsProcess.value = WS_Serial_Search::class.java.name
            //

            view.onState(
                Act092UiEvent.OpenDialog(
                    DialogType.PROCESS(
                        hmAux["dialog_serial_search_ttl"],
                        hmAux["dialog_serial_search_start"]
                    )
                )
            )

            actionUseCases.serialSearch(
                productCode.toString(),
                productId.toString(),
                serialId,
                if (searchExact) 1 else 0
            )
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                myAction?.let {
                    actionUseCases.processLocalSearchForSerialAction(
                        ProcessLocalSearchForSerialActionUseCase.ProcessLocalSearchForSerialParam(
                            it,
                            view.bundle
                        )
                    ).collect { result ->

                        result.isSuccess { bund ->
                            view.onState(
                                Act092UiEvent.CallAct(
                                    Act020_Main::class.java,
                                    bund
                                )
                            )
                        }

                        result.isFailed { exception ->
                            view.onState(
                                Act092UiEvent.OpenDialog(
                                    DialogType.DEFAULT_OK(
                                        hmAux["alert_no_serial_found_ttl"],
                                        hmAux["alert_no_serial_found_msg"]
                                    )
                                )
                            )
                        }

                    }
                } ?: TODO("offlineSerialSearch()")
            }
        }
    }


    private fun buildRequestSerialDialog(
        scheduleExec: MD_Schedule_Exec,
        action: MyActions,
        showDialog: Boolean
    ) {
        //
        serialDialog = ScheduleRequestSerialDialog2(
            translateResource.context,
            scheduleExec,
            object : ScheduleRequestSerialDialog2.OnScheduleRequestSerialDialogListeners {
                override fun processToForm() {
                    val bundle = Bundle()
                    //LUCHE - 09/06/2021
                    //Como esse metodo só é chamado quando o usr prosegue SEM SERIAL,serial id
                    //será mudado de null para ""
                    if (scheduleExec.is_so == 0) {
                        scheduleExec.serial_id = scheduleExec.serial_id ?: ""

                        val scheduleLocalExists =
                            actionUseCases.scheduleFormLocalExists(scheduleExec)
                        if (actionUseCases.createFormLocalForSchedule(
                                scheduleLocalExists.first,
                                scheduleExec
                            )
                        ) {
                            action.scheduleCustomFormData =
                                scheduleLocalExists.second?.custom_form_data.toString()
                            //Atualiza fomr_data no item
                            action.scheduleCustomFormData =
                                bundle.getString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, "0")
                            //
                            view.onState(
                                Act092UiEvent.CallAct(
                                    Act011_Main::class.java,
                                    getFormFlowBundle(action, scheduleExec)
                                )
                            )
                        } else {
                            showMsg(MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR, action)

                        }
                    } else {
                        view.onState(
                            Act092UiEvent.OpenDialog(
                                DialogType.DEFAULT_OK(
                                    hmAux["alert_form_os_requires_serial_ttl"],
                                    hmAux["alert_form_os_requires_serial_msg"]
                                )
                            )
                        )
                    }
                }

                override fun processToSearchSerial(serialID: String) {
                    executeSerialSearch(
                        action.productCode,
                        action.productId,
                        serialID,
                        false
                    )
                }

                override fun addMketControl(mketSerial: MKEditTextNM) {
                    /* mView.addControlToActivity(mketSerial)*/
                }

                override fun removeMketControl(mketSerial: MKEditTextNM) {
                    /*mView.removeControlFromActivity(mketSerial)*/
                }
            }
        )
        //
        if (showDialog) {
            serialDialog?.show()
        }
    }

    private fun processActSchedule(
        action: MyActions,
        actType: String,
        scheduleExec: MD_Schedule_Exec,
        ticketCode: Int,
        ticketPrefix: Int,
        ticketSeq: Int
    ) {
        when (actType) {
            Constant.ACT011 -> {
                view.onState(
                    Act092UiEvent.CallAct(
                        Act011_Main::class.java,
                        getFormFlowBundle(
                            action,
                            scheduleExec
                        )
                    )
                )
            }
            Constant.ACT070 -> {
                view.onState(
                    Act092UiEvent.CallAct(
                        Act070_Main::class.java,
                        getTicketFlowBundle(
                            action,
                            ticketPrefix,
                            ticketCode
                        )
                    )
                )
            }
            Constant.ACT071 -> {
                view.onState(
                    Act092UiEvent.CallAct(
                        Act071_Main::class.java,
                        getTicketActionFlowBundle(
                            action,
                            scheduleExec,
                            ticketPrefix,
                            ticketCode,
                            ticketSeq
                        )
                    )
                )
            }
        }
    }


    private fun getTicketActionFlowBundle(
        myAction: MyActions,
        scheduleExec: MD_Schedule_Exec,
        ticket_prefix: Int,
        ticket_code: Int,
        ticket_seq: Int
    ): Bundle {
        val bundle = Bundle()
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(myAction.actionType, myAction.processPk)
        //
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT092)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, originFlow)
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_PREFIX, scheduleExec.schedule_prefix)
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_CODE, scheduleExec.schedule_code)
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_EXEC, scheduleExec.schedule_exec)
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticket_prefix)
        bundle.putInt(TK_TicketDao.TICKET_CODE, ticket_code)
        //16/03/2020 - foi convencionado que durante a criação da execução do ticket, o ticket id,
        //será o igual ao do exibido nas celulas do agendamento.
        bundle.putString(
            TK_TicketDao.TICKET_ID, ToolBox_Inf.getFormattedTicketSeqExec(
                myAction.processPk,
                ticket_prefix.toString(),
                ticket_code.toString()
            )
        )
        //bundle.putString(TK_TicketDao.TYPE_PATH, item.get(TK_TicketDao.TYPE_PATH));
        bundle.putString(TK_TicketDao.TYPE_DESC, scheduleExec.ticket_type_desc)
        bundle.putBoolean(Act070_Main.PARAM_DENIED_BY_CHECKIN, false)
        bundle.putString(Constant.ACT_SELECTED_DATE, _serialModel.value.calendarDate)
        bundle.putString(MD_Schedule_ExecDao.SCHEDULE_PK, myAction.processPk)
        //
        //LUCHE - 14/08/2020 - Criação de action agendado v2
        val ctrlItem = actionUseCases.ticketCtrl(
            GetScheduleCtrlIfExistsUseCase.GetScheduleCtrlIfExistsParam(
                scheduleExec.schedule_prefix.toString(),
                scheduleExec.schedule_code.toString(),
                scheduleExec.schedule_exec.toString(),
                ticket_prefix.toString(),
                ticket_code.toString()
            )
        )
        //Se encontrou o ctrl, é um waiting sync, seta dados para abertura o item
        if (ctrlItem != null) {
            bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, ctrlItem.ticket_seq)
            bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ_TMP, ctrlItem.ticket_seq_tmp)
            bundle.putInt(TK_Ticket_ActionDao.STEP_CODE, ctrlItem.step_code)
            bundle.putBoolean(Act070_Main.PARAM_CTRL_CREATION, false)
            bundle.putBoolean(Act070_Main.PARAM_ACTION_CREATION, false)
        } else {
            bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, 0)
            bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ_TMP, 0)
            bundle.putInt(TK_Ticket_ActionDao.STEP_CODE, 0)
            bundle.putBoolean(Act070_Main.PARAM_CTRL_CREATION, true)
            bundle.putBoolean(Act070_Main.PARAM_ACTION_CREATION, true)
        }
        //
        return bundle
    }

    private fun getTicketFlowBundle(
        action: MyActions,
        ticketPrefix: Int,
        ticketCode: Int
    ): Bundle {
        val bundle = Bundle()
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(action.actionType, action.processPk)
        //
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT092)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, originFlow)
        //
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticketPrefix)
        bundle.putInt(TK_TicketDao.TICKET_CODE, ticketCode)
        //bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, ToolBox_Inf.convertStringToInt(item.get(TK_Ticket_CtrlDao.TICKET_SEQ)))
        bundle.putString(Constant.ACT_SELECTED_DATE, _serialModel.value.calendarDate)
        bundle.putString(MD_Schedule_ExecDao.SCHEDULE_PK, action.processPk)
        return bundle
    }

    private fun getFormFlowBundle(
        action: MyActions,
        scheduleExec: MD_Schedule_Exec
    ): Bundle {
        val bundle = Bundle()
        bundle.putString(MD_ProductDao.PRODUCT_CODE, scheduleExec.product_code.toString())
        bundle.putString(MD_ProductDao.PRODUCT_DESC, scheduleExec.product_desc.toString())
        bundle.putString(MD_ProductDao.PRODUCT_ID, scheduleExec.product_id.toString())
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, scheduleExec.serial_id.toString())
        bundle.putString(
            GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE,
            scheduleExec.custom_form_type.toString()
        )

        bundle.putString(
            GE_Custom_FormDao.CUSTOM_FORM_CODE,
            scheduleExec.custom_form_code.toString()
        )
        bundle.putString(
            GE_Custom_FormDao.CUSTOM_FORM_VERSION,
            scheduleExec.custom_form_version.toString()
        )
        bundle.putString(
            Constant.ACT010_CUSTOM_FORM_CODE_DESC,
            scheduleExec.custom_form_desc.toString()
        )
        //
        action.scheduleCustomFormData?.let {
            bundle.putString(
                GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,
                action.scheduleCustomFormData
            )
        }
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, scheduleExec.site_code.toString())
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(action.actionType, action.processPk)
        //
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT092)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, originFlow)
        return bundle
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
                    DialogType.DEFAULT_OK(
                        hmAux[Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_TTL],
                        hmAux[Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_MSG]
                    )
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
                            DialogType.DEFAULT_OK(
                                hmAux[Act092Translate.PROGRESS_SYNC_TTL],
                                hmAux[Act092Translate.PROGRESS_SYNC_MSG]
                            )
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
                                    DialogType.DEFAULT_OK(
                                        title = hmAux[Act092Translate.ALERT_NO_FORM_TTL],
                                        message = exception.message
                                    )
                                )
                            )
                        } else {
                            view.onState(
                                Act092UiEvent.OpenDialog(
                                    DialogType.DEFAULT_OK(
                                        title = hmAux[Act092Translate.ALERT_PRODUCT_OR_SERIAL_NOT_FOUND_TTL],
                                        message = hmAux[Act092Translate.ALERT_PRODUCT_OR_SERIAL_NOT_FOUND_MSG]
                                    )
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
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, _serialModel.value.originFlow)
        return bundle
    }

    override fun getUnfocusHistoricalList() {
        view.wsProcess.value = WS_UnfocusAndHistoric::class.java.name
        view.onState(
            Act092UiEvent.OpenDialog(
                DialogType.PROCESS(
                    hmAux["alert_send_finish_ttl"],
                    hmAux["alert_send_finish_msg"]
                )
            )
        )
        actionUseCases.unfocusHistoricalAction(
            myActionFilterParam.productCode!!,
            _serialModel.value.serialCode ?: 0L
        )
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

    companion object {
        const val MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR =
            "MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR"
    }
}