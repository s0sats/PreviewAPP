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
    private var focusFilter: Int = 1
    private var calendarDate: String? = null

    var myActionsList = mutableListOf<MyActions>()
        get() {
            return _myActionsList
        }

    var hmAux_Trans: HMAux? = HMAux()
        get() {
            return _hmAux_Trans
        }

    init {
        loadFilters()
        generateMyActionList("", true)
    }

    private fun loadFilters() {
        myActionFilterParam = bundle.getSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM) as MyActionFilterParam

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
        focusFilter
    }

    private fun loadTranslation(): HMAux? {
        val transList: MutableList<String> = java.util.ArrayList()
        transList.add("act083_title")
        transList.add("tab_my_actions_lbl")
        transList.add("tab_other_actions_lbl")
        transList.add("filter_hint")
        transList.add("form_lbl")
        transList.add("IN_PROCESSING")
        //
        return ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        )
    }

    private fun generateMyActionList(mketTextFilter: String, tabFilter: Boolean) {
        _myActionsList.addAll(
                getLocalTickets(mketTextFilter, tabFilter
                ).map {
                    it.toMyActionsObj(context)
                }
        )
        //
        _myActionsList.addAll(
                getCachedTickets(mketTextFilter, tabFilter
                ).map {
                    it.toMyActionsObj(context)
                }
        )
        //
//        _myActionsList.addAll(
//                getSchedules(mketTextFilter,tabFilter
//                ).map {
//                    it.toMyActionsObj(appContext)
//                }
//        )
        //
        _myActionsList.addAll(
                getFormAp(mketTextFilter, tabFilter
                ).map {
                    it.toMyActionsObj(context)
                }
        )
        myActionsList.addAll(
                getLocalForms(mketTextFilter, tabFilter
                ).map {
                    if (it.hasConsistentValue(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)
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

    private fun getLocalTickets(mketTextFilter: String, tabFilter: Boolean): MutableList<TK_Ticket> {
        //
        return ticketDao.query(
                SqlAct083_002(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                        tagFilter,
                        null,
                        productCode,
                        serialId,
                        clientId,
                        contractId,
                        ticketId,
                        calendarDate,
                        userFocus = focusFilter
                ).toSqlQuery()
        )
    }

    private fun getCachedTickets(mketTextFilter: String, tabFilter: Boolean): MutableList<TkTicketCache> {
        return ticketCacheDao.query(
                SqlAct083_001(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                        tagFilter,
                        null,
                        productCode,
                        serialId,
                        clientId,
                        contractId,
                        ticketId,
                        calendarDate
                ).toSqlQuery()
        )
    }

    private fun getSchedules(mketTextFilter: String, tabFilter: Boolean): MutableList<MD_Schedule_Exec> {
        return scheduleDao.query(
                SqlAct083_005(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                        tagFilter,
                        productCode,
                        serialId,
                        null,
                        calendarDate
                ).toSqlQuery()
        )
    }

    private fun getFormAp(mketTextFilter: String, tabFilter: Boolean): MutableList<GE_Custom_Form_Ap> {
        var toSqlQuery = SqlAct083_003(
                context,
                ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                tagFilter,
                productCode,
                serialId,
                calendarDate
        ).toSqlQuery()
        return formApDao.query(
                toSqlQuery
        )
    }

    private fun getLocalForms(mketTextFilter: String, tabFilter: Boolean): MutableList<HMAux> {
        var lbl = _hmAux_Trans?.get("form_lbl") ?: "FORMULARIO"
        var toSqlQuery = SqlAct083_004(
                ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                tagFilter,
                productCode,
                serialId,
                calendarDate,
                lbl
        ).toSqlQuery()
        return formLocalDao.query_HM(
                toSqlQuery
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
        if(ConstantBaseApp.PREFERENCE_HOME_ALL_SITE_OPTION != timePrefence){
            //_hmAux_Trans?.get(sitePrefence)?.let {chipList.add(it) }
            chipList.add(sitePrefence)
        }
        val focusPrefence = ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_FOCUS_FILTER, ConstantBaseApp.PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION)
        if(ConstantBaseApp.PREFERENCE_HOME_ALL_ACTIONS_FILTER != focusPrefence){
            _hmAux_Trans?.get(focusPrefence)?.let {chipList.add(it) }
        }
        return chipList
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
