package com.namoadigital.prj001.sql.trip.ticket_cache

import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class FsTripSqlNextTicketCache(val userCode:String, val siteCode:Int): Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    override fun toSqlQuery() = """
        SELECT t.*
          FROM ${TkTicketCacheDao.TABLE} t
         WHERE t.${TkTicketCacheDao.KANBAN} = 1   
           AND t.${TkTicketCacheDao.OPEN_SITE_CODE} = $siteCode
           AND t.${TkTicketCacheDao.MAIN_USER} = $userCode 
           AND strftime('%Y-%m-%d', t.${TkTicketCacheDao.FORECAST_START},'$deviceGMT') > strftime('%Y-%m-%d','now', '$deviceGMT')
           AND strftime('%Y-%m-%d', t.${TkTicketCacheDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','$deviceGMT','+7 days')
           AND (t.${TkTicketCacheDao.ADDRESS} is null OR t.${TkTicketCacheDao.ADDRESS} = 0)
           AND t.${TkTicketCacheDao.TICKET_STATUS} IN ('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}')  
           AND t.${TkTicketCacheDao.KANBAN_STAGE} IN ('${TK_TicketDao.KANBAN_STAGE_EXECUTION}', '${TK_TicketDao.KANBAN_STAGE_RELEASE_FOR_EXECUTION}')
             AND NOT EXISTS(SELECT 1
                            FROM ${TK_TicketDao.TABLE} ticket
                            WHERE ticket.${TK_TicketDao.CUSTOMER_CODE} = t.${TkTicketCacheDao.CUSTOMER_CODE}
                                  and ticket.${TK_TicketDao.TICKET_PREFIX} = t.${TkTicketCacheDao.TICKET_PREFIX}
                                  and ticket.${TK_TicketDao.TICKET_CODE} = t.${TkTicketCacheDao.TICKET_CODE}
                          )  
    """.trimIndent()
}