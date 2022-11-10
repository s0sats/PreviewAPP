package com.namoadigital.prj001.ui.act092.repository

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.GE_Custom_Form_Ap
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.TkTicketCache
import com.namoadigital.prj001.ui.act092.model.LocalTicketsModel

interface ActionSerialRepository {

    suspend fun getLocalTickets(ticket: LocalTicketsModel): MutableList<HMAux>
    suspend fun getTicketCache(ticket: LocalTicketsModel): MutableList<TkTicketCache>
    suspend fun getSchedules(ticket: LocalTicketsModel): MutableList<MD_Schedule_Exec>
    suspend fun getFormAp(ticket: LocalTicketsModel): MutableList<GE_Custom_Form_Ap>
    suspend fun getLocalForms(ticket: LocalTicketsModel): MutableList<HMAux>

}