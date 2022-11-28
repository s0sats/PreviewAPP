package com.namoadigital.prj001.ui.act092.data.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_Serial_Search
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download
import com.namoadigital.prj001.receiver.WBR_UnfocusAndHistoric
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.ui.act092.data.local.preferences.FilterParamPreferences
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.base.NamoaFactory
import com.namoadigital.prj001.util.*
import java.io.File

class IActionSerialRepository constructor(
    private val context: Context,
    private val ticketDao: TK_TicketDao,
    private val ticketCacheDao: TkTicketCacheDao,
    private val scheduleDao: MD_Schedule_ExecDao,
    private val formApDao: GE_Custom_Form_ApDao,
    private val localFormsDao: GE_Custom_Form_LocalDao,
    private val serialDao: MD_Product_SerialDao,
    private val productDao: MD_ProductDao,
    private val syncChecklistdao: Sync_ChecklistDao,
    private val siteDao: MD_SiteDao,
    private val ticketCtrlDao: TK_Ticket_CtrlDao,
    private val filterParamPreferences: FilterParamPreferences
) : ActionSerialRepository {

    override suspend fun getLocalTickets(ticket: SerialModel): MutableList<HMAux> =
        with(ticket) {
            return ticketDao.query_HM(
                SqlAct092_002(
                    context,
                    customerCode ?: -1,
                    productCode,
                    serialId,
                    userFocus,
                    multStepsLbl
                ).toSqlQuery()
            )
        }

    override suspend fun getTicketCache(ticket: SerialModel): MutableList<TkTicketCache> =
        with(ticket) {
            return ticketCacheDao.query(
                SqlAct092_001(
                    context,
                    customerCode ?: -1,
                    productCode,
                    serialId,
                    multStepsLbl
                ).toSqlQuery()
            )
        }

    override suspend fun getSchedules(ticket: SerialModel): MutableList<MD_Schedule_Exec> =
        with(ticket) {
            return scheduleDao.query(
                SqlAct092_005(
                    context,
                    customerCode ?: -1,
                    productCode,
                    serialId,
                    userFocus,
                ).toSqlQuery()
            )
        }

    override suspend fun getFormAp(ticket: SerialModel): MutableList<GE_Custom_Form_Ap> =
        with(ticket) {
            return formApDao.query(
                SqlAct092_005(
                    context,
                    customerCode ?: -1,
                    productCode,
                    serialId,
                    userFocus,
                ).toSqlQuery()
            )
        }

    override suspend fun getLocalForms(ticket: SerialModel): MutableList<HMAux> =
        with(ticket) {
            return localFormsDao.query_HM(
                SqlAct092_004(
                    customerCode ?: -1,
                    productCode,
                    serialId,
                    hmAux?.get("form_lbl") ?: "FORMULARIO",
                    userFocus,
                ).toSqlQuery()
            )
        }

    override fun getSerial(productCode: Int, serialId: String): MD_Product_Serial {
        return serialDao.getByString(
            MD_Product_Serial_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                productCode.toLong(),
                serialId
            ).toSqlQuery()
        )
    }

    override suspend fun getProductInfo(productCode: Int): MD_Product {
        return productDao.getByString(
            MD_Product_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                productCode.toLong()
            ).toSqlQuery()
        )
    }

    override fun downloadTicket(bundle: Bundle) {
        Intent(context, WBR_TK_Ticket_Download::class.java).also {
            it.putExtras(bundle)
            context.sendBroadcast(it)
        }
    }

    override suspend fun updateSyncChecklist(syncChecklist: Sync_Checklist) {
        syncChecklistdao.addUpdate(syncChecklist)
    }

    override fun unfocusAndHistorical(bundle: Bundle) {
        val mIntent = Intent(context, WBR_UnfocusAndHistoric::class.java)
        mIntent.putExtras(bundle)
        context.sendBroadcast(mIntent)
    }

    override suspend fun getUnfocusAndHistorical(productCode: Int, serialCode: Long): MutableList<MyActions> {
        //
        if(productCode>0 && serialCode>0) {
            val fileName = ToolBox_Inf.getOtherActionFileName(productCode, serialCode)
            val file = File(ConstantBaseApp.OTHER_ACTIONS_JSON_PATH, fileName)
            if (file.exists()) {
                val contents = ToolBox_Inf.getContents(file)
                val gson = GsonBuilder().serializeNulls().create()
                val rec = gson.fromJson<java.util.ArrayList<MyActionsCache>>(
                    contents,
                    object : TypeToken<java.util.ArrayList<MyActionsCache?>?>() {}.type
                )
                val myUnfocusActionList = mutableListOf<MyActions>()
                for (myActions in rec) {
                    myUnfocusActionList.add(myActions.toMyActions(context))
                }
                return myUnfocusActionList
            }
        }
        return mutableListOf()
    }

    override suspend fun setPreferences(model: SerialModel) {
        filterParamPreferences.write(model)
    }

    override suspend fun getPreferences(): SerialModel {
        return filterParamPreferences.read()
    }

    override suspend fun getScheduleFromMyAction(
        prefix: Int,
        code: Int,
        exec: Int
    ): MD_Schedule_Exec? {
        return scheduleDao.getByString(
            MD_Schedule_Exec_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                prefix,
                code,
                exec
            ).toSqlQuery()
        )
    }

    override suspend fun getSite(site_code: String): MD_Site? {
        return siteDao.getByString(
            MD_Site_Sql_003(
                ToolBox_Con.getPreference_Customer_Code(context),
                site_code
            ).toSqlQuery()
        )
    }

    override suspend fun getCustomFormLocal(
        customer_code: String,
        form_type: String,
        form_code: String,
        form_version: String,
        product_code: String,
        serial_id: String
    ): GE_Custom_Form_Local? {
        return localFormsDao.getByString(
            GE_Custom_Form_Local_Sql_003(
                customer_code,
                form_type,
                form_code,
                form_version,
                "0",
                product_code,
                serial_id
            ).toSqlQuery()
        )
    }


    override fun scheduleFormLocalExists(scheduleExec: MD_Schedule_Exec): GE_Custom_Form_Local? {
        return localFormsDao.getByString(
            MD_Schedule_Exec_Sql_006(
                scheduleExec.customer_code.toString(),
                scheduleExec.schedule_prefix.toString(),
                scheduleExec.schedule_code.toString(),
                scheduleExec.schedule_exec.toString()
            ).toSqlQuery()
        )
    }


    override suspend fun getTicketBySchedule(
        schedule_prefix: Int,
        schedule_code: Int,
        schedule_exec: Int
    ): TK_Ticket? {
        return ticketDao.getByString(
            TK_Ticket_Sql_009(
                ToolBox_Con.getPreference_Customer_Code(context),
                schedule_prefix,
                schedule_code,
                schedule_exec
            ).toSqlQuery()
        )
    }

    override fun getScheduleCtrlIFExists(
        schedulePrefix: String,
        scheduleCode: String,
        scheduleExec: String,
        ticketPrefix: String,
        ticketCode: String
    ): TK_Ticket_Ctrl? {
        return ticketCtrlDao.getByString(
            Sql_Act017_005(
                ToolBox_Con.getPreference_Customer_Code(context),
                schedulePrefix,
                scheduleCode,
                scheduleExec,
                ticketPrefix,
                ticketCode
            ).toSqlQuery()
        )
    }


    override fun createFormLocalForSchedule(
        formLocalExists: Boolean,
        scheduleExec: MD_Schedule_Exec
    ): Boolean {
        val custom_formDao = GE_Custom_FormDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
        val custom_form_field_LocalDao = GE_Custom_Form_Field_LocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
        val custom_form_fieldDao = GE_Custom_Form_FieldDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
        val custom_form_blob_localDao = GE_Custom_Form_Blob_LocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
        var isOkForCreation = false


        if (formLocalExists) {
            isOkForCreation = true
        } else {
            ScheduleFormFatory().buildInitialScheduleFormLocal(
                context = context,
                scheduleExec,
                custom_formDao,
                custom_form_fieldDao,
                custom_form_field_LocalDao,
                custom_form_blob_localDao,
                localFormsDao
            )?.let {
                isOkForCreation = true
            }
        }
        return isOkForCreation
    }


    override fun searchSerialWS(bundle: Bundle) {
        Intent(context, WBR_Serial_Search::class.java).also {
            it.putExtras(bundle)
            context.sendBroadcast(it)
        }
    }

    companion object {

        class ActionSerialRepositoryFactoryRepository(private val context: Context) :
            NamoaFactory<ActionSerialRepository>() {
            override fun build(): ActionSerialRepository =
                IActionSerialRepository(
                    context,
                    TK_TicketDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    TkTicketCacheDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    MD_Schedule_ExecDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    GE_Custom_Form_ApDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    MD_Product_SerialDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    Sync_ChecklistDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    MD_SiteDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    TK_Ticket_CtrlDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    FilterParamPreferences(
                        PreferenceManager.getDefaultSharedPreferences(context)
                    ),
                )
        }

    }

}