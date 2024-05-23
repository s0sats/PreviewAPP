package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.database.Specification

class FsTripEventSqlTruncate: Specification {
    override fun toSqlQuery() = "DELETE FROM ${FSTripEventDao.TABLE}"
}