package com.namoadigital.prj001.ui.act084

import android.app.Application
import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.*

class Act084MainPresenter(
        private val context: Context,
        private val mView: Act084Main,
        private val bundle: Bundle,
        private val mModule_Code: String,
        private val mResource_Code: String,
        private val ticketDao: TK_TicketDao,
        private val scheduleDao: MD_Schedule_ExecDao,
        private val formApDao: GE_Custom_Form_ApDao,
        private val formLocalDao: GE_Custom_Form_LocalDao
) : Act084MainContract.I_Presenter {
    private var launch : Job? = null
    private var _myActionsList = mutableListOf<MyActionsBase>()
    val myActionsList : MutableList<MyActionsBase>
        get() {
            return _myActionsList
        }
    val hmAuxTrans: HMAux? by lazy {
        loadTranslation()
    }


    private fun loadTranslation(): HMAux? {
        val transList: MutableList<String> = mutableListOf()
        transList.add("act084_title")
        transList.add("filter_hint")
        transList.add("tab_done_lbl")
        transList.add("tab_discard_lbl")
        transList.add("no_record_lbl")
        transList.add("form_lbl")
        //
        return ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        )
    }

    override fun updateMyActionList(userFocusFilter: Int, ncFilterOn: Boolean) {
        generateMyActionList(userFocusFilter,ncFilterOn)
    }

    private fun generateMyActionList(tabDone: Int, ncFilterOn: Boolean) {
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
                    getLocalTickets(tabDone).map {
                        TK_Ticket.toMyActionsObj(context, it)
                    }
            )
            //
            _myActionsList.addAll(
                    getSchedules(tabDone, ncFilterOn).map {
                        it.toMyActionsObj(context)
                    }
            )
            //
            _myActionsList.addAll(
                    getFormAp(tabDone).map {
                        it.toMyActionsObj(context)
                    }
            )

            myActionsList.addAll(
                    getLocalForms(tabDone,ncFilterOn).map {
                        if (it.hasConsistentValue(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)
                                && ConstantBaseApp.SYS_STATUS_IN_PROCESSING == it[GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS]
                        ) {
                            it[GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS] = hmAuxTrans?.get(ConstantBaseApp.SYS_STATUS_PROCESS)
                        }
                        //
                        GE_Custom_Form_Local.toMyActionsObj(context.applicationContext as Application?, it)
                    }
            )
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

    private fun getLocalTickets(tabDone: Int): MutableList<HMAux> {
        return ticketDao.query_HM(
                SqlAct084_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        tabDone
                ).toSqlQuery()
        )
    }

    private fun getSchedules(tabDone: Int, ncFilterOn: Boolean): MutableList<MD_Schedule_Exec> {
        return scheduleDao.query(
                SqlAct084_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ncFilterOn,
                        tabDone
                ).toSqlQuery()
        )
    }

    private fun getFormAp(tabDone: Int):  MutableList<GE_Custom_Form_Ap> {
        return formApDao.query(
                SqlAct084_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        tabDone
                ).toSqlQuery()
        )
    }

    private fun getLocalForms(tabDone: Int, ncFilterOn: Boolean): MutableList<HMAux> {
        val lbl = hmAuxTrans?.get("form_lbl") ?: "FORMULARIO"

        return formLocalDao.query_HM(
                SqlAct084_004(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        lbl,
                        ncFilterOn,
                        tabDone
                ).toSqlQuery()
        )
    }
}