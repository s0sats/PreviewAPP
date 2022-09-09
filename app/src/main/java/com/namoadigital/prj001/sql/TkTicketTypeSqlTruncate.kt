package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TkTicketTypeDao
import com.namoadigital.prj001.database.Specification

class TkTicketTypeSqlTruncate : Specification {
    override fun toSqlQuery() = "DELETE FROM ${TkTicketTypeDao.TABLE}"
}
