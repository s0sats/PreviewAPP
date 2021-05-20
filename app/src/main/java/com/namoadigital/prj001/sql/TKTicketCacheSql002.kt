package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification

class TKTicketCacheSql002(
        var customerCode: Long,
        var ticketPrefix: Int,
        var ticketCode: Int
) : Specification {

    override fun toSqlQuery(): String {
        val sb = StringBuilder()
        return sb
                .append(""" DELETE FROM ${TkTicketCacheDao.TABLE} 
                            WHERE ${TkTicketCacheDao.CUSTOMER_CODE}  = $customerCode
                              and ${TkTicketCacheDao.TICKET_PREFIX}  = $ticketPrefix
                              and ${TkTicketCacheDao.TICKET_CODE}  = $ticketCode 
                        """)
                .toString()
    }
}