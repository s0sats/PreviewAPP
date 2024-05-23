package com.namoadigital.prj001.sql.trip.ticket

import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class FsTripSqlPriorityTicket(val userCode:String, val siteCode:Int): Specification {
    override fun toSqlQuery() = """
        SELECT t.*
          FROM ${TK_TicketDao.TABLE} t
         WHERE t.${TK_TicketDao.KANBAN} = 1   
           AND t.${TK_TicketDao.IS_PRIORITY} = 1
           AND t.${TK_TicketDao.HAS_ADDRESS} = 0
           AND t.${TK_TicketDao.OPEN_SITE_CODE} = $siteCode
           AND t.${TK_TicketDao.MAIN_USER} = $userCode
           AND t.${TK_TicketDao.TICKET_STATUS} IN ('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}')  
           AND t.${TK_TicketDao.KANBAN_STAGE} IN ('${TK_TicketDao.KANBAN_STAGE_EXECUTION}', '${TK_TicketDao.KANBAN_STAGE_RELEASE_FOR_EXECUTION}') 
    """.trimIndent()
}