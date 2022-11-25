package com.namoadigital.prj001.ui.act092.data.repository

import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.ui.act092.model.SerialModel

interface ActionSerialRepository {

    suspend fun getLocalTickets(ticket: SerialModel): MutableList<HMAux>
    suspend fun getTicketCache(ticket: SerialModel): MutableList<TkTicketCache>
    suspend fun getSchedules(ticket: SerialModel): MutableList<MD_Schedule_Exec>
    suspend fun getFormAp(ticket: SerialModel): MutableList<GE_Custom_Form_Ap>
    suspend fun getLocalForms(ticket: SerialModel): MutableList<HMAux>
    suspend fun getSerial(productCode: Int, serialId: String): MD_Product_Serial?
    suspend fun getProductInfo(productCode: Int): MD_Product?
    fun downloadTicket(bundle: Bundle)
    suspend fun updateSyncChecklist(syncChecklist: Sync_Checklist)
    fun unfocusAndHistorical(bundle: Bundle)
    suspend fun getUnfocusAndHistorical(productCode: Int, serialCode: Long): MutableList<MyActions>
    suspend fun setPreferences(model: SerialModel)
    suspend fun getPreferences(): SerialModel
}