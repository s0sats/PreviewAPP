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
import com.namoadigital.prj001.dao.GE_Custom_FormDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao
import com.namoadigital.prj001.dao.MD_ProductDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.dao.MdJustifyItemDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao
import com.namoadigital.prj001.extensions.isCurrentTrip
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.TSerial_Search_Rec
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.receiver.WBR_Generate_NForm_PDF
import com.namoadigital.prj001.receiver.WBR_Product_Serial_Structure
import com.namoadigital.prj001.receiver.WBR_Save
import com.namoadigital.prj001.receiver.WBR_Schedule_Not_Executed
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save
import com.namoadigital.prj001.service.WS_Generate_NForm_PDF
import com.namoadigital.prj001.service.WS_Product_Serial_Structure
import com.namoadigital.prj001.service.WS_Save
import com.namoadigital.prj001.service.WS_Sync
import com.namoadigital.prj001.service.WS_TK_Ticket_Download
import com.namoadigital.prj001.service.WS_TK_Ticket_Save
import com.namoadigital.prj001.service.WS_UnfocusAndHistoric
import com.namoadigital.prj001.service.WsScheduleNotExecuted
import com.namoadigital.prj001.sql.MDProductSerialSql018
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
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.MODULE_CHECKLIST_START_FORM
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.MODULE_SCHEDULE_TICKET_CREATION_ERROR
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.MODULE_TICKET_EXEC_CONFIRM
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.PROFILE_MENU_TICKET_NOT_FOUND
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.PROFILE_PRJ001_AP_NOT_FOUND
import com.namoadigital.prj001.ui.act083.Act083_Main.Companion.SERIAL_CREATION_DENIED
import com.namoadigital.prj001.ui.act087.Act087Main
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.act092.usecases.ActionUseCases
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.MODULE_CHECKLIST_FORM_IN_PROCESSING
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.SERIAL_SITE_OUT_OF_LICENSE
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.SERIAL_WITHOUT_STRUCTURE
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.SITE_RESTRICTION_CONFIRM
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.SITE_RESTRICTION_NO_ACCESS
import com.namoadigital.prj001.ui.act092.usecases.FlowTicketAccessUseCase.FlowTicketAccessError
import com.namoadigital.prj001.ui.act092.usecases.GetScheduleCtrlIfExistsUseCase
import com.namoadigital.prj001.ui.act092.usecases.ProcessLocalSearchForSerialActionUseCase.ProcessLocalSearchForSerialParam
import com.namoadigital.prj001.ui.act092.usecases.ScheduleFormException
import com.namoadigital.prj001.ui.act092.usecases.ValidateNewFormUseCase.ValidateNewFormParam
import com.namoadigital.prj001.ui.act092.utils.Act092Translate
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent.OpenDialog.DialogType
import com.namoadigital.prj001.ui.act093.ui.Act093_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.util.ValidateNewFormUseCaseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class Act092Presenter constructor(
    private var myActionFilterParam: MyActionFilterParam,
    private val originFlow: String?,
    private val iconColor: String,
    private val actionUseCases: ActionUseCases,
    private val translateResource: TranslateResource,
    private val showProductOnFilter: Boolean,
) : Act092_Contract.Presenter {

    var actionSelectedPosition: Int = -1
    private var actionSelected: MyActions? = null

    private lateinit var view: Act092_Contract.View

    private var launch: Job? = null

    private var _serialModel = MutableStateFlow(SerialModel())
    override val serialModel: StateFlow<SerialModel> = _serialModel

    var newActionClick = false

    init {
        if (originFlow == ConstantBaseApp.ACT006 || originFlow == ConstantBaseApp.ACT083) {
            cleanUnfocusAndHistoricalFile()
        }
    }

    private fun cleanUnfocusAndHistoricalFile() {
        ToolBox_Inf.deleteAllFOD(Constant.OTHER_ACTIONS_JSON_PATH)
    }

    private fun firstSave() {
        actionUseCases.setPreferences(
            actionUseCases.getPreferences().copy(
                originFlow = originFlow,
                siteCodeBack = ToolBox_Con.getPreference_Site_Code(translateResource.context),
                zoneCodeBack = ToolBox_Con.getPreference_Zone_Code(translateResource.context),
                classColor = iconColor,
                productCode = myActionFilterParam.productCode,
                productId = myActionFilterParam.productId,
                productDesc = myActionFilterParam.productDesc,
                serialId = myActionFilterParam.serialId,
                serialCode = myActionFilterParam.serialCode,
                ticketId = myActionFilterParam.ticketId,
                calendarDate = myActionFilterParam.calendarDate
            )
        )
    }

    private fun saveFilterLeftActivity() {
        val serialModel = _serialModel.value
        actionUseCases.setPreferences(
            actionUseCases.getPreferences().copy(
                mainUserFocus = view.focusState.value.mainUser,
                editFilter = view.filterText.value,
                otherSerialIsFiltered = !view.focusState.value.userFocus,
                lastSelectActionType = serialModel.lastSelectActionType,
                lastSelectedPk = serialModel.lastSelectedPk,
            )
        )
    }

    private fun loadFilter(context: Context) {
        if (context.isCurrentTrip()) {
            _serialModel.value = actionUseCases.getPreferences().copy(hmAux = hmAux_Trans)
            view.focusState.value.userFocus = !_serialModel.value.otherSerialIsFiltered
            view.focusState.value.mainUser = true
            view.filterText.value = _serialModel.value.editFilter ?: ""
        } else {
            _serialModel.value = actionUseCases.getPreferences().copy(hmAux = hmAux_Trans)
            view.focusState.value.userFocus = !_serialModel.value.otherSerialIsFiltered
            view.focusState.value.mainUser = _serialModel.value.mainUserFocus
            view.filterText.value = _serialModel.value.editFilter ?: ""
        }


        if (view.focusState.value.mainUser) {
            view.onEvent(Act092UiEvent.FilterMainUser)
        } else if (_serialModel.value.otherSerialIsFiltered) {
            view.onEvent(Act092UiEvent.UpdateOtherAction())
        }


        view.onEvent(
            Act092UiEvent.UpdateTitleActionSerial(
                _serialModel.value
            )
        )
    }


    override fun goToInfoSerial(bundle: Bundle) {
        view.onEvent(Act092UiEvent.CallAct(Act093_Main::class.java, bundle.apply {
            putSerializable(
                MyActionFilterParam.MY_ACTION_FILTER_PARAM,
                myActionFilterParam
            )
        }))
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

    override fun getMyActionList() {
        val userFocus = if (view.focusState.value.userFocus) 1 else 0
        CoroutineScope(Dispatchers.IO).launch {
            actionUseCases.listMyActionUseCases(
                Pair(
                    serialModel.value.copy(
                        userFocus = userFocus
                    ), view.focusState.value.mainUser || view.getContext().isCurrentTrip()
                )
            ).catch { e ->
                emit(loading(false))
                view.onEvent(Act092UiEvent.ShowSnackbar(e.message ?: "not found"))
            }.collect {

                it.isSuccess { list ->
                    view.onEvent(Act092UiEvent.ListingSerialSteels(list))
                }

                it.isFailed { throwable ->
                    view.onEvent(Act092UiEvent.ShowSnackbar(throwable.toString()))
                }

                it.isLoading { isLoading, message ->
                    view.onEvent(Act092UiEvent.IsLoading(isLoading, message))
                }

            }
        }
    }

    override fun onBackPressedClicked(bundle: Bundle) {
        when (_serialModel.value.originFlow) {
            ConstantBaseApp.ACT006 -> {

                val product =
                    if (showProductOnFilter) serialModel.value.productId else bundle.getString(
                        Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER
                    )

                view.onEvent(Act092UiEvent.CallAct(Act006_Main::class.java, Bundle().apply {
                    putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, product)
                    putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, "")
                }
                )
                )
            }

            ConstantBaseApp.ACT016 -> {
                view.onEvent(Act092UiEvent.CallAct(Act016_Main::class.java, bundle.apply {
                    putString(ConstantBaseApp.ACT_SELECTED_DATE, _serialModel.value.calendarDate)
                }))
            }

            ConstantBaseApp.ACT068 -> view.onEvent(Act092UiEvent.CallAct(Act068_Main::class.java))
            ConstantBaseApp.ACT083 -> view.onEvent(
                Act092UiEvent.CallAct(
                    Act083_Main::class.java,
                    bundle.apply {
                        myActionFilterParam.productCode = null
                        myActionFilterParam.productId = null
                        myActionFilterParam.productDesc = null
                        myActionFilterParam.serialId = null
                        myActionFilterParam.originFlow =
                            _serialModel.value.originFlow ?: ConstantBaseApp.ACT005

                        putSerializable(
                            MyActionFilterParam.MY_ACTION_FILTER_PARAM,
                            myActionFilterParam
                        )
                    })
            )

            else -> view.onEvent(Act092UiEvent.CallAct(Act005_Main::class.java))
        }
    }


    override fun syncFiles(context: Context) {
        if (ToolBox_Con.isOnline(context)) {
            view.wsProcess.value = Act005_Main.WS_PROCESS_SYNC
            view.onEvent(
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

    override fun processActionClick(action: MyActions, context: Context, position: Int) {
        actionSelected = action
        when (action.actionType) {
            MyActions.MY_ACTION_TYPE_TICKET -> {
                if (action.siteCode == null) {
                    showMsg(SITE_NOT_FOUND, action)
                    return
                }

                performClickFlowTicket()
            }

            MyActions.MY_ACTION_TYPE_TICKET_CACHE -> {
                if (!ToolBox_Con.isOnline(context)) {
                    ToolBox_Inf.showNoConnectionDialog(context)
                    return
                }

                performClickFlowTicket(true)
            }


            MyActions.MY_ACTION_TYPE_SCHEDULE -> {
                if (!action.pdfName.isNullOrEmpty()) {
                    if (!action.pdfUrl.isNullOrEmpty()) {
                        executeNFormPDFDownload(context, action, position)
                    } else {
                        actionSelectedPosition = position
                        executeNFormPDFGeneration(context, action, position)
                    }
                } else {
                    checkScheduleFlow(action)
                }
            }

            MyActions.MY_ACTION_TYPE_FORM -> {
                if (!action.pdfName.isNullOrEmpty()) {
                    if (!action.pdfUrl.isNullOrEmpty()) {
                        executeNFormPDFDownload(context, action, position)
                    } else {
                        actionSelectedPosition = position
                        executeNFormPDFGeneration(context, action, position)
                    }
                } else {
                    setSeletedActionInfosIntoFilterParam(
                        action.actionType,
                        "",
                    )
                    view.onEvent(
                        Act092UiEvent.CallAct(
                            Act011_Main::class.java,
                            getFormBundle(action)
                        )
                    )
                }
            }
        }
    }

    private fun performClickFlowTicket(downloadTicket: Boolean = false) {
        actionSelected?.let { action ->
            CoroutineScope(Dispatchers.IO).launch {
                actionUseCases.flowTicketSiteAccess(action.siteCode.toString())
                    .collect {
                        it.watchStatus(
                            success = {
                                if (!downloadTicket) {
                                    goToAct070(action)
                                } else {
                                    flowDownloadTicket(action)
                                }
                            },
                            error = { message, _ ->
                                when (message!!) {
                                    FlowTicketAccessError.SITE_NOT_ACCESS -> {
                                        showMsg(SITE_RESTRICTION_NO_ACCESS, action, downloadTicket)
                                    }

                                    FlowTicketAccessError.SITE_ACCESS_CONFIRM -> {
                                        showMsg(message, action, downloadTicket)
                                    }
                                }
                            }
                        )
                    }
            }
        }
    }

    private fun flowDownloadTicket(action: MyActions) {
        view.wsProcess.value = WS_TK_Ticket_Download::class.java.name
        view.onEvent(
            Act092UiEvent.OpenDialog(
                DialogType.PROCESS(
                    Act092Translate.DIALOG_DOWNLOAD_TICKET_TTL,
                    Act092Translate.DIALOG_DOWNLOAD_TICKET_START
                )
            )
        )

        actionUseCases.downloadTicket(action.processPk.replace(".", "|"))
    }

    private fun goToAct070(action: MyActions) {
        val slippedPk = action.getSplippedPk()
        setSeletedActionInfosIntoFilterParam(
            action.actionType,
            action.processPk,
            action.isMainUserTicket
        )

        view.onEvent(
            Act092UiEvent.CallAct(
                Act070_Main::class.java,
                ticketBundle(slippedPk[0].toInt(), slippedPk[1].toInt())
            )
        )
    }

    private fun getFormBundle(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        val bundle = Bundle()
        val currentOrigin = view.bundle.getString(MY_ACTIONS_ORIGIN_FLOW, Constant.ACT092)
        //
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT092)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(MY_ACTIONS_ORIGIN_FLOW, currentOrigin)
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
                        if (transform.isProcess) {
                            processActSchedule(
                                action,
                                transform.actType,
                                transform.scheduleExec,
                                transform.productSerial
                            )
                            return@isSuccess
                        }
                        //
                        if (action.processStatus == ConstantBaseApp.SYS_STATUS_DONE) {
                            processActSchedule(
                                action,
                                transform.actType,
                                transform.scheduleExec,
                                transform.productSerial
                            )
                        } else {
                            view.onEvent(
                                Act092UiEvent.OpenDialog(
                                    DialogType.ACTION(
                                        title = Act092Translate.ALERT_TTL_START_NEW_PROCESSING,
                                        message = Act092Translate.ALERT_MSG_START_NEW_PROCESSING,
                                        negativeBtn = 1,
                                        action = { dialog, i ->
                                            processActSchedule(
                                                action,
                                                transform.actType,
                                                transform.scheduleExec,
                                                transform.productSerial
                                            )
                                        }
                                    )
                                )
                            )
                        }
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
        if (ToolBox_Con.isOnline(context)) {
            view.wsProcess.value = WS_Save::class.java.simpleName
            //

            view.onEvent(
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
        } else {
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }

    override fun callTicketSave(context: Context) {
        if (ToolBox_Con.isOnline(context)) {
            view.wsProcess.value = WS_TK_Ticket_Save::class.java.simpleName
            //
            view.onEvent(
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
        } else {
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }

    override fun otherActionFlow(context: Context) {
        val fileExists = actionUseCases.checkIfFileExists(
            _serialModel.value.productCode ?: -1,
            _serialModel.value.serialCode ?: -1L
        )

        if (fileExists) {
            getMyActionList()
        } else {
            getFileOnline(context)
        }

    }

    private fun getFileOnline(context: Context) {
        if (ToolBox_Con.isOnline(context)) {
            val hasFormPendency = getFormPendency(context)
            val hasSerialStructurePendency = getSerialStructurePendency(context)
            val hasTicketPendency = getTicketPendency(context)
            if (hasFormPendency) {
                callFormSave(context)
            } else if (hasSerialStructurePendency) {
                updateSerialStrucutreAfterWsSave(context)
            } else if (hasTicketPendency) {
                callTicketSave(context)
            } else {
                getUnfocusHistoricalList(context)
            }
        } else {
            ToolBox_Inf.showNoConnectionDialog(context)
            view.onEvent(Act092UiEvent.UpdateOtherAction(false))
        }
    }

    private fun getTicketPendency(context: Context): Boolean {
        return ToolBox_Inf.handleTicketUpdateRequired(
            context,
            ToolBox_Con.getPreference_Customer_Code(context)
        ).toInt() > 0
    }

    private fun getSerialStructurePendency(context: Context): Boolean {
        val mdProductSerialDao = MD_Product_SerialDao(context)
        val serial: List<MD_Product_Serial> = mdProductSerialDao.query(
            MDProductSerialSql018(
                ToolBox_Con.getPreference_Customer_Code(context)
            ).toSqlQuery()
        )
        //
        //
        return serial.size > 0
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

    private fun showMsg(type: String, item: MyActions, downloadTicket: Boolean = false) {
        CoroutineScope(Dispatchers.Main).launch {
            when (type) {

                SITE_NOT_FOUND -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.CUSTOM_OK(
                                title = Act092Translate.ALERT_SITE_NOT_FOUND_TTL,
                                message = Act092Translate.ALERT_SITE_NOT_FOUND_MSG,
                                action = { dialog, _ ->
                                    dialog.dismiss()
                                    getMyActionList()
                                }
                            )
                        )
                    )
                }


                ZONE_NOT_FOUND -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.CUSTOM_OK(
                                title = Act092Translate.ALERT_ZONE_NOT_FOUND_TTL,
                                message = Act092Translate.ALERT_ZONE_NOT_FOUND_MSG,
                                action = { dialog, _ ->
                                    dialog.dismiss()
                                    getMyActionList()
                                }
                            )
                        )
                    )
                }

                SITE_RESTRICTION_NO_ACCESS -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                title = Act092Translate.ALERT_FORM_SITE_RESTRICTION_TTL,
                                message = Act092Translate.ALERT_FORM_SITE_RESTRICTION_NO_ACCESS_MSG,
                            )
                        )
                    )
                }

                MODULE_CHECKLIST_START_FORM -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.ACTION(
                                title = Act092Translate.ALERT_TTL_START_NEW_PROCESSING,
                                message = Act092Translate.ALERT_MSG_START_NEW_PROCESSING,
                                action = { dialog, i ->
                                    actionSelected?.let {
                                        checkScheduleFlow(it)
                                    }
                                }
                            )
                        )
                    )
                }

                SITE_RESTRICTION_CONFIRM -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.ACTION(
                                title = Act092Translate.ALERT_FORM_SITE_RESTRICTION_TTL,
                                message = Act092Translate.ALERT_FORM_SITE_RESTRICTION_CONFIRM,
                                { dialog, _ ->
                                    dialog.dismiss()
                                    noRestriction(item)
                                }, negativeBtn = 1
                            )
                        )
                    )
                }

                FlowTicketAccessError.SITE_ACCESS_CONFIRM -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.ACTION(
                                title = Act092Translate.ALERT_FORM_SITE_RESTRICTION_TTL,
                                message = Act092Translate.ALERT_FORM_SITE_RESTRICTION_CONFIRM,
                                action = { dialog, _ ->
                                    dialog.dismiss()
                                    flowTicketSiteRestriction(item, downloadTicket)
                                },
                                negativeBtn = 1
                            )
                        )
                    )
                }

                MODULE_CHECKLIST_FORM_IN_PROCESSING -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_TTL_EXISTS_IN_PROCESSING,
                                Act092Translate.ALERT_MSG_EXISTS_IN_PROCESSING
                            )
                        )
                    )
                }

                MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_ERROR_ON_CREATE_FORM_TTL,
                                Act092Translate.ALERT_ERROR_ON_CREATE_FORM_MSG
                            )
                        )
                    )
                }

                EMPTY_SERIAL_SEARCH -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_NO_SERIAL_FOUND_TTL,
                                Act092Translate.ALERT_NO_SERIAL_FOUND_MSG
                            )
                        )
                    )
                }

                SERIAL_CREATION_DENIED -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_PRODUCT_OR_SERIAL_NOT_FOUND_TTL,
                                Act092Translate.ALERT_PRODUCT_NO_ALLOW_NEW_SERIAL_MSG
                            )
                        )
                    )
                }

                MODULE_TICKET_EXEC_CONFIRM -> {
                    view.onEvent(Act092UiEvent.OpenDialog(
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
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_ERROR_ON_CREATE_TICKET_ACTION_TTL,
                                Act092Translate.ALERT_ERROR_ON_CREATE_TICKET_ACTION_MSG
                            )
                        )
                    )
                }

                MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_SCHEDULE_STATUS_PREVENTS_TO_OPEN_TTL,
                                Act092Translate.ALERT_SCHEDULE_STATUS_PREVENTS_TO_OPEN_MSG
                            )
                        )
                    )
                }

                PROFILE_PRJ001_AP_NOT_FOUND -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_MENU_APP_PROFILE_NOT_FOUND_TTL,
                                Act092Translate.ALERT_FORM_AP_MENU_PROFILE_NOT_FOUND_MSG
                            )
                        )
                    )
                }

                PROFILE_MENU_TICKET_NOT_FOUND -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_MENU_APP_PROFILE_NOT_FOUND_TTL,
                                Act092Translate.ALERT_TICKET_MENU_PROFILE_NOT_FOUND_MSG
                            )
                        )
                    )
                }

                SERIAL_SITE_OUT_OF_LICENSE -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_TTL,
                                Act092Translate.ALERT_FREE_EXECUTION_BLOCKED_MSG
                            )
                        )
                    )
                }

                SERIAL_WITHOUT_STRUCTURE -> {
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.DEFAULT_OK(
                                Act092Translate.ALERT_SERIAL_WITHOUT_STRUCTURE_TTL,
                                Act092Translate.ALERT_SERIAL_WITHOUT_STRUCTURE_MSG
                            )
                        )
                    )
                }

                else -> {
                    view.onEvent(
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

    private fun flowTicketSiteRestriction(item: MyActions, downloadTicket: Boolean = false) {
        val context = translateResource.context

        val itemZoneCode = MD_Product_SerialDao(context).let { dao ->
            val productSerial: MD_Product_Serial? = ToolBox_Inf.getProductSerial(
                context,
                dao,
                item.productCode!!.toLong(),
                item.serialId!!
            )

            productSerial?.zone_code
        }

        ToolBox_Con.setPreference_Site_Code(
            context,
            item.siteCode.toString()
        )

        ToolBox_Con.setPreference_Zone_Code(
            context,
            itemZoneCode ?: -1
        )

        if (!downloadTicket) {
            goToAct070(item)
        } else {
            flowDownloadTicket(item)
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
            view.onEvent(Act092UiEvent.UpdateFooterInfos)
            //
            checkScheduleFlow(item)
        } else {
            ToolBox_Con.setPreference_Site_Code(
                translateResource.context,
                item.siteCode.toString()
            )
            ToolBox_Con.setPreference_Zone_Code(translateResource.context, -1)
            view.onEvent(
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
            view.onEvent(
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
        serial: MD_Product_Serial,
        action: MyActions
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
                MY_ACTIONS_ORIGIN_FLOW,
                Constant.ACT092
            )
            bundle.putString(
                GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,
                action.scheduleCustomFormData
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
                    if (actionUseCases.createFormLocalForSchedule(
                            actionUseCases.scheduleFormLocalExists(
                                scheduleExec,
                                action
                            ).first, scheduleExec, action
                        )
                    ) {
                        setSeletedActionInfosIntoFilterParam(
                            action.actionType, action.processPk,
                        )

                        view.onEvent(
                            Act092UiEvent.CallAct(
                                Act011_Main::class.java,
                                getScheduleBundle(scheduleExec, serial, action)
                            )
                        )
                    } else {
                        view.onEvent(
                            Act092UiEvent.OpenDialog(
                                DialogType.DEFAULT_OK(
                                    hmAux_Trans[""],
                                    hmAux_Trans[""]
                                )
                            )
                        )
                    }

                }

                Constant.ACT087 -> {
                    view.onEvent(
                        Act092UiEvent.CallAct(
                            Act087Main::class.java,
                            Bundle().apply {
                                setSeletedActionInfosIntoFilterParam(
                                    action.actionType, action.processPk,
                                )
                                putString(MD_Product_SerialDao.SERIAL_ID, serial.serial_id)
                                view.bundle.putString(
                                    ConstantBaseApp.MAIN_REQUESTING_ACT,
                                    Constant.ACT092
                                )
                                /*
                                    Mantem act006 como origem para navegacao de forms avulsos.
                                 */
                                val originFlow = view.bundle.getString(MY_ACTIONS_ORIGIN_FLOW)
                                if (originFlow == null || (originFlow == ConstantBaseApp.ACT006 && action.actionType == "SCHEDULE")) {
                                    view.bundle.putString(MY_ACTIONS_ORIGIN_FLOW, Constant.ACT092)
                                }

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
            MY_ACTIONS_ORIGIN_FLOW,
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
                view.onEvent(
                    Act092UiEvent.CallAct(
                        Act070_Main::class.java,
                        getCacheTicketBundle(hmAuxTicketDownload)
                    )
                )
            }
        }
    }

    override fun saveFilterWhenLeftActivity() {
        saveFilterLeftActivity()
    }

    override fun processNewFormClick(context: Context) {
        _serialModel.value = _serialModel.value.copy(
            lastSelectedPk = null,
            lastSelectActionType = null
        )
        if (ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context)) {
            view.onEvent(
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
                    view.onEvent(
                        Act092UiEvent.OpenDialog(
                            DialogType.PROCESS(
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
                } else {
                    validateCreateNewForm()
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
                    myActionFilterParam.paramItemSelectedTab =
                        if (view.focusState.value.userFocus) 1 else 0
                    myActionFilterParam.paramItemSelectedPk = null
                    myActionFilterParam.paramItemSelectedType = null

                    if (ConstantBaseApp.ACT006 == originFlow) {
                        myActionFilterParam.originFlow = originFlow
                        bundle.putString(
                            MY_ACTIONS_ORIGIN_FLOW,
                            originFlow
                        )
                    }
                    view.onEvent(
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
                                view.onEvent(
                                    Act092UiEvent.OpenDialog(
                                        DialogType.CUSTOM_OK(
                                            title = Act092Translate.ALERT_NO_FORM_TTL,
                                            message = exception.message
                                        )
                                    )
                                )
                            } else {
                                view.onEvent(
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

        myActionFilterParam.setSelectedItemParams(
            view.filterText.value,
            if (view.focusState.value.userFocus) 1 else 0,
            myActionType,
            myActionPk,
            null,
            mainFocus
        )

        _serialModel.value = serialModel.value.copy(
            mainUserFocus = mainFocus,
            lastSelectActionType = myActionType,
            lastSelectedPk = myActionPk,
        )
        saveFilterLeftActivity()
    }

    private fun ticketBundle(ticketPrefix: Int, ticketCode: Int): Bundle {
        val bundle = Bundle()
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticketPrefix)
        bundle.putInt(TK_TicketDao.TICKET_CODE, ticketCode)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT092)
        return bundle
    }

    override fun getUnfocusHistoricalList(context: Context) {
        if (ToolBox_Con.isOnline(context)) {
            view.wsProcess.value = WS_UnfocusAndHistoric::class.java.simpleName
            //
            view.onEvent(
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
        } else {
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
                            view.onEvent(
                                Act092UiEvent.CallAct(
                                    Act020_Main::class.java,
                                    bund
                                )
                            )
                        }

                        result.isFailed { exception ->
                            view.onEvent(
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

    override fun executeNFormPDFGeneration(context: Context, action: MyActions, position: Int) {

        if (ToolBox_Con.isOnline(context)) {
            view.wsProcess.value = WS_Generate_NForm_PDF::class.java.name
            //
            view.showPD(
                hmAux_Trans["dialog_generate_form_pdf_ttl"],
                hmAux_Trans["dialog_generate_form_pdf_start"]
            )
            //
            val mIntent = Intent(context, WBR_Generate_NForm_PDF::class.java)
            val bundle = Bundle()
            //
            val nformPkFormatted = """${ToolBox_Con.getPreference_Customer_Code(context)}|${
                action.processPk.replace(
                    ".",
                    "|"
                )
            }"""
            bundle.putString(WS_Generate_NForm_PDF.NFORM_PK_KEY, nformPkFormatted)
            bundle.putString(WS_Generate_NForm_PDF.TYPE_KEY, action.actionType)
            //
            mIntent.putExtras(bundle)
            //
            context.sendBroadcast(mIntent)
        } else {
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }

    override fun setView(view: Act092_Contract.View) {
        this.view = view
        if (originFlow == ConstantBaseApp.ACT006 || originFlow == ConstantBaseApp.ACT083) {
            firstSave()
        }

        loadFilter(view.getContext())
        getMyActionList()
    }

    override fun loadTranslation(): HMAux {
        mutableListOf(
            "act092_title",
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
            Act092Translate.CELL_WAITING_APPROVAL,
            Act092Translate.ALERT_SERIAL_WITHOUT_STRUCTURE_TTL,
            Act092Translate.ALERT_SERIAL_WITHOUT_STRUCTURE_MSG,
            "progress_ticket_save_ttl",
            "progress_ticket_save_msg",
            "progress_form_save_ttl",
            "progress_form_save_msg",
            "progress_serial_structure_ttl",
            "progress_serial_structure_msg",
            "msg_preparing_to_send_data",
            "cell_step_lbl",
            "cell_open_action_lbl",
            "cell_continue_action_lbl",
            "cell_download_action_lbl",
            "alert_starting_pdf_not_supported_ttl",
            "alert_starting_pdf_not_supported_msg",
            "alert_form_pdf_download_error_ttl",
            "alert_form_pdf_download_error_msg",
            "done_action_list_limiter_lbl",
            "dialog_generate_form_pdf_ttl",
            "dialog_generate_form_pdf_start",
            "cell_justify_lbl",
            "cell_item_in_process_lbl",
            "other_steps_available_lbl",
            "cell_download_action_pdf_lbl",
            "btn_other_actions",
            "btn_new_action",
            Act092Translate.HINT_FILTER,
            Act092Translate.PLACEHOLDER_FILTER,
            Act092Translate.OTHER_ACTIONS,
            Act092Translate.NEW_ACTION,
            "alert_not_execute_ttl",
            "alert_not_execute_msg",
            "alert_not_execute_justify_date_ttl",
            "alert_not_execute_justify_option_lbl",
            "alert_not_execute_justify_comment_lbl",
            "sys_alert_btn_cancel",
            "alert_not_execute_save_btn",
            "alert_not_execute_justify_required_ttl",
            "alert_not_execute_justify_option_required_msg",
            "alert_not_execute_justify_comment_required_msg",
            "alert_not_execute_justify_success_ttl",
            "alert_not_execute_justify_success_msg",
            "btn_cancel_schedule",
            "warning_not_execute_justify_required_date_hour",
            "warning_not_execute_justify_future_date_hour",
            "alert_not_execute_justify_lost_data_ttl",
            "alert_not_execute_justify_lost_data_msg",
            "progress_n_form_sync_ttl",
            "alert_serial_structure_error_ttl",
            "alert_serial_structure_error_msg",
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

    fun executeNFormPDFDownload(context: Context, myAction: MyActions, position: Int) {
        actionSelectedPosition = -1
        if (ToolBox_Con.isOnline(context)) {
            val pdfFile = File(ConstantBaseApp.CACHE_PATH + "/" + myAction.pdfName)
            if (pdfFile.exists() && pdfFile.isFile) {
                openPDF(context, myAction.pdfName!!)
            } else {
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
                            if (pdfFile.exists() && pdfFile.isFile) {
                                myAction.processMidIcon = R.drawable.ic_baseline_cloud_done_24_blue
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
                if (pdfFile.exists() && pdfFile.isFile) {
                    openPDF(context, myAction.pdfName)
                } else {
                    ToolBox_Inf.showNoConnectionDialog(context)
                }
            } ?: ToolBox_Inf.showNoConnectionDialog(context)
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


    override fun justifyNotExecuteSchedule(
        processPk: String,
        comments: String,
        justify_group_code: Int,
        justify_item_code: Int,
        reschedule_date: String,
        context: Context,
    ) {
        if (ToolBox_Con.isOnline(context)) {
            view.wsProcess.value = WsScheduleNotExecuted::class.java.name

            view.showPD(
                hmAux_Trans["progress_n_form_sync_ttl"],
                hmAux_Trans["progress_n_form_sync_msg"],
            )

            Intent(context, WBR_Schedule_Not_Executed::class.java).also { intent ->

                Bundle().apply {
                    val schedule = processPk.split(".")
                    putInt(WsScheduleNotExecuted.WS_BUNDLE_SCHEDULE_PREFIX, schedule[0].toInt())
                    putInt(WsScheduleNotExecuted.WS_BUNDLE_SCHEDULE_CODE, schedule[1].toInt())
                    putInt(WsScheduleNotExecuted.WS_BUNDLE_SCHEDULE_EXEC, schedule[2].toInt())
                    putString(WsScheduleNotExecuted.WS_BUNDLE_COMMENTS, comments)
                    putInt(WsScheduleNotExecuted.WS_BUNDLE_JUSTIFY_GROUP_CODE, justify_group_code)
                    putInt(WsScheduleNotExecuted.WS_BUNDLE_JUSTIFY_ITEM_CODE, justify_item_code)
                    putString(WsScheduleNotExecuted.WS_BUNDLE_RESCHEDULE_DATE, reschedule_date)
                }.let { bundle ->
                    intent.putExtras(bundle)
                    context.sendBroadcast(intent)
                }

            }
        } else {
            ToolBox_Inf.showNoConnectionDialog(context)
        }

    }

    override fun getJustifyItems(justifyGroupCode: Int, context: Context): ArrayList<HMAux> {
        MdJustifyItemDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        ).let { mdJustifyItemDao ->

            return mdJustifyItemDao.getJustifyItems(
                ToolBox_Con.getPreference_Customer_Code(context),
                justifyGroupCode
            )

        }

    }

    override fun hasSerialStructureOutdate(context: Context): Boolean {
        val serialDao = MD_Product_SerialDao(
            context
        )
        val serial: List<MD_Product_Serial> = serialDao.query(
            MDProductSerialSql018(
                ToolBox_Con.getPreference_Customer_Code(context)
            ).toSqlQuery()
        )
        //
        return serial.size > 0
    }

    override fun updateSerialStrucutreAfterWsSave(context: Context) {
        if (ToolBox_Con.isOnline(context)) {
            //
            view.wsProcess.value = WS_Product_Serial_Structure::class.java.simpleName
            //
            view.showPD(
                hmAux_Trans["progress_serial_structure_ttl"],
                hmAux_Trans["progress_serial_structure_msg"]
            )
            //
            val mIntent = Intent(context, WBR_Product_Serial_Structure::class.java)
            val bundle = Bundle()
            bundle.putLong(MD_Product_SerialDao.CUSTOMER_CODE, -1)
            bundle.putLong(MD_Product_SerialDao.PRODUCT_CODE, -1)
            bundle.putLong(MD_Product_SerialDao.SERIAL_CODE, -1)
            bundle.putInt(MD_Product_SerialDao.SCN_ITEM_CHECK, 0)
            //
            mIntent.putExtras(bundle)
            //
            context.sendBroadcast(mIntent)
        } else {
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }


    companion object {
        const val MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR =
            "MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR"

        const val ZONE_NOT_FOUND = "ZONE_NOT_FOUND"
        const val SITE_NOT_FOUND = "SITE_NOT_FOUND"
    }
}