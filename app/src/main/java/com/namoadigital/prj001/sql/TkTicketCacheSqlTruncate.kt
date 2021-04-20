package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification

class TkTicketCacheSqlTruncate :Specification {
    override fun toSqlQuery() = "DELETE FROM ${TkTicketCacheDao.TABLE}"
}