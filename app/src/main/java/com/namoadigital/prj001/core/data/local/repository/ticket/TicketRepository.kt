package com.namoadigital.prj001.core.data.local.repository.ticket

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.model.TK_Ticket_Form

interface TicketRepository {
    fun getTicketPriorityCntList(siteCode: Int): Int
    fun getTicketTodayCntList(siteCode: Int): Int
    fun getTicketLateCntList(siteCode: Int): Int
    fun getTicketNextList(siteCode: Int): Int
    fun getTicketActionList(siteCode: Int, isFocused: Int, multStepsLbl: String?, productCode: Int?, serialId:String?): List<HMAux>
    fun getTicket(prefix: Int, code: Int): TK_Ticket?
    fun getTicketKanban(prefix: Int, code: Int): TK_Ticket?

    fun downloadTicket(prefix: Int, code: Int)
    fun getTicketFormById(
        ticketPrefix: Int,
        ticketCode: Int,
        stepCode: Int,
        ticketSeqTmp: Int
    ): TK_Ticket_Form?
}