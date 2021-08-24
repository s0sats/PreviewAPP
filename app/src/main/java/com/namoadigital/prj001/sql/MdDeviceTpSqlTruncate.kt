package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdDeviceTpDao
import com.namoadigital.prj001.database.Specification

class MdDeviceTpSqlTruncate :Specification {
    override fun toSqlQuery() = "DELETE FROM ${MdDeviceTpDao.TABLE}"
}