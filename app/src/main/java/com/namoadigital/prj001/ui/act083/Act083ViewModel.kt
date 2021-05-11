package com.namoadigital.prj001.ui.act083

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act083ViewModel(private val appContext: Application,
                      private val ticketDao: TK_TicketDao,
                      private val ticketCacheDao: TkTicketCacheDao,
                      private val scheduleDao: MD_Schedule_ExecDao,
                      private val formApDao: GE_Custom_Form_ApDao,
                      private val formLocalDao: GE_Custom_Form_LocalDao,
                      private val mModule_Code: String,
                      private val mResource_Code: String
                      ): ViewModel()
{
        private val _myActionsList = mutableListOf<MyActions>()
        private val _hmAux_Trans : HMAux? by lazy{
            loadTranslation()
        }

        var myActionsList = mutableListOf<MyActions>()
        get() {
            return _myActionsList
        }

        var hmAux_Trans: HMAux? = HMAux()
        get() {
            return _hmAux_Trans
        }

        init{
            generateMyActionList("",true)
        }

        private fun loadTranslation(): HMAux? {
            val transList: MutableList<String> = java.util.ArrayList()
            transList.add("act083_title")
            transList.add("tab_my_actions_lbl")
            transList.add("tab_other_actions_lbl")
            transList.add("filter_hint")
            //
            return ToolBox_Inf.setLanguage(
                    appContext,
                    mModule_Code,
                    mResource_Code,
                    ToolBox_Con.getPreference_Translate_Code(appContext),
                    transList
            )
        }

        private fun generateMyActionList(mketTextFilter: String, tabFilter: Boolean){
//        _myActionsList.addAll(
//                getLocalTickets(mketTextFilter,tabFilter
//                ).map {
//                    it.toMyActionsObj(appContext)
//                }
//        )
//        //
//       _myActionsList.addAll(
//                getCachedTickets(mketTextFilter,tabFilter
//                ).map {
//                    it.toMyActionsObj(appContext)
//                }
//        )
//        //
//        _myActionsList.addAll(
//                getSchedules(mketTextFilter,tabFilter
//                ).map {
//                    it.toMyActionsObj(appContext)
//                }
//        )
//        //
//        _myActionsList.addAll(
//                getFormAp(mketTextFilter,tabFilter
//                ).map {
//                    it.toMyActionsObj(appContext)
//                }
//        )
            myActionsList.addAll(
                    getLocalForms(mketTextFilter,tabFilter
                    ).map {
                        GE_Custom_Form_Local.toMyActionsObj(appContext,it)
                    }
            )

            _myActionsList.sortBy {
                it.orderBy
            }
        }

        private fun getLocalTickets(mketTextFilter: String, tabFilter: Boolean): MutableList<TK_Ticket> {
            val userFocus = if(tabFilter){1}else{0}
            //
            return ticketDao.query(
                    SqlAct083_002(
                            appContext,
                            ToolBox_Con.getPreference_Customer_Code(appContext).toInt(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            userFocus = userFocus
                    ).toSqlQuery()
            )
        }

        private fun getCachedTickets(mketTextFilter: String, tabFilter: Boolean): MutableList<TkTicketCache> {
            return ticketCacheDao.query(
                    SqlAct083_001(
                            appContext,
                            ToolBox_Con.getPreference_Customer_Code(appContext).toInt(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    ).toSqlQuery()
            )
        }

        private fun getSchedules(mketTextFilter: String, tabFilter: Boolean): MutableList<MD_Schedule_Exec> {
            return scheduleDao.query(
                    SqlAct083_005(
                            appContext,
                            ToolBox_Con.getPreference_Customer_Code(appContext).toInt(),
                            null,
                            null,
                            null,
                            null,
                            null
                    ).toSqlQuery()
            )
        }

        private fun getFormAp(mketTextFilter: String, tabFilter: Boolean): MutableList<GE_Custom_Form_Ap> {
            var toSqlQuery = SqlAct083_003(
                    appContext,
                    ToolBox_Con.getPreference_Customer_Code(appContext).toInt(),
                    null,
                    null,
                    null,
                    null
            ).toSqlQuery()
            return formApDao.query(
                    toSqlQuery
            )
        }

        private fun getLocalForms(mketTextFilter: String, tabFilter: Boolean): MutableList<HMAux> {
            var lbl = _hmAux_Trans?.get("form_lbl") ?: "FORMULARIO"
            var toSqlQuery = SqlAct083_004(
                    ToolBox_Con.getPreference_Customer_Code(appContext).toInt(),
                    null,
                    null,
                    null,
                    null,
                    lbl
                    ).toSqlQuery()
            return formLocalDao.query_HM(
                    toSqlQuery
            )
        }
}

class Act083ViewModelFactory(
        private val appContext: Application,
        private val ticketDao: TK_TicketDao,
        private val ticketCacheDao: TkTicketCacheDao,
        private val scheduleDao: MD_Schedule_ExecDao,
        private val formApDao: GE_Custom_Form_ApDao,
        private val formLocalDao: GE_Custom_Form_LocalDao,
        private val mModule_Code: String,
        private val mResource_Code: String): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return Act083ViewModel(appContext,ticketDao, ticketCacheDao, scheduleDao, formApDao, formLocalDao,mModule_Code, mResource_Code) as T
    }
}
