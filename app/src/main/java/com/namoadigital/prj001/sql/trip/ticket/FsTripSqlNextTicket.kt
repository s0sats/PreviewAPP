package com.namoadigital.prj001.sql.trip.ticket

import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_TicketDao.KANBAN_STAGE_EXECUTION
import com.namoadigital.prj001.dao.TK_TicketDao.KANBAN_STAGE_RELEASE_FOR_EXECUTION
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class FsTripSqlNextTicket(val userCode:String, val siteCode:Int): Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    override fun toSqlQuery() = """
        SELECT t.*
          FROM ${TK_TicketDao.TABLE} t
         WHERE t.${TK_TicketDao.KANBAN} = 1 
           AND t.${TK_TicketDao.MAIN_USER} = $userCode
           AND t.${TK_TicketDao.OPEN_SITE_CODE} = $siteCode
           and strftime('%Y-%m-%d', t.${TK_TicketDao.KANBAN_DATE},'$deviceGMT') > strftime('%Y-%m-%d','now', '$deviceGMT')
           and strftime('%Y-%m-%d', t.${TK_TicketDao.KANBAN_DATE},'$deviceGMT') <= strftime('%Y-%m-%d','now','$deviceGMT','+7 days')
           AND t.${TK_TicketDao.HAS_ADDRESS} = 0
           AND t.${TK_TicketDao.TICKET_STATUS} IN ('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}')  
           AND t.${TK_TicketDao.KANBAN_STAGE} IN ('${KANBAN_STAGE_EXECUTION}', '${KANBAN_STAGE_RELEASE_FOR_EXECUTION}')
    """.trimIndent()
}