package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdOrderTypeDao
import com.namoadigital.prj001.database.Specification

class MdOrderTypeSqlTruncate :Specification {
    override fun toSqlQuery() = "DELETE FROM ${MdOrderTypeDao.TABLE}"
}