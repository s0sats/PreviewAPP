package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.database.Specification

class TKTicketSqlAll : Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT * 
            FROM ${TK_TicketDao.TABLE}
            WHERE (${TK_TicketDao.OPEN_PHOTO_LOCAL} is not null
              AND ${TK_TicketDao.OPEN_PHOTO_LOCAL} != '')
               OR (${TK_TicketDao.NOT_EXECUTED_PHOTO_NAME} is not null
              AND ${TK_TicketDao.NOT_EXECUTED_PHOTO_NAME} != '')
        """.trimIndent()
    }

}