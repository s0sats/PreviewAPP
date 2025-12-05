package com.namoadigital.prj001.extensions.dao.ticket

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.model.T_TK_Ticket_Download_PK_Env
import com.namoadigital.prj001.sql.ticket.SqlWSTKTicketBigFile001
import java.util.Objects

fun TK_TicketDao.getBigFileSyncList(customerCode: Long, siteCode:String): MutableList<T_TK_Ticket_Download_PK_Env> {
    val tickets = this.query_HM(
        SqlWSTKTicketBigFile001(
            customerCode,
            siteCode
        ).toSqlQuery()
    ) as ArrayList<HMAux>
    //
    val syncTickets = mutableListOf<T_TK_Ticket_Download_PK_Env>()
    tickets.map {
        val tTkTicketDownloadPkEnv = T_TK_Ticket_Download_PK_Env()
        tTkTicketDownloadPkEnv.customer_code = customerCode.toString()
        tTkTicketDownloadPkEnv.ticket_prefix = Objects.requireNonNull<String?>(it.get(TK_TicketDao.TICKET_PREFIX))
        tTkTicketDownloadPkEnv.ticket_code = Objects.requireNonNull<String?>(it.get(TK_TicketDao.TICKET_CODE))
        tTkTicketDownloadPkEnv.scn = Objects.requireNonNull<String?>(it.get(TK_TicketDao.SCN))
        tTkTicketDownloadPkEnv
    }
    //
    return syncTickets
}