package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_UnfocusAndHistoric
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class WS_UnfocusAndHistoric : IntentService("WS_UnfocusAndHistoric") {

    private val mModuleCode = Constant.APP_MODULE
    private val mResourceName = "ws_generic_resource"
    private val gson = GsonBuilder().serializeNulls().create()
    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()
        //
        try {
            val bundle = intent?.extras ?: Bundle()
            val productCode = bundle.getInt(PRODUCT_CODE,-1)
            val serialCode = bundle.getLong(SERIAL_CODE,-1)
            //
            processUnfocusHistoricAction(productCode, serialCode)

        } catch (e: Exception) {
            sb = ToolBox_Inf.wsExceptionTreatment(applicationContext, e)
            //
            ToolBox_Inf.registerException(javaClass.name, e)
            //
            ToolBox.sendBCStatus(
                applicationContext,
                ConstantBase.PD_TYPE_ERROR_1,
                sb.toString(),
                "",
                "0"
            )
        } finally {
            WBR_UnfocusAndHistoric.completeWakefulIntent(intent)
        }
    }

    private fun processUnfocusHistoricAction(productCode: Int, serialCode: Long) {
        val env = TUnfocusAndHistoricEnv(
            productCode,
            serialCode,
            Constant.PRJ001_CODE,
            Constant.PRJ001_VERSION,
            Constant.PKG_APP_TYPE_DEFAULT,
            ToolBox_Con.getPreference_Session_App(applicationContext)
        )

        ToolBox_Inf.sendBCStatus(
            applicationContext, "STATUS",
            hmAuxTrans["generic_receiving_data_msg"], "", "0"
        )
        //
        val resultado: String = ToolBox_Con.connWebService(
            Constant.WS_DOWNLOAD_NOT_FOCUS_AND_HISTORIC,
            gson.toJson(env)
        )
        //
        val rec: TUnfocusAndHistoricRec = gson.fromJson(
            resultado,
            TUnfocusAndHistoricRec::class.java
        )
        //
        if (!ToolBox_Inf.processWSCheckValidation(
                applicationContext,
                rec.validation,
                rec.error_msg,
                rec.link_url,
                1,
                1
            )
            ||
            !ToolBox_Inf.processoOthersError(
                applicationContext,
                resources.getString(R.string.generic_error_lbl),
                rec.error_msg
            )
        ) {
            return
        }
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAuxTrans["generic_processing_data"],
            "",
            "0"
        )
        //
        processReturn(rec.obj, productCode, serialCode)

    }

    private fun processReturn(rec: List<MyActionsCache>?, productCode: Int, serialCode: Long) {
        val processList =  mutableListOf<MyActionsCache>()
        rec?.forEach {
            when (it.actionType) {
                MyActions.MY_ACTION_TYPE_TICKET -> {
                    processTicket(it.processPk)
                    processList.add(it)
                }
                MyActions.MY_ACTION_TYPE_TICKET_CACHE -> {
                    removeTicketCache(it.processPk)
                    processList.add(it)
                }
                MyActions.MY_ACTION_TYPE_SCHEDULE -> {
                    val scheduleCache = processSchedule(it)
                    scheduleCache?.let{
                        processList.add(scheduleCache)
                    }?: processList.add(it)
                }
                MyActions.MY_ACTION_TYPE_FORM -> {
                    processList.add(it)
                }
            }
        }

        val file_name = ToolBox_Inf.getOtherActionFileName(
            productCode,
            serialCode
        )
        if(processList.isNullOrEmpty()) {
            ToolBox_Inf.createJsonFile(
                file_name,
                gson.toJson(rec),
                Constant.OTHER_ACTIONS_JSON_PATH
            )
        }else{
            ToolBox_Inf.createJsonFile(
                file_name,
                gson.toJson(processList),
                Constant.OTHER_ACTIONS_JSON_PATH
            )
        }

        //
        var hmAux = HMAux()
//        hmAux.put(RESULT_LIST_SIZE, listSize.toString())
        ToolBox.sendBCStatus(
            applicationContext,
            "CLOSE_ACT",
            hmAuxTrans["generic_process_finalized_msg"],
            hmAux,
            "",
            "0"
        )
        //
    }

    private fun processSchedule(actionsCache: MyActionsCache): MyActionsCache?{
        val schedulePk = actionsCache.processPk.split("|").toTypedArray()
        val customerCode = schedulePk[0].toLong()
        val schedulePrefix = schedulePk[1].toInt()
        val scheduleCode = schedulePk[2].toInt()
        val scheduleExec = schedulePk[3].toInt()
        //
        val mdScheduleExecdao = MD_Schedule_ExecDao(
            applicationContext,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(applicationContext)),
            Constant.DB_VERSION_CUSTOM
        )
        //
        val schedule = mdScheduleExecdao.getByString(
            MD_Schedule_Exec_Sql_001(
                customerCode,
                schedulePrefix,
                scheduleCode,
                scheduleExec
            ).toSqlQuery()
        )
        //
        schedule?.let{
           if(it.status == ConstantBaseApp.SYS_STATUS_DONE){
               return MyActionsCache(
                   actionsCache.actionType,
                   actionsCache.processPk,
                   actionsCache.processId,
                   ConstantBaseApp.SYS_STATUS_DONE,
                   ConstantBaseApp.SYS_STATUS_DONE,
                   actionsCache.processLeftIcon,
                   actionsCache.processRightIcon,
                   actionsCache.plannedDateStart,
                   actionsCache.plannedDateEnd,
                   schedule.tag_operational_code,
                   schedule.tag_operational_id,
                   actionsCache.tagOperationDesc,
                   actionsCache.originDescriptor,
                   actionsCache.processDesc,
                   actionsCache.internalComments,
                   actionsCache.focusStepDesc,
                   actionsCache.siteCode,
                   actionsCache.siteDesc,
                   actionsCache.zoneDesc,
                   actionsCache.doneDateStart,
                   actionsCache.plannedDateEnd,
                   actionsCache.orderBy,
                   actionsCache.data_type,
                   actionsCache.ticketOriginType,
                   actionsCache.ticketScn,
                   actionsCache.highlightItem,
                   actionsCache.periodStarted,
                   actionsCache.lateItem,
                   actionsCache.isLastSelectedItem,
                   actionsCache.mainUser,
                   actionsCache.userFocus,
                   actionsCache.hasNc,
                   null,
                   null,
                   actionsCache.ticketClassId,
                   actionsCache.ticketClassColor,
                   actionsCache.justify_item_id,
                   actionsCache.justify_item_desc,
                   actionsCache.not_executed_comments
               )
           }
        }
        //
        return null
    }

    private fun processTicket(processPk: String) {
        val ticketPk: Array<String> = processPk.split("|").toTypedArray()
        val customerCode = ticketPk[0].toLong()
        val ticketPrefix = ticketPk[1].toInt()
        val ticketCode = ticketPk[2].toInt()

        val ticketDao = TK_TicketDao(
            applicationContext,
            ToolBox_Con.customDBPath(customerCode),
            Constant.DB_VERSION_CUSTOM
        )
        //
        val ticket = ticketDao.getByString(
            TK_Ticket_Sql_001(
                customerCode,
                ticketPrefix,
                ticketCode
            ).toSqlQuery()
        )
        //
        if (ticket != null) {
//            ticket.user_focus = 0
            ticket.sync_required = 1
            ticketDao.addUpdate(ticket)
        }
    }

    private fun removeTicketCache(processPk: String) {
        val ticketPk: Array<String> = processPk.split("|").toTypedArray()
        val customerCode = ticketPk[0].toLong()
        val ticketPrefix = ticketPk[1].toInt()
        val ticketCode = ticketPk[2].toInt()
        //
        val ticketCacheDao = TkTicketCacheDao(
            applicationContext,
            ToolBox_Con.customDBPath(customerCode),
            Constant.DB_VERSION_CUSTOM
        )
        //
        ticketCacheDao.remove(
            TKTicketCacheSql002(
                customerCode,
                ticketPrefix,
                ticketCode
            ).toSqlQuery()
        )
    }

    private fun loadTranslation() : HMAux {
        val translist = listOf<String>(
            "generic_sending_data_msg",
            "generic_receiving_data_msg",
            "generic_processing_data",
            "generic_process_finalized_msg",
            "msg_no_data_returned"
        )
        //
        val mResourceCode = ToolBox_Inf.getResourceCode(
            applicationContext,
            mModuleCode,
            mResourceName
        )
        //
        return ToolBox_Inf.setLanguage(
            applicationContext,
            mModuleCode,
            mResourceCode,
            ToolBox_Con.getPreference_Translate_Code(applicationContext),
            translist
        )
    }

    companion object{
        const val PRODUCT_CODE = "PRODUCT_CODE"
        const val SERIAL_CODE = "SERIAL_CODE"
        const val RESULT_LIST_SIZE = "RESULT_LIST_SIZE"
    }
}