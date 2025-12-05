package com.namoadigital.prj001.extensions.dao.ticket_cache

import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.dao.TkTicketCacheDao.Companion.AUTOMATIC_TICKET_DOWNLOAD
import com.namoadigital.prj001.dao.TkTicketCacheDao.Companion.CUSTOMER_CODE
import com.namoadigital.prj001.dao.TkTicketCacheDao.Companion.KANBAN
import com.namoadigital.prj001.dao.TkTicketCacheDao.Companion.MAIN_USER
import com.namoadigital.prj001.dao.TkTicketCacheDao.Companion.OPEN_SITE_CODE
import com.namoadigital.prj001.dao.TkTicketCacheDao.Companion.SYNC_BIG_FILE
import com.namoadigital.prj001.dao.TkTicketCacheDao.Companion.TABLE
import com.namoadigital.prj001.dao.TkTicketCacheDao.Companion.TICKET_CODE
import com.namoadigital.prj001.dao.TkTicketCacheDao.Companion.TICKET_PREFIX
import com.namoadigital.prj001.model.T_TK_Ticket_Download_PK_Env

fun TkTicketCacheDao.getSyncBigFile(siteCode:String): MutableList<T_TK_Ticket_Download_PK_Env> {

    val query = query(
        """
            SELECT c.*
              FROM $TABLE c             
             WHERE c.${SYNC_BIG_FILE} = 1 
             ORDER BY (c.${OPEN_SITE_CODE} = $siteCode) DESC, c.${KANBAN} DESC 
            """
    )
    return query.map {
        val tTkTicketDownloadPkEnv = T_TK_Ticket_Download_PK_Env()
        tTkTicketDownloadPkEnv.customer_code = it.customer_code.toString()
        tTkTicketDownloadPkEnv.ticket_prefix = it.ticket_prefix.toString()
        tTkTicketDownloadPkEnv.ticket_code = it.ticket_code.toString()
        tTkTicketDownloadPkEnv.scn = "0"
        tTkTicketDownloadPkEnv
    } as MutableList<T_TK_Ticket_Download_PK_Env>
}

fun TkTicketCacheDao.setSyncBigFile(userCode: String) {

    addUpdate("""
            UPDATE $TABLE
               SET $SYNC_BIG_FILE = 1
            WHERE
            (
                (
                     ${KANBAN} = 1 
                 AND ${MAIN_USER} = $userCode 
                ) 
              or ${AUTOMATIC_TICKET_DOWNLOAD} = 1
            )    
            AND NOT EXISTS(
                SELECT 1
                    FROM ${TABLE} C,
                            ${TK_TicketDao.TABLE} t
                    WHERE t.${TK_TicketDao.CUSTOMER_CODE} = C.${CUSTOMER_CODE}
                          and t.${TK_TicketDao.TICKET_PREFIX} = C.${TICKET_PREFIX}
                          and t.${TK_TicketDao.TICKET_CODE} = C.${TICKET_CODE}
           )
            """)
}