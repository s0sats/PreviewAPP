package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification

class TKTicketCacheSql001 (
        var customerCode: Long,
        var ticketPrefix: Int,
        var ticketCode: Int
) : Specification {

    override fun toSqlQuery(): String? {
        val sb = StringBuilder()
        return sb
                .append(""" SELECT tc.*
                            FROM
                            ${TkTicketCacheDao.TABLE}  tc
                            WHERE
                            tc.customer_code = '$customerCode'
                            and tc.ticket_prefix = '$ticketPrefix'
                            and tc.ticket_code = '$ticketCode' """)
                .toString()
    }
}