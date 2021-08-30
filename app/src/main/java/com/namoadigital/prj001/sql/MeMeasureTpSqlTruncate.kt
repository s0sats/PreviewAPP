package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MeMeasureTpDao
import com.namoadigital.prj001.database.Specification

class MeMeasureTpSqlTruncate : Specification {
    override fun toSqlQuery() = "DELETE FROM ${MeMeasureTpDao.TABLE}"
}
