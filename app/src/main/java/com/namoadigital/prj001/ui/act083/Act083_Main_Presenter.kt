package com.namoadigital.prj001.ui.act083

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_Serial_Search
import com.namoadigital.prj001.receiver.WBR_Sync
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download
import com.namoadigital.prj001.service.WS_Serial_Search
import com.namoadigital.prj001.service.WS_TK_Ticket_Download
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.dialog.ScheduleRequestSerialDialog2
import kotlinx.coroutines.*
import java.util.*

class Act083_Main_Presenter(private val context: Context,
                            private val mView: Act083_Main_Contract.I_View,
                            private val bundle: Bundle,
                            private val ticketDao: TK_TicketDao,
                            private val ticketCacheDao: TkTicketCacheDao,
                            private val scheduleDao: MD_Schedule_ExecDao,
                            private val formApDao: GE_Custom_Form_ApDao,
                            private val formLocalDao: GE_Custom_Form_LocalDao,
                            private val siteDao: MD_SiteDao,
                            private val ticketCtrlDao: TK_Ticket_CtrlDao,
                            private val serialDao: MD_Product_SerialDao,
                            private val productDao: MD_ProductDao,
                            private val mModule_Code: String,
                            private val mResource_Code: String
) : Act083_Main_Contract.I_Presenter{


    private lateinit var myActionFilterParam : MyActionFilterParam
    private var tagFilter: Int? = null
    private var productCode: Int? = null
    private var serialId: String? = null
    private var siteCode: String? = null
    private var ticketId: String? = null
    private var clientId: String? = null
    private var contractId: String? = null
    private var calendarDate: String? = null
    private lateinit var originFlow: String
    private var _myActionsList = mutableListOf<MyActionsBase>()
    var myActionsList = mutableListOf<MyActionsBase>()
        get() {
            return _myActionsList
        }
    val hmAux_Trans: HMAux? by lazy {
        loadTranslation()
    }
    var siteCodeBack: String? = null
    var zoneCodeBack = 0
    var actionSelected : MyActions? = null
    private var serialDialog : ScheduleRequestSerialDialog2? = null
    private var launch : Job? = null
    private var initialTabToLoad : Int = 1
    private var initialTextFilter : String? = null
    private var _lastSelectedActionPk : String? = null
    private var _lastSelectedActionType : String? = null
    val lastSelectedActionPk :String?
        get() = _lastSelectedActionPk
    val lastSelectedActionType :String?
        get() = _lastSelectedActionType

    init {
        recoverIntentsInfo()
        loadFilters()
        setViewFiltersParam()
        generateMyActionList(initialTabToLoad)
    }

    private fun setViewFiltersParam() {
        mView.setViewFiltersParam(
                initialTextFilter,
                initialTabToLoad
        )
    }

    override fun loadTranslation(): HMAux? {
        val transList: MutableList<String> = java.util.ArrayList()
        transList.add("act083_title")
        transList.add("tab_my_actions_lbl")
        transList.add("tab_other_actions_lbl")
        transList.add("filter_hint")
        transList.add("form_lbl")
        transList.add("IN_PROCESSING")
        transList.add("no_record_lbl")
        transList.add("other_steps_available_lbl")
        transList.add("dialog_download_ticket_ttl")
        transList.add("dialog_download_ticket_start")
        transList.add("progress_sync_ttl")
        transList.add("progress_sync_msg")
        transList.add("site_desc_not_found_lbl")
        //
        transList.add("alert_ttl_exists_in_processing")
        transList.add("alert_msg_exists_in_processing")
        transList.add("alert_ttl_start_new_processing")
        transList.add("alert_msg_start_new_processing")
        transList.add("alert_error_on_create_form_ttl")
        transList.add("alert_error_on_create_form_msg")
        transList.add("alert_no_serial_found_ttl")
        transList.add("alert_no_serial_found_msg")
        transList.add("alert_product_no_allow_new_serial_msg")
        transList.add("alert_ticket_action_start_ttl")
        transList.add("alert_ticket_action_start_confirm")
        transList.add("alert_error_on_create_ticket_action_ttl")
        transList.add("alert_error_on_create_ticket_action_msg")
        transList.add("alert_schedule_status_prevents_to_open_ttl")
        transList.add("alert_schedule_status_prevents_to_open_msg")
        transList.add("alert_menu_app_profile_not_found_ttl")
        transList.add("alert_form_ap_menu_profile_not_found_msg")
        transList.add("alert_menu_app_profile_not_found_ttl")
        transList.add("alert_ticket_menu_profile_not_found_msg")
        transList.add("alert_free_execution_blocked_ttl")
        transList.add("alert_free_execution_blocked_msg")
        //
        transList.add("alert_form_site_restriction_ttl")
        transList.add("alert_form_site_restriction_confirm")
        transList.add("dialog_serial_search_ttl")
        transList.add("dialog_serial_search_start")
        //
        transList.add("sys_main_menu_assets_local_lbl")
        transList.add("sys_main_menu_calendar_lbl")
        transList.add("sys_main_menu_search_lbl")
        //
        transList.add("new_form_lbl")
        transList.add("alert_no_form_lbl");
        transList.add("alert_no_form_for_product_msg");
        transList.add("alert_no_form_for_operation_msg");
        transList.add("alert_no_form_for_site_msg");
        transList.add("alert_no_form_ttl");
        transList.add("alert_product_or_serial_not_found_ttl");
        transList.add("alert_product_or_serial_not_found_msg");
        //
        return ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        )
    }

    override fun getChipList(): List<String> {
        val chipList = mutableListOf<String>()
        chipList.addAll(
                myActionFilterParam.getFilledFilters(context)
        )
        if(!ToolBox_Inf.usesSoMainActivity(context)) {
            when (originFlow) {
                ConstantBaseApp.ACT005 -> setPreferenceChips(chipList)
            }
        }
        return chipList
    }

    private fun setPreferenceChips(chipList: MutableList<String>) {
        val timePrefence = ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_PERIOD_FILTER, ConstantBaseApp.PREFERENCE_HOME_ALL_TIME_OPTION)
        if (ConstantBaseApp.PREFERENCE_HOME_ALL_TIME_OPTION != timePrefence) {
            hmAux_Trans?.get(timePrefence)?.let { chipList.add(it) }
        }
        val sitePrefence = ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_SITES_FILTER, ConstantBaseApp.PREFERENCE_HOME_ALL_SITE_OPTION)
        if (ConstantBaseApp.PREFERENCE_HOME_ALL_SITE_OPTION != sitePrefence) {
            val siteObjInfo = ToolBox_Inf.getSiteObjInfo(context, ToolBox_Con.getPreference_Site_Code(context))
            chipList.add(siteObjInfo?.site_desc ?: hmAux_Trans?.get("site_desc_not_found_lbl")
            ?: "SITE_DESC_NOT_FOUND")
        }
    }

    override fun getActTitle(): String {
        return when(originFlow){
            ConstantBaseApp.ACT005 -> myActionFilterParam.tagFilterDesc
                    ?: hmAux_Trans!!["act083_title"]!!
            ConstantBaseApp.ACT006 -> hmAux_Trans!!["sys_main_menu_assets_local_lbl"]!!
            ConstantBaseApp.ACT016 -> hmAux_Trans!!["sys_main_menu_calendar_lbl"]!!
            ConstantBaseApp.ACT068 -> hmAux_Trans!!["sys_main_menu_search_lbl"]!!
            else -> hmAux_Trans!!["act083_title"]!!
        }
    }

    override fun updateMyActionList(userFocusFilter: Int) {
        generateMyActionList(userFocusFilter)
    }

    override fun processActionFormButtonClick(myActionsFormButton: MyActionsFormButton) {
        if(ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context)){
            mView.showAlertMsg(
                    hmAux_Trans!!["alert_free_execution_blocked_ttl"]!!,
                    hmAux_Trans!!["alert_free_execution_blocked_msg"]!!
            )
        }else{
           validadeCreateNewForm(myActionsFormButton)
        }
    }

    private fun validadeCreateNewForm(myActionsFormButton: MyActionsFormButton) {
        val mdProductSerial : MD_Product_Serial? = getSerial(myActionsFormButton.productCode, myActionsFormButton.serialId)
        val mdProduct : MD_Product? = getProductInfo(myActionsFormButton.productCode)
        //
        if(mdProductSerial != null && mdProduct != null){
            //
            val formXProductExist = ToolBox_Inf.checkFormXProductExists(context, ToolBox_Con.getPreference_Customer_Code(context), myActionsFormButton.productCode.toLong())
            val formXOperationExists = ToolBox_Inf.checkFormXOperationExists(context, ToolBox_Con.getPreference_Customer_Code(context), ToolBox_Con.getPreference_Operation_Code(context))
            val formXSiteExists = ToolBox_Inf.checkFormXSiteExists(
                    context,
                    ToolBox_Con.getPreference_Customer_Code(context),
                    if (mdProductSerial.site_code != null) mdProductSerial.site_code.toString() else ToolBox_Con.getPreference_Site_Code(context)
            )
            var producSiteRestXSite = false
            if (mdProduct.site_restriction == 1) {
                if (mdProductSerial.site_code != null
                        && ToolBox_Con.getPreference_Site_Code(context) == mdProductSerial.site_code.toString()) {
                    producSiteRestXSite = true
                }
            } else {
                producSiteRestXSite = true
            }
            //Se existir form para o produto,site e operação e a regra de restrição de site respeitada,
            //avança para o form
            //Se existir form para o produto,site e operação e a regra de restrição de site respeitada,
            //avança para o form
            if (formXProductExist && formXOperationExists && formXSiteExists && producSiteRestXSite) {
                bundle.putString(MD_ProductDao.PRODUCT_CODE, mdProductSerial.product_code.toString())
                bundle.putString(MD_Product_SerialDao.SERIAL_ID, ToolBox_Inf.removeAllLineBreaks(mdProductSerial.serial_id))
                bundle.putString(MD_ProductDao.PRODUCT_DESC, mdProductSerial.product_desc.trim())
                bundle.putString(MD_ProductDao.PRODUCT_ID, mdProductSerial.product_id.trim())
                bundle.putString(MD_SiteDao.SITE_CODE, if (mdProductSerial.site_code != null) mdProductSerial.site_code.toString() else ToolBox_Con.getPreference_Site_Code(context))
//            bundle.putAll(act081Bundle)
                myActionFilterParam.paramTextFilter = mView.getMketFilter()
                myActionFilterParam.paramItemSelectedTab = mView.getCurrentTab()
                myActionFilterParam.paramItemSelectedPk = null
                myActionFilterParam.paramItemSelectedType = null
                //
                mView.callAct009(bundle)
            } else {
                var msg = hmAux_Trans!!["alert_no_form_lbl"]
                msg += "\n"
                msg = if (!formXProductExist) "$msg${hmAux_Trans!!["alert_no_form_for_product_msg"]}\n" else msg
                msg = if (!formXOperationExists) "$msg${hmAux_Trans!!["alert_no_form_for_operation_msg"]}\n" else msg
                msg = if (!formXSiteExists) "$msg${hmAux_Trans!!["alert_no_form_for_site_msg"]}\n" else msg
                msg = if (!producSiteRestXSite) "$msg${hmAux_Trans!!["alert_site_restriction_violation_msg"]}\n" else msg
                //
                mView.showAlertMsg(
                        hmAux_Trans!!["alert_no_form_ttl"]!!,
                        msg ?: ""
                )
            }
        }else{
            mView.showAlertMsg(
                    hmAux_Trans!!["alert_product_or_serial_not_found_ttl"]!!,
                    hmAux_Trans!!["alert_product_or_serial_not_found_msg"]!!
            )
        }

    }

    private fun getProductInfo(productCode: Int): MD_Product? {
        return productDao.getByString(
                MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        productCode.toLong()
                ).toSqlQuery()
        );
    }

    private fun getSerial(productCode: Int, serialId: String): MD_Product_Serial {
        return serialDao.getByString(
                MD_Product_Serial_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        productCode.toLong(),
                        serialId
                ).toSqlQuery()
        )
    }

    override fun processActionClick(myAction: MyActions) {
        when(myAction.actionType){
            MyActions.MY_ACTION_TYPE_TICKET -> processLocalTicketClick(myAction)
            MyActions.MY_ACTION_TYPE_TICKET_CACHE -> processCachedTicketClick(myAction)
            MyActions.MY_ACTION_TYPE_SCHEDULE -> checkScheduleFlow(myAction)
            MyActions.MY_ACTION_TYPE_FORM_AP -> processFormApClick(myAction)
            MyActions.MY_ACTION_TYPE_FORM -> processFormClick(myAction)
        }
    }

    private fun processLocalTicketClick(myAction: MyActions) {
        mView.callAct070(
                getLocalTicket(
                        myAction
                )
        )
    }

    private fun processCachedTicketClick(myAction: MyActions) {
        if(ToolBox_Con.isOnline(context)){
            mView.setProcess(WS_TK_Ticket_Download::class.java.name)
            mView.showPD(
                    hmAux_Trans?.get("dialog_download_ticket_ttl"),
                    hmAux_Trans?.get("dialog_download_ticket_start")
            )
            //
            prepareWsTicketDownload(myAction)
        }else{
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }

    private fun processFormApClick(myAction: MyActions) {
        mView.callAct038(getFormApBundle(myAction))
    }

    private fun processFormClick(myAction: MyActions) {
        mView.callAct011(getFormBundle(myAction))
    }

    override fun checkScheduleFlow(myAction: MyActions) {
        actionSelected = myAction
        //
        val scheduleExec = getScheduleFromMyAction(myAction)
        //
        if(scheduleExec != null) {
            when (scheduleExec.schedule_type) {
                ConstantBaseApp.MD_SCHEDULE_TYPE_FORM -> processFormFlow(myAction, scheduleExec)
                else -> iniProcessTicketFlow(myAction)
            }
        }
    }

    private fun iniProcessTicketFlow(actions: MyActions) {
        if (ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET, null)) {
            processTicketFlow(actions)
        } else {
            mView.showMsg(
                    Act083_Main.PROFILE_MENU_TICKET_NOT_FOUND,
                    actions
            )
        }
    }

    private fun processFormFlow(actions: MyActions, scheduleExec: MD_Schedule_Exec) {
        if (Constant.SYS_STATUS_SCHEDULE == scheduleExec.status) {
            if (isScheduleSiteDifferentThanLogged(actions)) {
                startSiteChangeFlow(actions)
            } else if (isAnyFormInProcessing(scheduleExec)) {
                mView.showMsg(Act083_Main.MODULE_CHECKLIST_FORM_IN_PROCESSING, actions)
            } else {
                //LUCHE - 14/01/2021
                //Verifica se deve bloquear a execução e em caso posito, exibe msg informando do
                // bloqueio
                if (ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context)) {
                    mView.showMsg(Act083_Main.FREE_EXECUTION_BLOCKED, actions)
                } else {
                    mView.showMsg(Act083_Main.MODULE_CHECKLIST_START_FORM, actions)
                }
            }
        } else {
            if (isStatusPossibleToOpen(scheduleExec)) {
                prepareOpenForm(actions, scheduleExec)
            } else {
                mView.showMsg(
                        Act083_Main.MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN,
                        actions
                )
            }
        }
    }

    private fun isStatusPossibleToOpen(scheduleExec: MD_Schedule_Exec): Boolean {
        return (scheduleExec.status != null
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_CANCELLED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_REJECTED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_IGNORED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_NOT_EXECUTED)
    }


    private fun prepareOpenForm(item: MyActions, scheduleExec: MD_Schedule_Exec) {
        //valida se form existe e ja add info de form_data
        scheduleFormLocalExists(scheduleExec, item)
        //
        val bundle: Bundle = getFormFlowBundle(item, scheduleExec)
        mView.callAct011(bundle)
    }

    private fun getFormFlowBundle(myAction: MyActions, scheduleExec: MD_Schedule_Exec): Bundle {
        val bundle = Bundle()
        bundle.putString(MD_ProductDao.PRODUCT_CODE, scheduleExec.product_code.toString())
        bundle.putString(MD_ProductDao.PRODUCT_DESC, scheduleExec.product_desc.toString())
        bundle.putString(MD_ProductDao.PRODUCT_ID, scheduleExec.product_id.toString())
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, scheduleExec.serial_id.toString())
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, scheduleExec.custom_form_type.toString())
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, scheduleExec.custom_form_type_desc.toString())
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, scheduleExec.custom_form_code.toString())
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, scheduleExec.custom_form_version.toString())
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, scheduleExec.custom_form_desc.toString())
        //
        myAction.scheduleCustomFormData?.let{
            bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, myAction.scheduleCustomFormData)
        }
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, scheduleExec.site_code.toString())
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(myAction.actionType,myAction.processPk)
        //
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT083)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,originFlow)
        return bundle
    }

    override fun checkFormFlow(action: MyActions) {
        val scheduleExec = getScheduleFromMyAction(action)
        scheduleExec?.let {
            if (Constant.SYS_STATUS_SCHEDULE != it.status) {
                prepareOpenForm(action, it)
            } else if (hasSerialDefined(it)) {
                buildRequestSerialDialog(
                        it,
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
                //Cria e exibe dialog que requer serial.
                buildRequestSerialDialog(
                        it,
                        action,
                        true
                )
            }
        }
    }

    private fun buildRequestSerialDialog(scheduleExec: MD_Schedule_Exec, action: MyActions, showDialog: Boolean) {
        //
        serialDialog = ScheduleRequestSerialDialog2(
                context,
                scheduleExec,
                object : ScheduleRequestSerialDialog2.OnScheduleRequestSerialDialogListeners {
                    override fun processToForm() {
                        val bundle = Bundle()
                        if (createFormLocalForSchedule(action, scheduleExec)) {
                            //Atualiza fomr_data no item
                            action.scheduleCustomFormData =
                                    bundle.getString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, "0")
                            //
                            prepareOpenForm(action, scheduleExec)
                        } else {
                            mView.showMsg(Act083_Main.MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR, action)
                        }
                    }

                    override fun processToSearchSerial(serialID: String) {
                        executeSerialSearch(
                                action.productCode,
                                action.productId,
                                serialID,
                                false)
                    }

                    override fun addMketControl(mketSerial: MKEditTextNM) {
                        mView.addControlToActivity(mketSerial)
                    }

                    override fun removeMketControl(mketSerial: MKEditTextNM) {
                        mView.removeControlFromActivity(mketSerial)
                    }
                }
        )
        //
        if (showDialog) {
            serialDialog?.show()
        }
    }

    private fun createFormLocalForSchedule(action: MyActions, scheduleExec: MD_Schedule_Exec): Boolean {
        var daoObjReturn = DaoObjReturn()
        val custom_formDao = GE_Custom_FormDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        val custom_form_field_LocalDao = GE_Custom_Form_Field_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        val custom_form_fieldDao = GE_Custom_Form_FieldDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        val custom_form_blob_localDao = GE_Custom_Form_Blob_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        var creationOk = false
        //
        if (scheduleFormLocalExists(scheduleExec, action)) {
            creationOk = true
        } else {
            //region Implementação2
            val nextFormData = custom_formDao.getByStringHM(
                    GE_Custom_Form_Local_Sql_002(
                            scheduleExec.customer_code.toString(),
                            scheduleExec.custom_form_type.toString(),
                            scheduleExec.custom_form_code.toString(),
                            scheduleExec.custom_form_version.toString()
                    ).toSqlQuery().toLowerCase()
            )
            //
            if (nextFormData != null && nextFormData.size > 0 && nextFormData.hasConsistentValue("id")) {
                val customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context)
                val customForm = custom_formDao.getByString(
                        GE_Custom_Form_Sql_001_TT(
                                scheduleExec.customer_code.toString(),
                                scheduleExec.custom_form_type.toString(),
                                scheduleExec.custom_form_code.toString(),
                                scheduleExec.custom_form_version.toString()
                        ).toSqlQuery().toLowerCase()
                )
                //LUCHE - 15/05/2020 - Comentado pois só era usado para definir url_locla o icone do produto.
                //Add metodo que verifica se img existe local e definir valor.
                //MD_Product productInfo = getProduct(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.PRODUCT_CODE)));
                val customFormLocal = GE_Custom_Form_Local()
                //
                customFormLocal.customer_code = customForm.customer_code
                customFormLocal.custom_form_type = customForm.custom_form_type
                customFormLocal.custom_form_code = customForm.custom_form_code
                customFormLocal.custom_form_version = customForm.custom_form_version
                customFormLocal.custom_form_data = nextFormData["id"]!!.toLong()
                customFormLocal.custom_form_pre = ToolBox_Inf.getPrefix(context)
                customFormLocal.custom_form_status = ConstantBaseApp.SYS_STATUS_SCHEDULE
                customFormLocal.custom_product_code = scheduleExec.product_code
                customFormLocal.custom_product_desc = scheduleExec.product_desc
                customFormLocal.custom_product_id = scheduleExec.product_id
                customFormLocal.custom_form_type_desc = scheduleExec.custom_form_type_desc
                customFormLocal.custom_form_desc = scheduleExec.custom_form_desc
                customFormLocal.serial_id = scheduleExec.serial_id
                customFormLocal.require_signature = customForm.require_signature
                customFormLocal.automatic_fill = customForm.automatic_fill
                customFormLocal.schedule_date_start_format = "${scheduleExec.date_start} $customerGMT"
                customFormLocal.schedule_date_end_format = "${scheduleExec.date_end} $customerGMT"
                customFormLocal.schedule_date_start_format_ms = ToolBox_Inf.dateToMilliseconds("${scheduleExec.date_start} $customerGMT")
                customFormLocal.schedule_date_end_format_ms = ToolBox_Inf.dateToMilliseconds("${scheduleExec.date_end} $customerGMT")
                customFormLocal.require_location = customForm.require_location
                customFormLocal.require_serial_done = customForm.require_serial_done
                customFormLocal.schedule_comments = scheduleExec.comments
                customFormLocal.schedule_prefix = scheduleExec.schedule_prefix
                customFormLocal.schedule_code = scheduleExec.schedule_code
                customFormLocal.schedule_exec = scheduleExec.schedule_exec
                customFormLocal.site_code = scheduleExec.site_code
                customFormLocal.site_id = scheduleExec.site_id
                customFormLocal.site_desc = scheduleExec.site_desc
                //LUCHE - 29/04/2020
                //Após alteração onde o servidor manda "tabelas" temporarias com as infos relacionais
                //do agendamento, agora a informação DEVE ser setado na criação do form.
                customFormLocal.allow_new_serial_cl = scheduleExec.allow_new_serial_cl
                customFormLocal.require_serial = scheduleExec.require_serial
                customFormLocal.serial_rule = scheduleExec.serial_rule
                customFormLocal.serial_max_length = scheduleExec.serial_max_length
                customFormLocal.serial_min_length = scheduleExec.serial_min_length
                customFormLocal.local_control = scheduleExec.local_control
                customFormLocal.product_io_control = scheduleExec.io_control
                customFormLocal.site_restriction = scheduleExec.site_restriction
                customFormLocal.custom_product_icon_name = scheduleExec.product_icon_name
                customFormLocal.custom_product_icon_url = scheduleExec.product_icon_url
                customFormLocal.custom_product_icon_url_local = getProductIconLocalPath(scheduleExec.product_icon_name?.toLowerCase(Locale.getDefault()))
                customFormLocal.require_location = scheduleExec.require_location
                //
                customFormLocal.tag_operational_code = scheduleExec.tag_operational_code
                customFormLocal.tag_operational_id = scheduleExec.tag_operational_id
                customFormLocal.tag_operational_desc = scheduleExec.tag_operational_desc
                //
                //LUCHE -  14/03/2019
                //Alteração Dao de insert com exception NOVO METODO DAO
                //custom_form_LocalDao.addUpdate(customFormLocal);
                daoObjReturn = formLocalDao.addUpdateThrowException(customFormLocal)
                //
                if (!daoObjReturn.hasError()) {
                    //Seta form data na action que será enviado para as proximas acts
                    action.scheduleCustomFormData = customFormLocal.custom_form_data.toString()
                    //
                    val items = custom_form_fieldDao.query_HM(
                            Sql_Act011_002(
                                    customFormLocal.customer_code.toString(),
                                    customFormLocal.custom_form_type.toString(),
                                    customFormLocal.custom_form_code.toString(),
                                    customFormLocal.custom_form_version.toString(),
                                    ToolBox_Con.getPreference_Translate_Code(context),
                                    customFormLocal.custom_form_data.toString()
                            ).toSqlQuery().toLowerCase(Locale.getDefault())
                    ) as ArrayList<HMAux>
                    //
                    custom_form_field_LocalDao.addUpdate(items)
                    //
                    custom_form_blob_localDao.addUpdate(
                            custom_form_blob_localDao.query(
                                    GE_Custom_Form_Blob_Sql_001(
                                            customFormLocal.customer_code.toString(),
                                            customFormLocal.custom_form_type.toString(),
                                            customFormLocal.custom_form_code.toString(),
                                            customFormLocal.custom_form_version.toString()
                                    ).toSqlQuery().toLowerCase(Locale.getDefault())
                            ),
                            false
                    )
                    creationOk = true
                }
            }
        }
        //
        return creationOk
    }

    /**
     * LUCHE - 03/03/2020
     *
     * Metodo que verifica se já existe form_local para o agendamento
     * Se existir, atualiza form_data no bundle
     * @param scheduleExec - Item selecionando
     * @param actions - Bundle a ser enviado e que tera o custom_form_data setado se existir.
     * @return - Verdadeiro se o form_locla ja existir
     */
    private fun scheduleFormLocalExists(scheduleExec: MD_Schedule_Exec, action: MyActions): Boolean {
        val customFormLocal = formLocalDao.getByString(
                MD_Schedule_Exec_Sql_006(
                        scheduleExec.customer_code.toString(),
                        scheduleExec.schedule_prefix.toString(),
                        scheduleExec.schedule_code.toString(),
                        scheduleExec.schedule_exec.toString()
                ).toSqlQuery()
        )
        //
        if (customFormLocal != null) {
            action.scheduleCustomFormData = customFormLocal.custom_form_data.toString()
            return true
        }
        return false
    }

    /**
     * Metodo que verifica se o icone do produto existe e se sim retorno o url_local
     * @param product_icon_name
     * @return
     */
    private fun getProductIconLocalPath(productIconName: String?): String? {
         if (productIconName != null && ToolBox_Inf.verifyDownloadFileInf(productIconName, Constant.CACHE_PATH)) {
                return  productIconName
            }
        return null
    }

    private fun processTicketFlow(myAction: MyActions) {
        val scheduleExec = getScheduleFromMyAction(myAction)
        if(scheduleExec != null) {
            if (ConstantBaseApp.SYS_STATUS_SCHEDULE != scheduleExec.status) {
                if (isScheduleStatusPossibleToOpen(scheduleExec!!)) {
                    prepareOpenTicket(myAction, scheduleExec)
                } else {
                    mView.showMsg(
                            Act083_Main.MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN,
                            myAction
                    )
                }
            } else {
                if (isScheduleSiteDifferentThanLogged(myAction)) {
                    startSiteChangeFlow(myAction)
                } else {
                    //LUCHE - 14/01/2021
                    //Verifica se deve bloquear a execução e em caso posito, exibe msg informando do
                    // bloqueio
                    if (ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context)) {
                        mView.showMsg(Act083_Main.FREE_EXECUTION_BLOCKED, myAction)
                    } else {
                        mView.showMsg(
                                Act083_Main.MODULE_TICKET_EXEC_CONFIRM,
                                myAction
                        )
                    }
                }
            }
        }
    }

    fun checkTicketFlow(item: MyActions) {
        var ticket_prefix: Int = 0
        var ticket_code: Int = 0
        var result: Boolean = false

        val splippedPk = item.getSplippedPk()
        val scheduleTicket = getTicketBySchedule(splippedPk.get(0).toInt(), splippedPk.get(1).toInt(), splippedPk.get(2).toInt())
        val scheduleExec = getScheduleFromMyAction(item)

        if (scheduleTicket != null && TK_Ticket.isValidTkTicket(scheduleTicket)) {
           result = true
        }else{
            val createTicketForSchedule = createTicketForSchedule(item, scheduleExec)
            if(createTicketForSchedule != null){
                ticket_prefix = createTicketForSchedule.ticket_prefix
                ticket_code = createTicketForSchedule.ticket_code
                result = true
            }else{
                result = false
            }
        }

        if (result) {
            mView.callAct071(getTicketActionFlowBundle(item, scheduleExec!!, ticket_prefix, ticket_code, 1)!!)
        } else {
            mView.showMsg(
                    Act083_Main.MODULE_SCHEDULE_TICKET_CREATION_ERROR,
                    item
            )
        }

    }

    private fun getTicketActionFlowBundle(myAction: MyActions, scheduleExec: MD_Schedule_Exec, ticket_prefix: Int, ticket_code: Int, ticket_seq: Int): Bundle {
        val bundle = Bundle()
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(myAction.actionType,myAction.processPk)
        //
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT083)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,originFlow)
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_PREFIX, scheduleExec.schedule_prefix)
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_CODE, scheduleExec.schedule_code)
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_EXEC, scheduleExec.schedule_exec)
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticket_prefix)
        bundle.putInt(TK_TicketDao.TICKET_CODE, ticket_code)
        //16/03/2020 - foi convencionado que durante a criação da execução do ticket, o ticket id,
        //será o igual ao do exibido nas celulas do agendamento.
        bundle.putString(TK_TicketDao.TICKET_ID, ToolBox_Inf.getFormattedTicketSeqExec(
                myAction.processPk,
                ticket_prefix.toString(),
                ticket_code.toString()
        )
        )
        //bundle.putString(TK_TicketDao.TYPE_PATH, item.get(TK_TicketDao.TYPE_PATH));
        bundle.putString(TK_TicketDao.TYPE_DESC, scheduleExec.ticket_type_desc)
        bundle.putBoolean(Act070_Main.PARAM_DENIED_BY_CHECKIN, false)
        bundle.putString(Constant.ACT_SELECTED_DATE, calendarDate)
        bundle.putString(MD_Schedule_ExecDao.SCHEDULE_PK, myAction.processPk)
        //
        //LUCHE - 14/08/2020 - Criação de action agendado v2
        val ctrlItem = getScheduleCtrlIfExists(scheduleExec, ticket_prefix, ticket_code)
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

    private fun getScheduleCtrlIfExists(item: MD_Schedule_Exec, ticket_prefix: Int, ticket_code: Int): TK_Ticket_Ctrl? {
        return ticketCtrlDao.getByString(
                Sql_Act017_005(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        item.schedule_prefix.toString(),
                        item.schedule_code.toString(),
                        item.schedule_exec.toString(),
                        ticket_prefix.toString(),
                        ticket_code.toString()
                ).toSqlQuery()
        )
    }

    private fun isScheduleSiteDifferentThanLogged(item: MyActions): Boolean {
        item.siteCode?.let {
            return it.toString() != ToolBox_Con.getPreference_Site_Code(context)
        }
        return false
    }

    fun setSeletedActionInfosIntoFilterParam(myActionType: String,myActionPk: String){
        if(::myActionFilterParam.isInitialized){
            myActionFilterParam.originFlow = originFlow
            myActionFilterParam.setSelectedItemParams(
                    mView.getMketFilter(),
                    mView.getCurrentTab(),
                    myActionType,
                    myActionPk
            )
        }
    }

    override fun getLocalTicket(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(myAction.actionType,myAction.processPk)
        //
        return ticketBundle(splippedPk[0].toInt(), splippedPk[1].toInt())
    }

    override fun getFormApBundle(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        val bundle = Bundle()
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(myAction.actionType,myAction.processPk)
        //
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT083)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,originFlow)
        //
        bundle.putString(GE_Custom_Form_ApDao.CUSTOMER_CODE, ToolBox_Con.getPreference_Customer_Code(context).toString())
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, splippedPk[0])
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, splippedPk[1])
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, splippedPk[2])
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, splippedPk[3])
        bundle.putString(GE_Custom_Form_ApDao.AP_CODE, splippedPk[4])
        return bundle
    }

    override fun getFormBundle(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        val bundle = Bundle()
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(myAction.actionType,myAction.processPk)
        //
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT083)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,originFlow)
        //
        bundle.putString(MD_ProductDao.PRODUCT_CODE, myAction.productCode.toString())
        bundle.putString(MD_ProductDao.PRODUCT_DESC, myAction.productDesc)
        //bundle.putString(MD_ProductDao.PRODUCT_ID, myAction.productDesc)
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, myAction.serialId)
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, splippedPk[0])
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, myAction.customFormTypeDesc)
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, splippedPk[1])
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, splippedPk[2])
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, myAction.customFormDesc)
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, splippedPk[3])
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, myAction.siteCode.toString())
        return bundle
    }

    override fun prepareWsTicketDownload(myAction: MyActions) {
        val mIntent = Intent(context, WBR_TK_Ticket_Download::class.java)
        val bundle = Bundle()
        bundle.putString(
                TK_TicketDao.TICKET_PREFIX,
                "${ToolBox_Con.getPreference_Customer_Code(context)}|${myAction.processPk.replace(".", "|")}"
        )
        mIntent.putExtras(bundle)
        //
        context.sendBroadcast(mIntent)
    }

    private fun prepareOpenTicket(item: MyActions, scheduleExec: MD_Schedule_Exec) {
        val splippedPk = item.getSplippedPk()
        val scheduleTicket = getTicketBySchedule(splippedPk.get(0).toInt(), splippedPk.get(1).toInt(), splippedPk.get(2).toInt())
        val ticketPrefix = scheduleTicket?.ticket_prefix?:0
        val ticketCode = scheduleTicket?.ticket_code?:0
        if (scheduleTicket!= null
                && ticketPrefix > 0
                && ticketCode > 0) {
            mView.callAct070(getTicketFlowBundle(item, ticketPrefix, ticketCode))
        } else {
            mView.callAct071(getTicketActionFlowBundle(item, scheduleExec!!, ticketPrefix, ticketCode, 1))
        }
    }

    private fun getTicketFlowBundle(myAction: MyActions, ticketPrefix: Int, ticketCode: Int): Bundle {
        val bundle = Bundle()
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(myAction.actionType,myAction.processPk)
        //
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT083)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,originFlow)
        //
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticketPrefix)
        bundle.putInt(TK_TicketDao.TICKET_CODE, ticketCode)
        //bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, ToolBox_Inf.convertStringToInt(item.get(TK_Ticket_CtrlDao.TICKET_SEQ)))
        bundle.putString(Constant.ACT_SELECTED_DATE, calendarDate)
        bundle.putString(MD_Schedule_ExecDao.SCHEDULE_PK, myAction.processPk)
        return bundle
    }

    private fun startSiteChangeFlow(item: MyActions) {
        //Verifica se o usuario possui acesso ao site do form com restrição
        //Se possuir, da opção do usr alterar para o site se não, apenas informa
        //sobre a restrição.
        if (hasScheduleSiteAccess(item.siteCode)) {
            ToolBox.alertMSG_YES_NO(
                    context,
                    hmAux_Trans!!["alert_form_site_restriction_ttl"],
                    hmAux_Trans!!["alert_form_site_restriction_confirm"],
                    { dialog, which ->
                        if (!ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)
                                && !ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI, null)) {
                            ToolBox_Con.setPreference_Site_Code(context, item.siteCode.toString())
                            ToolBox_Con.setPreference_Zone_Code(context, -1)
                            //
                            checkScheduleFlow(item)
                        } else {
                            ToolBox_Con.setPreference_Site_Code(context, item.siteCode.toString())
                            ToolBox_Con.setPreference_Zone_Code(context, -1)
                            mView.callAct033()
                        }
                    },
                    1
            )
        } else {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans!!["alert_form_site_restriction_ttl"],
                    hmAux_Trans!!["alert_form_site_restriction_no_access_msg"],
                    null,
                    0
            )
        }
    }

    override fun isScheduleStarted(myAction: MyActions): Boolean {
        val scheduleExec = getScheduleFromMyAction(myAction)
        return scheduleExec != null && !scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_SCHEDULE)
    }

    private fun getScheduleFromMyAction(myAction: MyActions): MD_Schedule_Exec? {
        val splippedPk = myAction.getSplippedPk()
        //
        val schedule_prefix = splippedPk.get(0).toInt()
        val schedule_code = splippedPk.get(1).toInt()
        val schedule_exec = splippedPk.get(2).toInt()
        //
        val scheduleExec: MD_Schedule_Exec? = scheduleDao.getByString(
                MD_Schedule_Exec_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        schedule_prefix,
                        schedule_code,
                        schedule_exec
                ).toSqlQuery()
        )
        //
        return scheduleExec
    }

    private fun isScheduleStatusPossibleToOpen(scheduleExec: MD_Schedule_Exec): Boolean {
        //
        return (!scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_CANCELLED)
                && !scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_REJECTED)
                && !scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_IGNORED)
                && !scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_NOT_EXECUTED))
    }

    override fun isScheduleFormType(myAction: MyActions): Boolean {
        val scheduleExec = getScheduleFromMyAction(myAction)

        return scheduleExec != null && scheduleExec.schedule_type.equals(scheduleExec.custom_form_type_desc)
    }

    private fun isAnyFormInProcessing(scheduleExec: MD_Schedule_Exec): Boolean {
        val customFormLocal = formLocalDao.getByString(
                GE_Custom_Form_Local_Sql_003(
                        scheduleExec.customer_code.toString(),
                        scheduleExec.custom_form_type.toString(),
                        scheduleExec.custom_form_code.toString(),
                        scheduleExec.custom_form_version.toString(),
                        "0",
                        scheduleExec.product_code.toString(),
                        scheduleExec.serial_id
                ).toSqlQuery()
        )
        return customFormLocal != null
    }


    private fun createTicketForSchedule(item: MyActions, scheduleExec: MD_Schedule_Exec?): TK_Ticket? {
        val nextTicketCode: Int = getNextScheduleTicketCode()
        val md_site = getSiteObj(ToolBox_Con.getPreference_Site_Code(context))
        val mdOperation = getOperationObj(ToolBox_Con.getPreference_Operation_Code(context))
        //
        if (nextTicketCode > 0 && MD_Site.isValid(md_site) && MD_Operation.isValid(mdOperation)) {
            //Cria ticket
            val tkTicket = createTicket(item, scheduleExec!!, nextTicketCode, md_site!!, mdOperation!!)
            //Add ctrl e action ao ticket
            //TODO REVE COMO FAZER AGORA QUE CTRL É DO STEP
            tkTicket?.let {
                it.step?.add(
                        createStep(it)
                )
                if (updateScheduleStatus(it.schedule_prefix!!, it.schedule_code!!, it.schedule_exec!!, ConstantBaseApp.SYS_STATUS_PROCESS)) {
                    val daoObjReturn = ticketDao.addUpdate(tkTicket)
                    //
                    if (!daoObjReturn.hasError()) {
                        //LUCHE - 18/01/2021 - Implementação de licença por site.
                        //Caso o customer user licença por site, mas o site logado não controla licença,
                        // o numero de execução deve ser controlado.
                        if (ToolBox_Inf.isConcurrentBySiteLicense(context) && ToolBox_Inf.isSiteLicenseDisabled(context)) {
                            incrementAppExecutionCount(md_site)
                        }
                        //
                        return tkTicket
                    } else {
                        updateScheduleStatus(it.schedule_prefix!!, it.schedule_code!!, it.schedule_exec!!, ConstantBaseApp.SYS_STATUS_SCHEDULE)
                    }
                }
            }

        }
        return null
    }

    private fun createTicket(item: MyActions, scheduleExec: MD_Schedule_Exec, nextTicketCode: Int, md_site: MD_Site, mdOperation: MD_Operation): TK_Ticket? {
        val tkTicket = TK_Ticket()
        //
        tkTicket.customer_code = ToolBox_Con.getPreference_Customer_Code(context)
        tkTicket.ticket_prefix = 0
        tkTicket.ticket_code = nextTicketCode
        tkTicket.scn = 0
        tkTicket.ticket_id = """0.$nextTicketCode"""
        tkTicket.type_code = scheduleExec.ticket_type!!
        tkTicket.type_id = scheduleExec.ticket_type_id!!
        tkTicket.type_desc = scheduleExec.ticket_type_desc!!
        tkTicket.open_date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
        tkTicket.open_user = ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context))
        tkTicket.open_user_name = ToolBox_Inf.getFullNick(
                ToolBox_Con.getPreference_User_Code_Nick(context),
                ToolBox_Con.getPreference_User_Code(context)
        )
        tkTicket.open_site_code = ToolBox_Inf.convertStringToInt(md_site.site_code)
        tkTicket.open_site_id = md_site.site_id
        tkTicket.open_site_desc = md_site.site_desc
        tkTicket.open_operation_code = mdOperation.operation_code.toInt()
        tkTicket.open_operation_id = mdOperation.operation_id
        tkTicket.open_operation_desc = mdOperation.operation_desc
        tkTicket.open_product_code = scheduleExec.product_code
        tkTicket.open_product_id = scheduleExec.product_id
        tkTicket.open_product_desc = scheduleExec.product_desc
        tkTicket.open_serial_code = scheduleExec.serial_code!!
        tkTicket.open_serial_id = scheduleExec.serial_id
        tkTicket.ticket_status = ConstantBaseApp.SYS_STATUS_PROCESS
        //
        tkTicket.origin_type = ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_SCHEDULE
        tkTicket.origin_desc = ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_SCHEDULE
        //
        tkTicket.schedule_prefix = scheduleExec.schedule_prefix
        tkTicket.schedule_code = scheduleExec.schedule_code
        tkTicket.schedule_exec = scheduleExec.schedule_exec
        //
        tkTicket.tag_operational_code = scheduleExec.tag_operational_code
        tkTicket.tag_operational_id = scheduleExec.tag_operational_id
        tkTicket.tag_operational_desc = scheduleExec.tag_operational_desc
        //
        return tkTicket
    }
    private fun createStep(tkTicket: TK_Ticket): TK_Ticket_Step? {
        val ticketStep = TK_Ticket_Step()
        ticketStep.step_code = 0
        ticketStep.step_order = 0
        ticketStep.exec_type = ConstantBaseApp.TK_PIPELINE_STEP_TYPE_ONE_TOUCH
        ticketStep.scan_serial = 0
        ticketStep.allow_new_obj = 0
        ticketStep.move_next_step = 1
        ticketStep.step_start_date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
        ticketStep.step_start_user = ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context))
        ticketStep.step_start_user_nick = ToolBox_Inf.getFullNick(
                ToolBox_Con.getPreference_User_Code_Nick(context),
                ToolBox_Con.getPreference_User_Code(context)
        )
        ticketStep.step_status = ConstantBaseApp.SYS_STATUS_PENDING
        ticketStep.user_focus = 1
        /**
         * BARRIONUEVO 16-09-2020
         * Criando com update_required =0 para evitar se enviado ao servidor quando user desiste de
         * finalizar a action.
         */
        ticketStep.update_required = 0
        ticketStep.setPK(tkTicket)
        //        ticketStep.getCtrl().add(
