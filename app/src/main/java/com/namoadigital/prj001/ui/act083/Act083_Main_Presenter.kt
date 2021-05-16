package com.namoadigital.prj001.ui.act083

import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_Sync
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act083_Main_Presenter (private val context: Application,
                             private val mView: Act083_Main_Contract.I_View,
                             private val bundle: Bundle,
                             private val ticketDao: TK_TicketDao,
                             private val ticketCacheDao: TkTicketCacheDao,
                             private val scheduleDao: MD_Schedule_ExecDao,
                             private val formApDao: GE_Custom_Form_ApDao,
                             private val formLocalDao: GE_Custom_Form_LocalDao,
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

    override fun getLocalTicket(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        return ticketBundle(splippedPk[0].toInt(), splippedPk[1].toInt())
    }

    override fun getFormApBundle(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        val bundle = Bundle()
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

    override fun getFormBundle(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        val bundle = Bundle()
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

    override fun isScheduleStarted(myAction: MyActions): Boolean {
        TODO("Not yet implemented")
    }

    override fun isScheduleStatusPossibleToOpen(myAction: MyActions): Boolean {
        TODO("Not yet implemented")
    }

    override fun isScheduleFormType(myAction: MyActions): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAnyFormInProcessing(myAction: MyActions): Boolean {
        TODO("Not yet implemented")
    }

    override fun getScheduleTicketBundle(myAction: MyActions): Bundle {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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
                    GE_Custom_Form_Local.toMyActionsObj(context, it)
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

    private fun getSchedules(userFocus: Int): MutableList<MD_Schedule_Exec> {
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


}