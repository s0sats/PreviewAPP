package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TkTicketTypeDao
import com.namoadigital.prj001.dao.TkTicketTypeOperationDao
import com.namoadigital.prj001.database.Specification

class TkTicketTypeOperationSqlTruncate: Specification {
    override fun toSqlQuery() = "DELETE FROM ${TkTicketTypeOperationDao.TABLE}"
}
