package com.namoadigital.prj001.ui.act092.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_UnfocusAndHistoric
import com.namoadigital.prj001.sql.SqlAct092_001
import com.namoadigital.prj001.sql.SqlAct092_002
import com.namoadigital.prj001.sql.SqlAct092_004
import com.namoadigital.prj001.sql.SqlAct092_005
import com.namoadigital.prj001.ui.act092.model.LocalTicketsModel
import com.namoadigital.prj001.ui.base.FactoryRepository
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class IActionSerialRepository constructor(
    private val context: Context,
    private val ticketDao: TK_TicketDao,
    private val ticketCacheDao: TkTicketCacheDao,
    private val scheduleDao: MD_Schedule_ExecDao,
    private val formApDao: GE_Custom_Form_ApDao,
    private val localFormsDao: GE_Custom_Form_LocalDao,
) : ActionSerialRepository {

    override suspend fun getLocalTickets(ticket: LocalTicketsModel): MutableList<HMAux> =
        with(ticket) {
            return ticketDao.query_HM(
                SqlAct092_002(
                    context,
                    customerCode ?: -1,
                    productCode,
                    serialId,
                    userFocus ?: 1,
                    multStepsLbl
                ).toSqlQuery()
            )
        }

    override suspend fun getTicketCache(ticket: LocalTicketsModel): MutableList<TkTicketCache> =
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

    override suspend fun getSchedules(ticket: LocalTicketsModel): MutableList<MD_Schedule_Exec> =
        with(ticket) {
            return scheduleDao.query(
                SqlAct092_005(
                    context,
                    customerCode ?: -1,
                    productCode,
                    serialId,
                    userFocus ?: 1,
                ).toSqlQuery()
            )
        }

    override suspend fun getFormAp(ticket: LocalTicketsModel): MutableList<GE_Custom_Form_Ap> =
        with(ticket) {
            return formApDao.query(
                SqlAct092_005(
                    context,
                    customerCode ?: -1,
                    productCode,
                    serialId,
                    userFocus ?: 1,
                ).toSqlQuery()
            )
        }

    override suspend fun getLocalForms(ticket: LocalTicketsModel): MutableList<HMAux> =
        with(ticket) {
            return localFormsDao.query_HM(
                SqlAct092_004(
                    customerCode ?: -1,
                    productCode,
                    serialId,
                    hmAux?.get("form_lbl") ?: "FORMULARIO",
                    userFocus ?: 1,
                ).toSqlQuery()
            )
        }

    override fun callUnfocusAndHistorical(bundle: Bundle) {
        val mIntent = Intent(context, WBR_UnfocusAndHistoric::class.java)
        mIntent.putExtras(bundle)
        context.sendBroadcast(mIntent)
    }

    override suspend fun getUnfocusAndHistorical(productCode: Int, serialCode: Long): MutableList<MyActions> {
        //
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
        return mutableListOf<MyActions>()
    }

    companion object {

        class ActionSerialRepositoryFactoryRepository(private val context: Context) :
            FactoryRepository<ActionSerialRepository>() {
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
                    )
                )

        }

    }
}