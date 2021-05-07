package com.namoadigital.prj001.ui.act083

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.util.ToolBox_Con

class Act083ViewModel(private val appContext: Application,
                      private val ticketDao: TK_TicketDao,
                      private val ticketCacheDao: TkTicketCacheDao,
                      private val scheduleDao: MD_Schedule_ExecDao,
                      private val formApDao: GE_Custom_Form_ApDao,
                      private val formLocalDao: GE_Custom_Form_LocalDao
                      ): ViewModel()
{
    private val _myActionsList = mutableListOf<MyActions>()
    var myActionsList = mutableListOf<MyActions>()
    get() {
        return _myActionsList
    }

    init{
        generateMyActionList("",true)
    }

    private fun generateMyActionList(mketTextFilter: String, tabFilter: Boolean){
        _myActionsList.addAll(
                getLocalTickets(mketTextFilter,tabFilter
                ).map {
                    it.toMyActionsObj(appContext)

                }
        )
        //
        _myActionsList.addAll(
                getCachedTickets(mketTextFilter,tabFilter
                ).map {
                    it.toMyActionsObj(appContext)
                }
        )
        //
        _myActionsList.addAll(
                getSchedules(mketTextFilter,tabFilter
                ).map {
                    it.toMyActionsObj(appContext)
                }
        )
        //
        _myActionsList.addAll(
                getFormAp(mketTextFilter,tabFilter
                ).map {
                    it.toMyActionsObj(appContext)
                }
        )
//        myActionsList.addAll(
//                getFormAp(mketTextFilter,tabFilter
//                ).map {
//                    it.toMyActionsObj(appContext)
//                }
//        )


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
        var toSqlQuery = SqlAct083_005(
                appContext,
                ToolBox_Con.getPreference_Customer_Code(appContext).toInt(),
                null,
                null,
                null,
                null,
                null
        ).toSqlQuery()
        return scheduleDao.query(
                toSqlQuery
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

    private fun getLocalForms(mketTextFilter: String, tabFilter: Boolean): MutableList<GE_Custom_Form_Local> {
        return formLocalDao.query(
                SqlAct083_004(
                        ToolBox_Con.getPreference_Customer_Code(appContext).toInt(),
                        null,
                        null,
                        null,
                        null
                ).toSqlQuery()
        )
    }

}

class Act083ViewModelFactory(
                             private val appContext: Application,
                             private val ticketDao: TK_TicketDao,
                             private val ticketCacheDao: TkTicketCacheDao,
                             private val scheduleDao: MD_Schedule_ExecDao,
                             private val formApDao: GE_Custom_Form_ApDao,
                             private val formLocalDao: GE_Custom_Form_LocalDao): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return Act083ViewModel(appContext,ticketDao, ticketCacheDao, scheduleDao, formApDao, formLocalDao) as T
    }
}
