package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.database.Specification

class FsTripSqlTruncate:Specification {
    override fun toSqlQuery() = "DELETE FROM ${FSTripDao.TABLE}"
}