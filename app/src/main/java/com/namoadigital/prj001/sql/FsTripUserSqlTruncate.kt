package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FSTripUserDao
import com.namoadigital.prj001.database.Specification

class FsTripUserSqlTruncate: Specification {
    override fun toSqlQuery() = "DELETE FROM ${FSTripUserDao.TABLE}"
}