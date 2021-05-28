package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class SqlAct084_004(
        private val customerCode: Long,
        private val formLbl: String,
        private val ncFilterOn: Boolean,
        private val tabDone: Int
) : Specification {
    private var periodDateFilter: String = ""
    private var statusFilter: String =""

    private fun getStatusFilter() {
        statusFilter = when (tabDone) {
            1 -> """    and c.${TkTicketCacheDao.TICKET_STATUS} in('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}') """
            else -> """    and c.${TkTicketCacheDao.TICKET_STATUS}  = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'"""
        }
    }

    override fun toSqlQuery(): String {
        var s = """ SELECT
                     c.*
                    FROM
                     ${TkTicketCacheDao.TABLE} c 
                    WHERE
                     c.${TkTicketCacheDao.CUSTOMER_CODE} = $customerCode
                     $statusFilter
                     and ($formLbl is null or c.${TkTicketCacheDao.USER_FOCUS} = $formLbl)                                         
                     and NOT EXISTS(SELECT 1
                                    FROM ${TK_TicketDao.TABLE} t
                                    WHERE t.${TK_TicketDao.CUSTOMER_CODE} = c.${TkTicketCacheDao.CUSTOMER_CODE}
                                          and t.${TK_TicketDao.TICKET_PREFIX} = c.${TkTicketCacheDao.TICKET_PREFIX}
                                          and t.${TK_TicketDao.TICKET_CODE} = c.${TkTicketCacheDao.TICKET_CODE}
                                          )
              """.replace("'null'","null")
        return s
    }
}