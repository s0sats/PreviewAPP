package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TkTicketOriginNcDao
import com.namoadigital.prj001.database.Specification

class TkTicketOriginNcSql002(
    private var customer_code: Long,
    private var ticket_prefix: Int,
    private var ticket_code: Int
) : Specification {
    override fun toSqlQuery(): String {
      return """
                SELECT o.*
                FROM ${TkTicketOriginNcDao.TABLE} o
                WHERE o.${TkTicketOriginNcDao.CUSTOMER_CODE} = $customer_code
                      AND o.${TkTicketOriginNcDao.TICKET_PREFIX} = $ticket_prefix
                      AND o.${TkTicketOriginNcDao.TICKET_CODE} = $ticket_code
      """.trimIndent()
    }
}