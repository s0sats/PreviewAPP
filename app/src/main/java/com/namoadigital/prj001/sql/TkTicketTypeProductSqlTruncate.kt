package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TkTicketTypeDao
import com.namoadigital.prj001.dao.TkTicketTypeProductDao
import com.namoadigital.prj001.database.Specification

class TkTicketTypeProductSqlTruncate: Specification {
    override fun toSqlQuery() = "DELETE FROM ${TkTicketTypeProductDao.TABLE}"
}
