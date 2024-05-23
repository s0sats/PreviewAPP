package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import com.namoadigital.prj001.database.Specification

class FsTripPositionSqlTruncate: Specification {
    override fun toSqlQuery() = "DELETE FROM ${FsTripPositionDao.TABLE}"
}