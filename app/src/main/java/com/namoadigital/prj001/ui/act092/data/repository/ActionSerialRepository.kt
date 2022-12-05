package com.namoadigital.prj001.ui.act092.data.repository

import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.ui.act092.model.SerialModel

interface ActionSerialRepository {

    suspend fun getLocalTickets(ticket: SerialModel, mainUserFilter: Boolean): MutableList<HMAux>
    suspend fun getTicketCache(ticket: SerialModel, mainUserFilter: Boolean): MutableList<TkTicketCache>
    suspend fun getSchedules(ticket: SerialModel, mainUserFilter: Boolean): MutableList<MD_Schedule_Exec>
    suspend fun getFormAp(ticket: SerialModel): MutableList<GE_Custom_Form_Ap>
    suspend fun getLocalForms(ticket: SerialModel): MutableList<HMAux>
    fun getSerial(productCode: Int, serialId: String): MD_Product_Serial?
    suspend fun getProductInfo(productCode: Int): MD_Product?
    fun downloadTicket(bundle: Bundle)
    suspend fun updateSyncChecklist(syncChecklist: Sync_Checklist)
    fun unfocusAndHistorical(bundle: Bundle)
    suspend fun getUnfocusAndHistorical(productCode: Int, serialCode: Long, serialId: String): MutableList<MyActions>
    suspend fun setPreferences(model: SerialModel)
    suspend fun getPreferences(): SerialModel
    fun getScheduleFromMyAction(prefix: Int, code: Int, exec: Int): MD_Schedule_Exec?
    fun getSite(site_code: String): MD_Site?
    fun getCustomFormLocal(
        customer_code: String,
        form_type: String,
        form_code: String,
        form_version: String,
        product_code: String,
        serial_id: String
    ): GE_Custom_Form_Local?

    fun scheduleFormLocalExists(scheduleExec: MD_Schedule_Exec): GE_Custom_Form_Local?
    fun getTicketBySchedule(
        schedule_prefix: Int,
        schedule_code: Int,
        schedule_exec: Int
    ): TK_Ticket?

    fun getScheduleCtrlIFExists(
        schedulePrefix: String,
        scheduleCode: String,
        scheduleExec: String,
        ticketPrefix: String,
        ticketCode: String
    ): TK_Ticket_Ctrl?

    fun createFormLocalForSchedule(
        formLocalExists: Boolean,
        scheduleExec: MD_Schedule_Exec
    ): Boolean

    fun searchSerialWS(bundle: Bundle)
    fun getNextScheduleTicketCode(): HMAux?
    fun getOperationByCode(code: Long): MD_Operation?
    fun updateScheduleStatus(
        schedulePrefix: Int,
        scheduleCode: Int,
        scheduleExec: Int,
        status: String,
    ): Boolean

    fun updateObjReturn(tkTicket: TK_Ticket, md_site: MD_Site): Boolean
    fun scheduleIsOsForm(
        form_type: String,
        form_code: String,
        form_version: String
    ): Boolean

    fun serialHasStructure(serial: MD_Product_Serial): ArrayList<MD_Product_Serial_Tp_Device>?
}