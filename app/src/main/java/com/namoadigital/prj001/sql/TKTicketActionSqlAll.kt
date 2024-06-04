package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao
import com.namoadigital.prj001.database.Specification

class TKTicketActionSqlAll: Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT * 
            FROM ${TK_Ticket_ActionDao.TABLE}
            WHERE (${TK_Ticket_ActionDao.ACTION_PHOTO_LOCAL} is not null
              AND ${TK_Ticket_ActionDao.ACTION_PHOTO_LOCAL} != '')
        """.trimIndent()
    }

}