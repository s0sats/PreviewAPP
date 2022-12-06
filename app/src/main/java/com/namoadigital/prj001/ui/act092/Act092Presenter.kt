package com.namoadigital.prj001.ui.act092

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.IResult.Companion.isFailed
import com.namoadigital.prj001.core.IResult.Companion.isLoading
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.model.action_serial.ActionsCache
import com.namoadigital.prj001.receiver.WBR_Save
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save
import com.namoadigital.prj001.service.*
import com.namoadigital.prj001.sql.Sql_Act005_002
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act006.Act006_Main
import com.namoadigital.prj001.ui.act009.Act009_Main
import com.namoadigital.prj001.ui.act011.Act011_Main
import com.namoadigital.prj001.ui.act016.Act016_Main
import com.namoadigital.prj001.ui.act020.Act020_Main
import com.namoadigital.prj001.ui.act033.Act033_Main
import com.namoadigital.prj001.ui.act068.Act068_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.ui.act071.Act071_Main
import com.namoadigital.prj001.ui.act083.Act083_Main
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.EMPTY_SERIAL_SEARCH
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.MODULE_SCHEDULE_TICKET_CREATION_ERROR
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.MODULE_TICKET_EXEC_CONFIRM
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.PROFILE_MENU_TICKET_NOT_FOUND
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.PROFILE_PRJ001_AP_NOT_FOUND
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.SERIAL_CREATION_DENIED
import com.namoadigital.prj001.ui.act087.Act087Main
import com.namoadigital.prj001.ui.act091.mvp.model.TranslateResource
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.act092.usecases.ActionUseCases
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.MODULE_CHECKLIST_FORM_IN_PROCESSING
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.SERIAL_SITE_OUT_OF_LICENSE
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.SITE_RESTRICTION_CONFIRM
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.SITE_RESTRICTION_NO_ACCESS
import com.namoadigital.prj001.ui.act092.usecases.GetScheduleCtrlIfExistsUseCase
import com.namoadigital.prj001.ui.act092.usecases.ProcessLocalSearchForSerialActionUseCase.ProcessLocalSearchForSerialParam
import com.namoadigital.prj001.ui.act092.usecases.ScheduleFormException
import com.namoadigital.prj001.ui.act092.usecases.ValidateNewFormUseCase.ValidateNewFormParam
import com.namoadigital.prj001.ui.act092.utils.Act092Translate
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent.OpenDialog.DialogType
import com.namoadigital.prj001.util.*
import com.namoadigital.prj001.view.dialog.ScheduleRequestSerialDialog2
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.io.File

