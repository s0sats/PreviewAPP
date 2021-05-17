package com.namoadigital.prj001.ui.act083

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_Sync
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.ui.act017.Act017_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

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
    private var _myActionsList = mutableListOf<MyActions>()
    var myActionsList = mutableListOf<MyActions>()
        get() {
            return _myActionsList
        }
    private val _hmAux_Trans: HMAux? by lazy {
        loadTranslation()
    }
    var hmAux_Trans: HMAux? = HMAux()
        get() {
            return _hmAux_Trans
        }

    init {
        recoverIntentsInfo()
        loadFilters()
        generateMyActionList(1)
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
                myActionFilterParam.getFilledFilters()
        )
        val timePrefence = ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_PERIOD_FILTER, ConstantBaseApp.PREFERENCE_HOME_ALL_TIME_OPTION)
        if( ConstantBaseApp.PREFERENCE_HOME_ALL_TIME_OPTION != timePrefence){
            _hmAux_Trans?.get(timePrefence)?.let {chipList.add(it) }
        }
        val sitePrefence = ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_SITES_FILTER, ConstantBaseApp.PREFERENCE_HOME_ALL_SITE_OPTION)
        if(ConstantBaseApp.PREFERENCE_HOME_ALL_SITE_OPTION != sitePrefence){
            val siteObjInfo = ToolBox_Inf.getSiteObjInfo(context, ToolBox_Con.getPreference_Site_Code(context))
            chipList.add(siteObjInfo?.site_desc ?: _hmAux_Trans?.get("site_desc_not_found_lbl")
            ?: "SITE_DESC_NOT_FOUND")
        }
        return chipList
    }

    override fun getActTitle(): String {
        return when(originFlow){
            ConstantBaseApp.ACT005 -> myActionFilterParam.tagFilterDesc!!
            else -> _hmAux_Trans!!["act083_title"]!!
        }
    }

    override fun updateMyActionList(userFocusFilter: Int) {
        generateMyActionList(userFocusFilter)
    }

    override fun checkScheduleFlow(item: MyActions) {
        val scheduleExec = getScheduleFromMyAction(item)
        if(scheduleExec != null) {
            when (scheduleExec.schedule_type) {
                Constant.MD_SCHEDULE_TYPE_FORM -> {
//                    processFormFlow(item, scheduleExec)
                    mView.showToast("Em Dev")
                }
                else -> {
                    if (ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET, null)) {
                        processTicketFlow(item)
                    } else {
                        mView.showMsg(
                                Act017_Main.PROFILE_MENU_TICKET_NOT_FOUND,
                                item
                        )
                    }
                }
            }
        }
    }


    private fun prepareOpenForm(item: MyActions, scheduleExec: MD_Schedule_Exec) {
//        val bundle: Bundle = getFormFlowBundle(item, scheduleExec)
//        mView.callAct011(context, bundle)
    }

    private fun getFormFlowBundle(item: MyActions, scheduleExec: MD_Schedule_Exec): Bundle {
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
        //todo tratar o custom_form_data
//        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, scheduleExec.custom_form_data.toString())
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, scheduleExec.site_code.toString())
        return bundle
    }

    override fun processTicketFlow(item: MyActions) {
        val scheduleExec = getScheduleFromMyAction(item)
        if(scheduleExec != null) {
            if (!ConstantBaseApp.SYS_STATUS_SCHEDULE.equals(scheduleExec.status)) {
                if (isScheduleStatusPossibleToOpen(scheduleExec!!)) {
                    prepareOpenTicket(item)
                } else {
                    mView.showMsg(
                            Act017_Main.MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN,
                            item
                    )
                }
            } else {
                if (isScheduleSiteDifferentThanLogged(item)) {
                    startSiteChangeFlow(item)
                } else {
                    //LUCHE - 14/01/2021
                    //Verifica se deve bloquear a execução e em caso posito, exibe msg informando do
                    // bloqueio
                    if (ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context)) {
                        mView.showMsg(Act017_Main.FREE_EXECUTION_BLOCKED, item)
                    } else {
                        mView.showMsg(
                                Act017_Main.MODULE_TICKET_EXEC_CONFIRM,
                                item
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
                    Act017_Main.MODULE_SCHEDULE_TICKET_CREATION_ERROR,
                    item
            )
        }

    }

    private fun getTicketActionFlowBundle(item: MyActions, scheduleExec: MD_Schedule_Exec, ticket_prefix: Int, ticket_code: Int, ticket_seq: Int): Bundle {
        val bundle = Bundle()
        //
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT083)

        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT017)
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_PREFIX, scheduleExec.schedule_prefix)
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_CODE, scheduleExec.schedule_code)
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_EXEC, scheduleExec.schedule_exec)
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticket_prefix)
        bundle.putInt(TK_TicketDao.TICKET_CODE, ticket_code)
        //16/03/2020 - foi convencionado que durante a criação da execução do ticket, o ticket id,
        //será o igual ao do exibido nas celulas do agendamento.
        bundle.putString(TK_TicketDao.TICKET_ID, ToolBox_Inf.getFormattedTicketSeqExec(
                item.processPk,
                ticket_prefix.toString(),
                ticket_code.toString()
        )
        )
        //bundle.putString(TK_TicketDao.TYPE_PATH, item.get(TK_TicketDao.TYPE_PATH));
        bundle.putString(TK_TicketDao.TYPE_DESC, scheduleExec.ticket_type_desc)
        bundle.putBoolean(Act070_Main.PARAM_DENIED_BY_CHECKIN, false)
        bundle.putString(Constant.ACT_SELECTED_DATE, item.plannedDate)
        bundle.putString(MD_Schedule_ExecDao.SCHEDULE_PK, item.processPk)
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
        return item.siteCode != null &&
                !item.siteCode!!.equals("null") &&
                !item.siteCode!!.equals(ToolBox_Con.getPreference_Site_Code(context))
    }

    override fun getLocalTicket(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        return ticketBundle(splippedPk[0].toInt(), splippedPk[1].toInt())
    }

    override fun getFormApBundle(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        val bundle = Bundle()
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT083)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
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
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT083)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
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

    private fun prepareOpenTicket(item: MyActions) {
        val splippedPk = item.getSplippedPk()
        val scheduleTicket = getTicketBySchedule(splippedPk.get(0).toInt(), splippedPk.get(1).toInt(), splippedPk.get(2).toInt())
        val scheduleExec = getScheduleFromMyAction(item)
        var ticket_prefix =0
        var ticket_code =0
        if (scheduleTicket!= null
                && scheduleTicket.ticket_prefix > 0
                && scheduleTicket.ticket_code > 0) {
//            mView.callAct070(getTicketFlowBundle(item))
            mView.showToast("Em Dev")
        } else {
            mView.callAct071(getTicketActionFlowBundle(item, scheduleExec!!, ticket_prefix, ticket_code, 1))
        }
    }

    override fun getTicketFlowBundle(item: MyActions): Bundle {
        val bundle = Bundle()
//        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT017)
//        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ToolBox_Inf.convertStringToInt(item.get(TK_TicketDao.TICKET_PREFIX)))
//        bundle.putInt(TK_TicketDao.TICKET_CODE, ToolBox_Inf.convertStringToInt(item.get(TK_TicketDao.TICKET_CODE)))
//        bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, ToolBox_Inf.convertStringToInt(item.get(TK_Ticket_CtrlDao.TICKET_SEQ)))
//        bundle.putString(Constant.ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF))
//        bundle.putString(MD_Schedule_ExecDao.SCHEDULE_PK, item.get(MD_Schedule_ExecDao.SCHEDULE_PK))
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

    override fun isScheduleStatusPossibleToOpen(scheduleExec: MD_Schedule_Exec): Boolean {
        //
        return (scheduleExec != null
                && !scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_CANCELLED)
                && !scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_REJECTED)
                && !scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_IGNORED)
                && !scheduleExec.status.equals(ConstantBaseApp.SYS_STATUS_NOT_EXECUTED))
    }

    override fun isScheduleFormType(myAction: MyActions): Boolean {
        val scheduleExec = getScheduleFromMyAction(myAction)

        return scheduleExec != null && scheduleExec.schedule_type.equals(scheduleExec.custom_form_type_desc)
    }

    override fun isAnyFormInProcessing(myAction: MyActions): Boolean {
        TODO("Not yet implemented")
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


    override fun hasSerialDefined(myActions: MyActions): Boolean {
        TODO("Not yet implemented")
    }

    override fun executeSerialSearch(productCode: Int?, productId: Int?, serialId: String, b: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getScheduleFormBundle(myAction: MyActions): Bundle {
        TODO("Not yet implemented")
    }

    override fun getMdSchedule(myAction: MyActions): MD_Schedule_Exec {
        TODO("Not yet implemented")
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
        return ticketBundle(ticketPrefix, ticketCode)
    }

    private fun ticketBundle(ticketPrefix: Int, ticketCode: Int): Bundle {
        val bundle = Bundle()
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT083)
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticketPrefix)
        bundle.putInt(TK_TicketDao.TICKET_CODE, ticketCode)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        return bundle
    }

    private fun recoverIntentsInfo() {
        myActionFilterParam = bundle.getSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM) as MyActionFilterParam
        originFlow = bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT005)
    }

    private fun loadFilters() {
        tagFilter = myActionFilterParam.tagFilterCode
        productCode = myActionFilterParam.productCode
        serialId = myActionFilterParam.serialId
        ticketId = myActionFilterParam.ticketId
        clientId = myActionFilterParam.clientId
        contractId = myActionFilterParam.contractId
        calendarDate = myActionFilterParam.calendarDate
        siteCode = if(ConstantBaseApp.PREFERENCE_HOME_CURRENT_SITE_OPTION == ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_SITES_FILTER, ConstantBaseApp.PREFERENCE_HOME_ALL_SITE_OPTION)){
            ToolBox_Con.getPreference_Site_Code(context)
        }else{
            null
        }
    }

    private fun generateMyActionList(tabUserFocusFilter: Int) {
        _myActionsList.clear()
        //
        _myActionsList.addAll(
                getLocalTickets(tabUserFocusFilter).map {
                    TK_Ticket.toMyActionsObj(context, it)
                }
        )
        //
        _myActionsList.addAll(
                getCachedTickets(tabUserFocusFilter).map {
                    it.toMyActionsObj(context)
                }
        )
        //
        _myActionsList.addAll(
                getSchedules(tabUserFocusFilter).map {
                    it.toMyActionsObj(context)
                }
        )
        //
        _myActionsList.addAll(
                getFormAp(tabUserFocusFilter).map {
                    it.toMyActionsObj(context)
                }
        )
        myActionsList.addAll(
                getLocalForms(tabUserFocusFilter).map {
                    if (it.hasConsistentValue(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)
                            && ConstantBaseApp.SYS_STATUS_IN_PROCESSING == it[GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS]
                    ) {
                        it[GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS] = _hmAux_Trans?.get(ConstantBaseApp.SYS_STATUS_PROCESS)
                    }
                    //
                    GE_Custom_Form_Local.toMyActionsObj(context.applicationContext as Application?, it)
                }
        )
        //
        _myActionsList.sortBy {
            it.orderBy
        }
    }

    override fun getLocalTickets(userFocus: Int): MutableList<HMAux> {
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
                        _hmAux_Trans?.get("other_steps_available_lbl")
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
        val lbl = _hmAux_Trans?.get("form_lbl") ?: "FORMULARIO"

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


}