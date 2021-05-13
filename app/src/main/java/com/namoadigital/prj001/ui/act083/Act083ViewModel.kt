package com.namoadigital.prj001.ui.act083

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act083ViewModel(private val context: Application,
                      private val bundle: Bundle,
                      private val ticketDao: TK_TicketDao,
                      private val ticketCacheDao: TkTicketCacheDao,
                      private val scheduleDao: MD_Schedule_ExecDao,
                      private val formApDao: GE_Custom_Form_ApDao,
                      private val formLocalDao: GE_Custom_Form_LocalDao,
                      private val mModule_Code: String,
                      private val mResource_Code: String
) : ViewModel() {
    private val _myActionsList = mutableListOf<MyActions>()
    private val _hmAux_Trans: HMAux? by lazy {
        loadTranslation()
    }
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

    var myActionsList = mutableListOf<MyActions>()
        get() {
            return _myActionsList
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

    private fun recoverIntentsInfo() {
        myActionFilterParam = bundle.getSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM) as MyActionFilterParam
        originFlow = bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT005)
    }

    private fun loadTranslation(): HMAux? {
        val transList: MutableList<String> = java.util.ArrayList()
        transList.add("act083_title")
        transList.add("tab_my_actions_lbl")
        transList.add("tab_other_actions_lbl")
        transList.add("filter_hint")
        transList.add("form_lbl")
        transList.add("IN_PROCESSING")
        transList.add("no_record_lbl")
        //
        return ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        )
    }

    private fun generateMyActionList(tabUserFocusFilter: Int) {
        _myActionsList.clear()
        //
        _myActionsList.addAll(
                getLocalTickets(tabUserFocusFilter).map {
                    TK_Ticket.toMyActionsObj(context,it)
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
                    if ( it.hasConsistentValue(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)
                         && ConstantBaseApp.SYS_STATUS_IN_PROCESSING == it[GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS]
                    ) {
                        it[GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS] = _hmAux_Trans?.get(ConstantBaseApp.SYS_STATUS_IN_PROCESSING)
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
                        userFocus
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

    fun getChipList() : List<String>{
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
            chipList.add(siteObjInfo?.site_desc ?: _hmAux_Trans?.get("site_desc_not_found_lbl")?:"SITE_DESC_NOT_FOUND")
        }
        return chipList
    }

    fun getActTitle(): String {
        return when(originFlow){
            ConstantBaseApp.ACT005 -> myActionFilterParam.tagFilterDesc!!
            else -> _hmAux_Trans!!["act083_title"]!!
        }
    }

    fun updateMyActionList(userFocusFilter: Int) {
        generateMyActionList(userFocusFilter)
    }

}

class Act083ViewModelFactory(
        private val appContext: Application,
        private val bundle: Bundle,
        private val ticketDao: TK_TicketDao,
        private val ticketCacheDao: TkTicketCacheDao,
        private val scheduleDao: MD_Schedule_ExecDao,
        private val formApDao: GE_Custom_Form_ApDao,
        private val formLocalDao: GE_Custom_Form_LocalDao,
        private val mModule_Code: String,
        private val mResource_Code: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return Act083ViewModel(appContext, bundle, ticketDao, ticketCacheDao, scheduleDao, formApDao, formLocalDao, mModule_Code, mResource_Code) as T
    }
}
