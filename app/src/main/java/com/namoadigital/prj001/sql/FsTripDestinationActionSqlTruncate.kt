package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FsTripDestinationActionDao
import com.namoadigital.prj001.database.Specification

class FsTripDestinationActionSqlTruncate: Specification {
    override fun toSqlQuery() = "DELETE FROM ${FsTripDestinationActionDao.TABLE}"
}