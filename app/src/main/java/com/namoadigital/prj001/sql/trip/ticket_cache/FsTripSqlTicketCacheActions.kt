package com.namoadigital.prj001.sql.trip.ticket_cache

import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class FsTripSqlTicketCacheActions(
    val userCode:String,
    val siteCode:Int,
    val isFocused:Int
): Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)

    override fun toSqlQuery() = """
        SELECT c.*
          FROM ${TkTicketCacheDao.TABLE} c
         WHERE c.${TkTicketCacheDao.KANBAN} = 1   
           AND c.${TkTicketCacheDao.OPEN_SITE_CODE} = $siteCode
           AND c.${TkTicketCacheDao.MAIN_USER} = $userCode  
           AND c.${TkTicketCacheDao.USER_FOCUS} = $isFocused  
           AND (c.${TkTicketCacheDao.ADDRESS} is null OR c.${TkTicketCacheDao.ADDRESS} = 0)
           AND c.${TkTicketCacheDao.TICKET_STATUS} IN ('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}')  
           AND c.${TkTicketCacheDao.KANBAN_STAGE} IN ('${TK_TicketDao.KANBAN_STAGE_EXECUTION}', '${TK_TicketDao.KANBAN_STAGE_RELEASE_FOR_EXECUTION}')
           
           AND NOT EXISTS(SELECT 1
                                FROM ${TK_TicketDao.TABLE} t
                                WHERE c.${TK_TicketDao.CUSTOMER_CODE} = t.${TkTicketCacheDao.CUSTOMER_CODE}
                                      and c.${TK_TicketDao.TICKET_PREFIX} = t.${TkTicketCacheDao.TICKET_PREFIX}
                                      and c.${TK_TicketDao.TICKET_CODE} = t.${TkTicketCacheDao.TICKET_CODE}
                                      )
    """.trimIndent()
}