class Act092Presenter constructor(
    private var myActionFilterParam: MyActionFilterParam,
    private val originFlow: String?,
    private val iconColor: String,
    private val actionUseCases: ActionUseCases,
    private val translateResource: TranslateResource,
) : Act092_Contract.Presenter {

    private var actionSelected: MyActions? = null

    private lateinit var view: Act092_Contract.View

    private var serialDialog: ScheduleRequestSerialDialog2? = null

    private var launch : Job? = null

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
                    siteCodeBack = ToolBox_Con.getPreference_Site_Code(translateResource.context),
                    zoneCodeBack = ToolBox_Con.getPreference_Zone_Code(translateResource.context),
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

        _serialModel.value = actionUseCases.getPreferences().copy(hmAux = hmAux_Trans)
        view.onState(Act092UiEvent.UpdateTitleActionSerial)
    }

    override fun verifyProductOutdateForForm(hmAux: HMAux, context: Context): Boolean {
        val ticketPrefix = hmAux[TK_TicketDao.TICKET_PREFIX].let { Integer.valueOf(it) } ?: -1
        val ticketCode = hmAux[TK_TicketDao.TICKET_CODE].let { Integer.valueOf(it) } ?: -1
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
                        Act092Translate.ALERT_SEND_FINISH_TTL,
                        Act092Translate.ALERT_SEND_FINISH_MSG
                    )
                )
            )
            actionUseCases.syncFiles(hmAux_Trans)
        } else {
            ToolBox_Inf.showNoConnectionDialog(context)
        }

    }

    override fun syncFilesForm(productCode: Long) {
        actionUseCases.syncFilesForm(hmAux_Trans, productCode)
    }

    private fun fileOtherActionExists() {
        if (actionUseCases.checkIfFileExists(
                _serialModel.value.productCode ?: -1,
                _serialModel.value.serialCode ?: -1L
            )
        ) {
            view.onState(
                Act092UiEvent.CheckIfFileExists(true)
            )
        }
    }

    override fun processActionClick(action: MyActions, context: Context, position: Int) {
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
                                Act092Translate.DIALOG_DOWNLOAD_TICKET_TTL,
                                Act092Translate.DIALOG_DOWNLOAD_TICKET_START
                            )
                        )
                    )

                    actionUseCases.downloadTicket(action.processPk.replace(".", "|"))

                } else {
                    ToolBox_Inf.showNoConnectionDialog(context)
                }
            }

            MyActions.MY_ACTION_TYPE_SCHEDULE -> {
                checkScheduleFlow(action)
            }

            MyActions.MY_ACTION_TYPE_FORM -> {
                if(!action.pdfUrl.isNullOrEmpty()){
                    executeNFormPDFGeneration(context, action, position)
                }else{
                    view.onState(
                        Act092UiEvent.CallAct(
                            Act011_Main::class.java,
                            getFormBundle(action)
                        )
                    )
                }
            }
        }
    }

    private fun getFormBundle(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        val bundle = Bundle()
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(myAction.actionType, myAction.processPk)
        //
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT092)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, Constant.ACT092)
        //
        bundle.putString(MD_ProductDao.PRODUCT_CODE, myAction.productCode.toString())
        bundle.putString(MD_ProductDao.PRODUCT_ID, myAction.productId.toString())
        bundle.putString(MD_ProductDao.PRODUCT_DESC, myAction.productDesc)
        //bundle.putString(MD_ProductDao.PRODUCT_ID, myAction.productDesc)
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, myAction.serialId)
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, splippedPk[0])
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, splippedPk[1])
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, splippedPk[2])
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, myAction.customFormDesc)
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, splippedPk[3])
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, myAction.siteCode.toString())
        return bundle
    }

    override fun checkScheduleFlow(action: MyActions) {
        CoroutineScope(Dispatchers.IO).launch {
            actionSelected = action
            actionUseCases.flowScheduleFromMyAction(action)
                .collect {


                    it.isSuccess { transform ->
                        processActSchedule(
                            action,
                            transform.actType,
                            transform.scheduleExec,
                            transform.productSerial
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

    override fun callFormSave(context: Context) {
        if(ToolBox_Con.isOnline(context)) {
            view.wsProcess.value = WS_Save::class.java.simpleName
            //

            view.onState(
                Act092UiEvent.OpenDialog(
                    DialogType.PROCESS(
                        "progress_form_save_ttl",
                        "progress_form_save_msg"
                    )
                )
            )
            val mIntent = Intent(context, WBR_Save::class.java)
            val bundle = Bundle()
            bundle.putInt(Constant.GC_STATUS_JUMP, 1) //Pula validação Update require

            bundle.putInt(Constant.GC_STATUS, 1) //Pula validação de other device

            bundle.putString(Act005_Main.WS_PROCESS_SO_STATUS, "SEND")

            mIntent.putExtras(bundle)
            //
            //
            context.sendBroadcast(mIntent)
            ToolBox_Inf.sendBCStatus(
                context,
                "STATUS",
                hmAux_Trans["msg_preparing_to_send_data"],
                "",
                "0"
            )
        }else{
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }

    override fun callTicketSave(context: Context) {
        if(ToolBox_Con.isOnline(context)) {
            view.wsProcess.value = WS_TK_Ticket_Save::class.java.simpleName
            //
            view.onState(
                Act092UiEvent.OpenDialog(
                    DialogType.PROCESS(
                        "progress_ticket_save_ttl",
                        "progress_ticket_save_msg"

                    )
                )
            )
            //
            val mIntent = Intent(context, WBR_TK_Ticket_Save::class.java)
            val bundle = Bundle()
            mIntent.putExtras(bundle)
            //
            //
            context.sendBroadcast(mIntent)
        }else{
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }

    override fun otherActionFlow(context: Context) {
        if(ToolBox_Con.isOnline(context)){
            val hasFormPendency = getFormPendency(context)
            val hasTicketPendency = getTicketPendency(context)
            if(hasFormPendency){
                callFormSave(context)
            }else if (hasTicketPendency){
                callTicketSave(context)
            }else{
                getUnfocusHistoricalList(context)
            }
        }else{
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }

    private fun getTicketPendency(context: Context): Boolean {
        return ToolBox_Inf.handleTicketUpdateRequired(context, ToolBox_Con.getPreference_Customer_Code(context)).toInt() > 0
    }

    private fun getFormPendency(context: Context): Boolean {
        val customFormLocalDao = GE_Custom_Form_LocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )

        return try {
            val qty = customFormLocalDao.getByStringHM(
                Sql_Act005_002(
                    ToolBox_Con.getPreference_Customer_Code(context).toString()
                ).toSqlQuery()
            )[Sql_Act005_002.BADGE_WAITING_SYNC_QTY]
            qty!!.toInt() > 0
        } catch (e: Exception) {
            false
        }
    }

    private fun showMsg(type: String, item: MyActions) {
        CoroutineScope(Dispatchers.Main).launch {
            when (type) {

                SITE_RESTRICTION_NO_ACCESS -> {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            DialogType.ACTION(
                                title = Act092Translate.ALERT_FORM_SITE_RESTRICTION_TTL,
                                message = Act092Translate.ALERT_FORM_SITE_RESTRICTION_NO_ACCESS_MSG,
                                action = { dialog, i ->

                                }
                            )
                        )
                    )
                }

                SITE_RESTRICTION_CONFIRM -> {
                    view.onState(Act092UiEvent.OpenDialog(
                        DialogType.ACTION(
                            title = Act092Translate.ALERT_FORM_SITE_RESTRICTION_TTL,
                            message = Act092Translate.ALERT_FORM_SITE_RESTRICTION_NO_ACCESS_MSG,
                            { dialog, i ->
                                noRestriction(item)
                            }, negativeBtn = 1
                        )
                    ))
                }
                MODULE_CHECKLIST_FORM_IN_PROCESSING -> {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_TTL_EXISTS_IN_PROCESSING,
                                Act092Translate.ALERT_MSG_EXISTS_IN_PROCESSING
                            )
                        )
                    )
                }
                MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR -> {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_ERROR_ON_CREATE_FORM_TTL,
                                Act092Translate.ALERT_ERROR_ON_CREATE_FORM_MSG
                            )
                        )
                    )
                }
                EMPTY_SERIAL_SEARCH -> {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_NO_SERIAL_FOUND_TTL,
                                Act092Translate.ALERT_NO_SERIAL_FOUND_MSG
                            )
                        )
                    )
                }
                SERIAL_CREATION_DENIED -> {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_PRODUCT_OR_SERIAL_NOT_FOUND_TTL,
                                Act092Translate.ALERT_PRODUCT_NO_ALLOW_NEW_SERIAL_MSG
                            )
                        )
                    )
                }
                MODULE_TICKET_EXEC_CONFIRM -> {
                    view.onState(Act092UiEvent.OpenDialog(
                        DialogType.ACTION(
                            title = Act092Translate.ALERT_TICKET_ACTION_START_TTL,
                            message = Act092Translate.ALERT_TICKET_ACTION_START_CONFIRM,
                            { dialog, i ->
                                checkTicketFlow(item)

                            }
                        )
                    ))
                }
                MODULE_SCHEDULE_TICKET_CREATION_ERROR -> {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_ERROR_ON_CREATE_TICKET_ACTION_TTL,
                                Act092Translate.ALERT_ERROR_ON_CREATE_TICKET_ACTION_MSG
                            )
                        )
                    )
                }
                MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN -> {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_SCHEDULE_STATUS_PREVENTS_TO_OPEN_TTL,
                                Act092Translate.ALERT_SCHEDULE_STATUS_PREVENTS_TO_OPEN_MSG
                            )
                        )
                    )
                }
                PROFILE_PRJ001_AP_NOT_FOUND -> {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_MENU_APP_PROFILE_NOT_FOUND_TTL,
                                Act092Translate.ALERT_FORM_AP_MENU_PROFILE_NOT_FOUND_MSG
                            )
                        )
                    )
                }
                PROFILE_MENU_TICKET_NOT_FOUND -> {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_MENU_APP_PROFILE_NOT_FOUND_TTL,
                                Act092Translate.ALERT_TICKET_MENU_PROFILE_NOT_FOUND_MSG
                            )
                        )
                    )
                }
                SERIAL_SITE_OUT_OF_LICENSE -> {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_TTL,
                                Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_MSG
                            )
                        )
                    )
                }

                else -> {
                    view.onState(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                type,
                                "Sem dialog de variavel"
                            )
                        )
                    )
                }
            }
        }
    }


    private fun noRestriction(item: MyActions) {
        if (!ToolBox_Inf.profileExists(
                translateResource.context,
                Constant.PROFILE_PRJ001_SO,
                null
            )
            && !ToolBox_Inf.profileExists(
                translateResource.context,
                Constant.PROFILE_PRJ001_OI,
                null
            )
        ) {
            ToolBox_Con.setPreference_Site_Code(
                translateResource.context,
                item.siteCode.toString()
            )
            ToolBox_Con.setPreference_Zone_Code(translateResource.context, -1)
            //
            view.onState(Act092UiEvent.UpdateFooterInfos)
            //
            checkScheduleFlow(item)
        } else {
            ToolBox_Con.setPreference_Site_Code(
                translateResource.context,
                item.siteCode.toString()
            )
            ToolBox_Con.setPreference_Zone_Code(translateResource.context, -1)
            view.onState(
                Act092UiEvent.CallActForResult(
                    Act033_Main::class.java,
                    Bundle().apply {
                        putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT017)
                    },
                    view.CHANGE_ZONE_RESULT_CODE
                )
            )
        }
    }

    private fun checkTicketFlow(
        action: MyActions
    ) {
        actionUseCases.checkTicketFlowAndCreate(action)?.let {
            view.onState(
                Act092UiEvent.CallAct(
                    Act071_Main::class.java,
                    getTicketActionFlowBundle(
                        action,
                        it.scheduleExec,
                        it.ticket_prefix,
                        it.ticket_code,
                        it.ticket_seq
                    )
                )
            )
        }
    }


    private fun getScheduleBundle(
        scheduleExec: MD_Schedule_Exec,
        serial: MD_Product_Serial
    ): Bundle {
        return Bundle().also { bundle ->
            bundle.putString(MD_ProductDao.PRODUCT_CODE, scheduleExec.product_code.toString())
            bundle.putString(MD_ProductDao.PRODUCT_DESC, scheduleExec.product_desc.toString())
            bundle.putString(MD_ProductDao.PRODUCT_ID, scheduleExec.product_id.toString())
            bundle.putString(MD_Product_SerialDao.SERIAL_ID, serial.serial_id)
            bundle.putString(
                GE_Custom_FormDao.CUSTOM_FORM_CODE,
                scheduleExec.custom_form_code.toString()
            )
            bundle.putString(
                Constant.ACT010_CUSTOM_FORM_CODE_DESC,
                scheduleExec.custom_form_desc
            )
            bundle.putString(
                GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE,
                scheduleExec.custom_form_type.toString()
            )
            bundle.putString(
                GE_Custom_FormDao.CUSTOM_FORM_VERSION,
                scheduleExec.custom_form_version.toString()
            )
            view.bundle.putString(
                ConstantBaseApp.MAIN_REQUESTING_ACT,
                Constant.ACT092
            )
            view.bundle.putString(
                ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,
                Constant.ACT092
            )
            bundle.putAll(view.bundle)
        }
    }


    private fun processActSchedule(
        action: MyActions,
        actType: String,
        scheduleExec: MD_Schedule_Exec,
        serial: MD_Product_Serial
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            when (actType) {
                Constant.ACT011 -> {
                    view.onState(
                        Act092UiEvent.CallAct(
                            Act011_Main::class.java,
                            getScheduleBundle(scheduleExec, serial)
                        )
                    )
                }

                Constant.ACT087 -> {
                    view.onState(
                        Act092UiEvent.CallAct(
                            Act087Main::class.java,
                            Bundle().apply {
                                putString(MD_Product_SerialDao.SERIAL_ID, serial.serial_id)
                                view.bundle.putString(
                                    ConstantBaseApp.MAIN_REQUESTING_ACT,
                                    Constant.ACT092
                                )
                                view.bundle.putString(
                                    ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,
                                    Constant.ACT092
                                )
                                putAll(view.bundle)
                                putAll(
                                    Act087Main.getBundleInstance(
                                        scheduleExec.custom_form_type.toString(),
                                        scheduleExec.custom_form_code.toString(),
                                        scheduleExec.custom_form_version.toString(),
                                        serial.product_code.toString(),
                                        serial.serial_id.toString(),
                                        serial.serial_code.toString(),
                                        scheduleExec.schedule_prefix.toString(),
                                        scheduleExec.schedule_code.toString(),
                                        scheduleExec.schedule_exec.toString()
                                    )
                                )
                            }
                        )
                    )
                }
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
        bundle.putString(
            ConstantBaseApp.MAIN_REQUESTING_ACT,
            Constant.ACT092
        )
        bundle.putString(
            ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,
            Constant.ACT092
        )
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
                        Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_TTL,
                        Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_MSG
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
                                Act092Translate.PROGRESS_SYNC_TTL,
                                Act092Translate.PROGRESS_SYNC_MSG
                            )
                        )
                    )
                    newActionClick = true
                    view.wsProcess.value = WS_Sync::class.java.name
                    actionUseCases.syncFilesForm(
                        hmAux_Trans,
                        _serialModel.value.productCode?.toLong() ?: -1L
                    )
                }
            }
        }

    }

    private suspend fun validateCreateNewForm() {
        actionUseCases.validateNewForm(ValidateNewFormParam(_serialModel.value, hmAux_Trans))
            .collect {
                it.isSuccess { bundle ->
                    myActionFilterParam.paramTextFilter = view.filterText.value
                    myActionFilterParam.mainUserFilterState = view.focusState.value.mainUser
                    myActionFilterParam.paramItemSelectedTab = view.focusState.value.userFocusInt
                    myActionFilterParam.paramItemSelectedPk = null
                    myActionFilterParam.paramItemSelectedType = null

                    view.onState(
                        Act092UiEvent.CallAct(
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
                                        title = Act092Translate.ALERT_NO_FORM_TTL,
                                        message = exception.message
                                    )
                                )
                            )
                        } else {
                            view.onState(
                                Act092UiEvent.OpenDialog(
                                    DialogType.DEFAULT_OK(
                                        title = Act092Translate.ALERT_PRODUCT_OR_SERIAL_NOT_FOUND_TTL,
                                        message = Act092Translate.ALERT_PRODUCT_OR_SERIAL_NOT_FOUND_MSG
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

    override fun getUnfocusHistoricalList(context: Context) {
        if(ToolBox_Con.isOnline(context)) {
            view.wsProcess.value = WS_UnfocusAndHistoric::class.java.simpleName
            //
            view.onState(
                Act092UiEvent.OpenDialog(
                    DialogType.PROCESS(
                        Act092Translate.ALERT_SEND_FINISH_TTL,
                        Act092Translate.ALERT_SEND_FINISH_MSG

                    )
                )
            )
            //
            actionUseCases.unfocusHistoricalAction(
                _serialModel.value.productCode ?: -1,
                _serialModel.value.serialCode ?: 0L
            )
        }else{
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }


    override fun getActionSelected(): MyActions? {
        return actionSelected
    }


    override fun extractSearchResult(result: String?, myActionSelected: MyActions?) {
        CoroutineScope(Dispatchers.IO).launch {
            GsonBuilder().serializeNulls().create()?.fromJson<TSerial_Search_Rec>(
                result,
                TSerial_Search_Rec::class.java
            )?.let { rec ->
                actionSelected?.let {
                    actionUseCases.processLocalSearchForSerialAction(
                        ProcessLocalSearchForSerialParam(it, view.bundle, rec.record[0])
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
                                        "alert_no_serial_found_ttl",
                                        "alert_no_serial_found_msg"
                                    )
                                )
                            )
                        }
                    }
                }
            }

        }
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
            Act092Translate.ALERT_FORM_SITE_RESTRICTION_TTL,
            Act092Translate.ALERT_FORM_SITE_RESTRICTION_CONFIRM,
            Act092Translate.ALERT_FORM_SITE_RESTRICTION_NO_ACCESS_MSG,
            Act092Translate.ALERT_TTL_EXISTS_IN_PROCESSING,
            Act092Translate.ALERT_MSG_EXISTS_IN_PROCESSING,
            Act092Translate.ALERT_TTL_START_NEW_PROCESSING,
            Act092Translate.ALERT_MSG_START_NEW_PROCESSING,
            Act092Translate.ALERT_ERROR_ON_CREATE_FORM_TTL,
            Act092Translate.ALERT_ERROR_ON_CREATE_FORM_MSG,
            Act092Translate.ALERT_NO_SERIAL_FOUND_TTL,
            Act092Translate.ALERT_NO_SERIAL_FOUND_MSG,
            Act092Translate.ALERT_PRODUCT_NO_ALLOW_NEW_SERIAL_MSG,
            Act092Translate.ALERT_TICKET_ACTION_START_TTL,
            Act092Translate.ALERT_TICKET_ACTION_START_CONFIRM,
            Act092Translate.ALERT_ERROR_ON_CREATE_TICKET_ACTION_TTL,
            Act092Translate.ALERT_ERROR_ON_CREATE_TICKET_ACTION_MSG,
            Act092Translate.ALERT_SCHEDULE_STATUS_PREVENTS_TO_OPEN_TTL,
            Act092Translate.ALERT_SCHEDULE_STATUS_PREVENTS_TO_OPEN_MSG,
            Act092Translate.ALERT_MENU_APP_PROFILE_NOT_FOUND_TTL,
            Act092Translate.ALERT_FORM_AP_MENU_PROFILE_NOT_FOUND_MSG,
            Act092Translate.ALERT_TICKET_MENU_PROFILE_NOT_FOUND_MSG,
            Act092Translate.DIALOG_OTHER_ACTIONS_EMPTY_LIST_MSG,
            "progress_ticket_save_ttl",
            "progress_ticket_save_msg",
            "progress_form_save_ttl",
            "progress_form_save_msg",
            "cell_step_lbl",
            "msg_preparing_to_send_data",
            "cell_step_lbl",
            "cell_open_action_lbl",
            "cell_download_action_lbl",
            "alert_starting_pdf_not_supported_ttl",
            "alert_starting_pdf_not_supported_msg",
            "alert_form_pdf_download_error_ttl",
            "alert_form_pdf_download_error_msg",
            "done_action_list_limiter_lbl",
            "dialog_generate_form_pdf_ttl",
            "dialog_generate_form_pdf_start",
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

    fun executeNFormPDFGeneration(context: Context, myAction: MyActions, position: Int) {

        if (ToolBox_Con.isOnline(context)) {
            val pdfFile = File(ConstantBaseApp.CACHE_PATH + "/" + myAction.pdfName)
            if(pdfFile.exists() && pdfFile.isFile){
                openPDF(context, myAction.pdfName!!)
            }else {
                launch?.let {
                    if (it.isActive) {
                        it.cancel()
                    }
                }
//
                view.wsProcess.value = WS_Generate_NForm_PDF::class.java.name
                //
                view.showPD(
                    hmAux_Trans["dialog_generate_form_pdf_ttl"],
                    hmAux_Trans["dialog_generate_form_pdf_start"]
                )
                //
                launch = CoroutineScope(Dispatchers.IO).launch {

                    try {
                        if (!ToolBox_Inf.verifyDownloadFileInf(
                                Constant.CACHE_PATH + "/" +
                                        myAction.pdfName
                            )
                        ) {
                            val temp = myAction.pdfName!!.split(".")
                            ToolBox_Inf.deleteDownloadFileInf(
                                (Constant.CACHE_PATH + "/" +
                                        temp[0] + ".tmp")
                            )
                            //
                            ToolBox_Inf.downloadImagePDF(
                                myAction.pdfUrl,
                                (Constant.CACHE_PATH + "/" +
                                        temp[0] + ".tmp")
                            )
                            //
                            ToolBox_Inf.renameDownloadFileInf(temp[0], ".pdf")
                        }
                        //

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                    withContext(Dispatchers.Main) {
                        //Remove progress da act
                        view.disablePD()
                        //
                        if (myAction.pdfName != null) {
                            val pdfFile = File(ConstantBaseApp.CACHE_PATH + "/" + myAction.pdfName)
                            if(pdfFile.exists() && pdfFile.isFile){
                                myAction.processMidIcon =  R.drawable.ic_baseline_cloud_done_24_blue
                                view.setItemAsDownloaded(position, myAction)
                            }
                            //
                            openPDF(context, myAction.pdfName)
                        } else {
                            ToolBox.alertMSG(
                                context,
                                hmAux_Trans["alert_form_pdf_download_error_ttl"],
                                hmAux_Trans["alert_form_pdf_download_error_msg"],
                                null,
                                0
                            )
                        }
                    }
                }
            }
        } else {
            myAction.pdfName?.let {
                val pdfFile = File(ConstantBaseApp.CACHE_PATH + "/" + myAction.pdfName)
                if(pdfFile.exists() && pdfFile.isFile){
                    openPDF(context, myAction.pdfName)
                }else {
                    ToolBox_Inf.showNoConnectionDialog(context)
                }
            }?: ToolBox_Inf.showNoConnectionDialog(context)
        }
    }

    private fun openPDF(context: Context, pdfName: String) {
        val pdfFile = File(ConstantBaseApp.CACHE_PATH + "/" + pdfName)
        try {
            ToolBox_Inf.deleteAllFOD(Constant.CACHE_PDF)
            ToolBox_Inf.copyFile(
                pdfFile,
                File(Constant.CACHE_PDF)
            )
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        }

        val pdfIntent =
            ToolBox_Inf.getOpenPdfIntent(context, ConstantBaseApp.CACHE_PDF + "/" + pdfName)
        //
        try {
            context.startActivity(pdfIntent)
        } catch (e: ActivityNotFoundException) {
            ToolBox_Inf.registerException(javaClass.name, e)
            //
            ToolBox.alertMSG(
                context,
                hmAux_Trans["alert_starting_pdf_not_supported_ttl"],
                hmAux_Trans["alert_starting_pdf_not_supported_msg"],
                null,
                0
            )
        }
    }


    companion object {
        const val MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR =
            "MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR"
    }
}