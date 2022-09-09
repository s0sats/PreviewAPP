package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TkTicketTypeDao
import com.namoadigital.prj001.dao.TkTicketTypeSiteDao
import com.namoadigital.prj001.database.Specification

class TkTicketTypeSiteSqlTruncate : Specification {
    override fun toSqlQuery() = "DELETE FROM ${TkTicketTypeSiteDao.TABLE}"
}
