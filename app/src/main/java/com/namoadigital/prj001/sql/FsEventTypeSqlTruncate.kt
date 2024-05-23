package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FSEventTypeDao
import com.namoadigital.prj001.database.Specification

class FsEventTypeSqlTruncate: Specification {
    override fun toSqlQuery() = "DELETE FROM ${FSEventTypeDao.TABLE}"
}