//            createTicketCtrl(item, tkTicket, md_site, mdOperation)
//        );
        return ticketStep
    }

    /**
     * LUCHE - 14/02/2020
     *
     *
     * Atualiza status da tabela de agendamentos.
     *
     * @param schedule_prefix
     * @param schedule_code
     * @param schedule_exec
     * @param status
     * @return
     */
    private fun updateScheduleStatus(schedule_prefix: Int, schedule_code: Int, schedule_exec: Int, status: String): Boolean {
        val scheduleExec: MD_Schedule_Exec = scheduleDao.getByString(
                MD_Schedule_Exec_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        schedule_prefix,
                        schedule_code,
                        schedule_exec
                ).toSqlQuery()
        )
        //
        if (MD_Schedule_Exec.isValidScheduleExec(scheduleExec)) {
            scheduleExec.status = status
            val daoObjReturn: DaoObjReturn = scheduleDao.addUpdate(scheduleExec)
            //Retorna verdadeiro se não teve erro.
            return !daoObjReturn.hasError()
        }
        //
        return false
    }

    /**
     * LUCHE - 18/01/2021
     * Metodo que incrementa 1 no contador de execuções do app no registro do site.
     * @param md_site
     */
    private fun incrementAppExecutionCount(md_site: MD_Site) {
        md_site.app_executions_count = md_site.app_executions_count + 1
        siteDao.addUpdate(md_site)
    }

    private fun getSiteObj(site_code: String): MD_Site? {
        return siteDao.getByString(
                MD_Site_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        site_code
                ).toSqlQuery()
        )
    }


    private fun getOperationObj(operationCode: Long): MD_Operation? {
        val operationDao =  MD_OperationDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        )
        //
        return operationDao.getByString(
                MD_Operation_Sql_004(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        operationCode
                ).toSqlQuery()
        )
    }

    /**
     * LUCHE - 11/03/2020
     *
     *
     * Metodo que pega o proximo ticketCode para tickets criados via agendamento.
     * O tickets criados via agendamento, terão sempre o prefixo  = 0.
     * @return - Proximo ticket code  ou -1 em caso de erro.
     */
    private fun getNextScheduleTicketCode(): Int {
        val auxCode = ticketDao.getByStringHM(
                TK_Ticket_Sql_010(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        )
        //
        if (auxCode != null && auxCode.size > 0 && auxCode.hasConsistentValue(TK_Ticket_Sql_010.NEXT_SCHEDULE_TICKET_CODE)) {
            try {
                return auxCode[TK_Ticket_Sql_010.NEXT_SCHEDULE_TICKET_CODE]!!.toInt()
            } catch (e: Exception) {
                ToolBox_Inf.registerException(javaClass.name, e)
            }
        }
        return -1
    }


    private fun hasSerialDefined(scheduleExec: MD_Schedule_Exec): Boolean {
        return !scheduleExec.serial_id.isNullOrBlank()
                && !scheduleExec.serial_id.isNullOrEmpty()
    }

    private fun executeSerialSearch(productCode: Int?, productId: String?, serialId: String, searchExact: Boolean) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setProcess(WS_Serial_Search::class.java.name)
            //
            mView.showPD(
                    hmAux_Trans!!["dialog_serial_search_ttl"],
                    hmAux_Trans!!["dialog_serial_search_start"]
            )
            //
            val mIntent = Intent(context, WBR_Serial_Search::class.java)
            val bundle = Bundle()
            //
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, productCode.toString())
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, productId.toString())
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serialId)
            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, if (searchExact) 1 else 0)
            bundle.putBoolean(ConstantBaseApp.SCHEDULED_PROFILE_CHECK, false)
            //
            mIntent.putExtras(bundle)
            //
            context.sendBroadcast(mIntent)
        } else {
            offlineSerialSearch()
        }
    }

    override fun extractSearchResult(result: String?) {
        val gson = GsonBuilder().serializeNulls().create()
        val rec = gson.fromJson(
                result,
                TSerial_Search_Rec::class.java)
        //
        val serialList = rec.record
        //
        defineSearchResultFlow(serialList, rec.record_count, rec.record_page)
    }

    /**
     * LUCHE - 02/03/2020
     *
     * Metodo que busca o serial offline
     */
    private fun offlineSerialSearch() {
        val item: MD_Schedule_Exec = serialDialog!!.auxSchedule
        val serialToUse = if (!item.serial_id.isNullOrEmpty()){
                                item.serial_id
                            } else{
                                serialDialog?.serialId ?: ""
                            }
        val serialList: ArrayList<MD_Product_Serial> = hasLocalSerial(
                item.product_id,
                serialToUse!!
        )
        //
        //
        if (serialList.size > 0) {
            defineSearchResultFlow(serialList, serialList.size.toLong(), serialList.size.toLong())
        } else {
            if (item.allow_new_serial_cl == 0 && item.require_serial == 1){
                ToolBox_Inf.showNoConnectionDialog(context)
            } else {
                defineSearchResultFlow(serialList, serialList.size.toLong(), serialList.size.toLong())
            }
        }
    }

    private fun defineSearchResultFlow(serialList: ArrayList<MD_Product_Serial>, record_count: Long, record_page: Long) {
        val scheduleExec: MD_Schedule_Exec = serialDialog!!.auxSchedule
        //
        if (ToolBox_Inf.productConfigPreventToProceed(scheduleExec) && (serialList == null || serialList.size == 0)) {
            //Se serial não definido, significa que não avançou para proxima tela pois o produto não permite criação de serial.
            mView.showMsg(
                    if (!scheduleExec.serial_id.isNullOrEmpty()) Act083_Main.EMPTY_SERIAL_SEARCH else Act083_Main.SERIAL_CREATION_DENIED,
                    actionSelected!!
            )
        } else {
            val serialId =
                    if (!scheduleExec.serial_id.isNullOrEmpty()){
                        scheduleExec.serial_id?:""
                    } else{
                        serialDialog?.let{
                            it.serialId?:""
                        }?:""
                    }
            val idx: Int = getIdxIfEquals(
                    serialList,
                    scheduleExec.product_code,
                    serialId
            )
            //
            val bundle = Bundle()
            bundle.putString(MD_ProductDao.PRODUCT_ID, scheduleExec.product_id)
            if (idx >= 0) {
                val serialArrayList = ArrayList<MD_Product_Serial>()
                serialArrayList.add(serialList[idx])
                //
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true)
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serialArrayList)
            } else {
                if (serialList.size == 1 && serialList[0].serial_id == serialDialog!!.serialId) {
                    bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true)
                    bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serialList)
                } else {
                    bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, false)
                    bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serialList)
                }
            }
            //
            bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, serialDialog!!.serialId)
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, record_count)
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, record_page)
            //bundle.putString(ConstantBaseApp.ACT_SELECTED_DATE, item[Act017_Main.ACT017_ADAPTER_DATE_REF])
            bundle.putString(ConstantBaseApp.ACT_SELECTED_DATE, calendarDate)
            bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE, scheduleExec.custom_form_type.toString())
            bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE, scheduleExec.custom_form_code.toString())
            bundle.putString(Constant.ACT010_CUSTOM_FORM_VERSION, scheduleExec.custom_form_version.toString())
            bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, scheduleExec.custom_form_type_desc)
            bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, scheduleExec.custom_form_desc)
            bundle.putString(MD_Schedule_ExecDao.SCHEDULE_PK, ToolBox_Inf.formatSchedulePk(scheduleExec.schedule_prefix, scheduleExec.schedule_code, scheduleExec.schedule_exec))
            bundle.putString(Constant.ACT017_SCHEDULED_SITE, scheduleExec.site_code.toString())
            //
            //
            if (createFormLocalForSchedule(actionSelected!!, scheduleExec)) {
                /*
                 * BARRIONUEVO 13-04-2020
                 * Mudanca de ultima hora: adicionar flag para dar bypass em restricoes de serial.
                 */

                //Seta form data no bundle que será enviado para as proximas acts
                bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, actionSelected!!.scheduleCustomFormData.toString())
                bundle.putBoolean(ConstantBaseApp.SCHEDULED_PROFILE_CHECK, false)
                //
                setSeletedActionInfosIntoFilterParam(MyActions.MY_ACTION_TYPE_SCHEDULE,actionSelected!!.processPk)
                bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
                bundle.putSerializable(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, originFlow)
                //
                mView.callAct020(bundle)
            } else {
                mView.showMsg(Act083_Main.MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR, actionSelected!!)
            }
        }
    }

    /**
     * LUCHE - 02/03/2020
     *
     * Metodo que retorna o indice do serial buscado.
     * Faz loop na lista de seriais retornados e caso encontre o serial, retorna seu indice.
     *
     * @param serial_list - Lista de seriais encontradas
     * @param productCode - Codigo do produto buscado
     * @param serialId - Id do serial buscado
     * @return - Retorna indice do serial buscado ou -1 se serial não encontrado.
     */
    private fun getIdxIfEquals(serialList: ArrayList<MD_Product_Serial>, productCode: Int, serialId: String): Int {
        for (i in serialList.indices) {
            val serial: MD_Product_Serial = serialList.get(i)
            if (serial.product_code == productCode.toLong() && serial.serial_id.equals(serialId, true)) {
                return i
            }
        }
        //
        return -1
    }

    private fun hasLocalSerial(product_id: String, serial_id: String): ArrayList<MD_Product_Serial> {
        val serialDao = MD_Product_SerialDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        )
        return serialDao.query(
                Sql_Act020_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context),
                        product_id,
                        serial_id,
                        ""
                ).toSqlQuery()
        ) as ArrayList<MD_Product_Serial>
    }

    override fun hasScheduleSiteAccess(siteCode: Int?): Boolean {
        var access = false
        //
        val formSite = getSiteObj(siteCode.toString())
        //
        if (formSite != null && formSite.site_code.equals(siteCode.toString())) {
            access = true
        }
        //
        return access
    }

    override fun verifyProductOutdateForForm(hmAuxTicketDownloaded: HMAux): Boolean {
        val ticketPrefix = hmAuxTicketDownloaded[TK_TicketDao.TICKET_PREFIX]?.let { Integer.valueOf(it) } ?: -1
        val ticketCode = hmAuxTicketDownloaded[TK_TicketDao.TICKET_CODE]?.let { Integer.valueOf(it) } ?: -1
        //
        return ToolBox_Inf.hasFormProductOutdate(context, ticketPrefix, ticketCode)
    }

    override fun prepareWsFormSync() {
        val data_package = arrayListOf(DataPackage.DATA_PACKAGE_CHECKLIST)
        val mIntent = Intent(context, WBR_Sync::class.java)
        val bundle = Bundle()
        bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context))
        bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package)
        bundle.putLong(Constant.GS_PRODUCT_CODE, 0)
        bundle.putInt(Constant.GC_STATUS_JUMP, 1)
        bundle.putInt(Constant.GC_STATUS, 1)
        //
        mIntent.putExtras(bundle)
        //
        context.sendBroadcast(mIntent)
    }

    override fun getCacheTicketBundle(hmAuxTicketDownloaded: HMAux): Bundle {
        val ticketPrefix = hmAuxTicketDownloaded[TK_TicketDao.TICKET_PREFIX]?.let { Integer.valueOf(it) } ?: -1
        val ticketCode = hmAuxTicketDownloaded[TK_TicketDao.TICKET_CODE]?.let { Integer.valueOf(it) } ?: -1
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(MyActions.MY_ACTION_TYPE_TICKET,"$ticketPrefix.$ticketCode")
        //
        return ticketBundle(ticketPrefix, ticketCode)
    }

    private fun ticketBundle(ticketPrefix: Int, ticketCode: Int): Bundle {
        val bundle = Bundle()
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT083)
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticketPrefix)
        bundle.putInt(TK_TicketDao.TICKET_CODE, ticketCode)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, originFlow)
        return bundle
    }

    private fun recoverIntentsInfo() {
        myActionFilterParam = bundle.getSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM) as MyActionFilterParam
        originFlow = bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT005)
        siteCodeBack = ToolBox_Con.getPreference_Site_Code(context)
        zoneCodeBack = ToolBox_Con.getPreference_Zone_Code(context)
        initialTextFilter = myActionFilterParam.paramTextFilter
        initialTabToLoad = myActionFilterParam.paramItemSelectedTab ?: 1
        _lastSelectedActionPk = myActionFilterParam.paramItemSelectedPk
        _lastSelectedActionType = myActionFilterParam.paramItemSelectedType
    }

    private fun loadFilters() {
        tagFilter = myActionFilterParam.tagFilterCode
        productCode = myActionFilterParam.productCode
        serialId = myActionFilterParam.serialId
        ticketId = myActionFilterParam.ticketId
        clientId = myActionFilterParam.clientId
        contractId = myActionFilterParam.contractId
        calendarDate = myActionFilterParam.calendarDate
        siteCode =
        if(setSiteFilter()){
            ToolBox_Con.getPreference_Site_Code(context)
        }else{
            null
        }
    }

    private fun setSiteFilter(): Boolean {
        return  ConstantBaseApp.PREFERENCE_HOME_CURRENT_SITE_OPTION == ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_SITES_FILTER, ConstantBaseApp.PREFERENCE_HOME_ALL_SITE_OPTION)
                && !ToolBox_Inf.usesSoMainActivity(context)
    }


    private fun generateMyActionList(tabUserFocusFilter: Int) {
        _myActionsList.clear()
        //Cancela a coroutine em execução caso ainda exista.
        launch?.let {
            if(it.isActive){
                it.cancel()
            }
        }
        //
        launch = CoroutineScope(Dispatchers.IO).launch {
            _myActionsList.addAll(
                    getLocalTickets(tabUserFocusFilter).map {
                        val lastTicketSelected = getLastSelectedPk(MyActions.MY_ACTION_TYPE_TICKET)
                        TK_Ticket.toMyActionsObj(context, it,lastTicketSelected)
                    }
            )
            //
            _myActionsList.addAll(
                    getCachedTickets(tabUserFocusFilter).map {
                        val lastTicketCacheSelected = getLastSelectedPk(MyActions.MY_ACTION_TYPE_TICKET_CACHE)
                        it.toMyActionsObj(context, lastTicketCacheSelected)
                    }
            )
            //
            if (!ConstantBaseApp.ACT068.equals(originFlow, true)) {
                _myActionsList.addAll(
                        getSchedules(tabUserFocusFilter).map {
                            val lastScheduleSelected = getLastSelectedPk(MyActions.MY_ACTION_TYPE_SCHEDULE)
                            it.toMyActionsObj(context,lastScheduleSelected)
                        }
                )
            }
            //
            if (!ConstantBaseApp.ACT068.equals(originFlow, true)) {
                _myActionsList.addAll(
                        getFormAp(tabUserFocusFilter).map {
                            val lastFormApSelected = getLastSelectedPk(MyActions.MY_ACTION_TYPE_FORM_AP)
                            it.toMyActionsObj(context,lastFormApSelected)
                        }
                )
            }
            //
            if (!ConstantBaseApp.ACT068.equals(originFlow, true)) {
                myActionsList.addAll(
                        getLocalForms(tabUserFocusFilter).map {
                            val lastFormSelected = getLastSelectedPk(MyActions.MY_ACTION_TYPE_FORM)
                            GE_Custom_Form_Local.toMyActionsObj(context, it, lastFormSelected)
                        }
                )
            }
            //
            if (ConstantBaseApp.ACT006.equals(originFlow, true) && tabUserFocusFilter == 1 && ::myActionFilterParam.isInitialized) {
                _myActionsList.add(
                        createMyActionFormCreation()
                )
            }
            //
            _myActionsList.sortBy {
                when (it) {
                    is MyActions -> it.orderBy
                    is MyActionsFormButton -> it.orderBy
                    else -> "190001010000"
                }
            }
            //
            withContext(Dispatchers.Main) {
                mView.changeProgressBarVisility(false)
                mView.iniRecycler()
            }
        }
    }

    /**
     * Fun que retrona pk do item navegado caso seja do mesmo tipo da action
     * passada. Caso contario null.
     * Usado para passar a pk somente para a lista de acton do memso tipo da navegada
     */
    private fun getLastSelectedPk(myActionType: String) =
            if (_lastSelectedActionType == myActionType) {
                _lastSelectedActionPk
            } else {
                null
            }

    private fun createMyActionFormCreation(): MyActionsFormButton {
            return MyActionsFormButton(
                    productCode!!,
                    myActionFilterParam.productDesc!!,
                    serialId!!,
                    hmAux_Trans!!["new_form_lbl"]!!
            )
    }

    private fun getLocalTickets(userFocus: Int): MutableList<HMAux> {
        //
        return ticketDao.query_HM(
                SqlAct083_002(
                        context,
                        originFlow,
                        ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                        tagFilter,
                        siteCode,
                        productCode,
                        serialId,
                        clientId,
                        contractId,
                        ticketId,
                        calendarDate,
                        userFocus,
                        hmAux_Trans?.get("other_steps_available_lbl")
                ).toSqlQuery()
        )
    }

    private fun getCachedTickets(userFocus: Int): MutableList<TkTicketCache> {
        return ticketCacheDao.query(
                SqlAct083_001(
                        context,
                        originFlow,
                        ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                        tagFilter,
                        siteCode,
                        productCode,
                        serialId,
                        clientId,
                        contractId,
                        ticketId,
                        calendarDate,
                        userFocus
                ).toSqlQuery()
        )
    }

    fun getSchedules(userFocus: Int): MutableList<MD_Schedule_Exec> {
        return scheduleDao.query(
                SqlAct083_005(
                        context,
                        originFlow,
                        ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                        tagFilter,
                        productCode,
                        serialId,
                        siteCode,
                        calendarDate,
                        userFocus
                ).toSqlQuery()
        )
    }

    private fun getFormAp(userFocus: Int): MutableList<GE_Custom_Form_Ap> {
        return formApDao.query(
                SqlAct083_003(
                        context,
                        originFlow,
                        ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                        tagFilter,
                        productCode,
                        serialId,
                        calendarDate,
                        userFocus
                ).toSqlQuery()
        )
    }

    private fun getLocalForms(userFocus: Int): MutableList<HMAux> {
        val lbl = hmAux_Trans?.get("form_lbl") ?: "FORMULARIO"

        return formLocalDao.query_HM(
                SqlAct083_004(
                        originFlow,
                        ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                        tagFilter,
                        productCode,
                        serialId,
                        calendarDate,
                        lbl,
                        userFocus
                ).toSqlQuery()
        )
    }

    private fun getTicketBySchedule(schedule_prefix: Int, schedule_code: Int, schedule_exec: Int): TK_Ticket? {
        return ticketDao.getByString(
                TK_Ticket_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        schedule_prefix,
                        schedule_code,
                        schedule_exec
                ).toSqlQuery()
        )
    }

    override fun onBackPressedClicked() {
        when(originFlow){
            ConstantBaseApp.ACT006 -> mView.callAct006(getBundleToAssetsAndLocalOrigin())
            ConstantBaseApp.ACT016 -> mView.callAct016(getBundleToCalendarOrigin())
            ConstantBaseApp.ACT068 -> mView.callAct068(getBundleToSearchOrigin())
            else ->  mView.callAct005()
        }

    }


    private fun getBundleToAssetsAndLocalOrigin(): Bundle {
        return Bundle().apply {
            putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, myActionFilterParam.productId)
            putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, serialId)
        }
    }

    private fun getBundleToCalendarOrigin(): Bundle {
        return Bundle().apply {
            putString(ConstantBaseApp.ACT_SELECTED_DATE, calendarDate)
        }
    }

    private fun getBundleToSearchOrigin(): Bundle {
        return Bundle()
    }


}