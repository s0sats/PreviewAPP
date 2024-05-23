package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.database.Specification

class FsTripDestinationSqlTruncate: Specification {
    override fun toSqlQuery() = "DELETE FROM ${FsTripDestinationDao.TABLE}"
}