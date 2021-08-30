package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdItemCheckDao
import com.namoadigital.prj001.database.Specification

class MdItemCheckSqlTruncate : Specification {
    override fun toSqlQuery() = "DELETE FROM ${MdItemCheckDao.TABLE}"
}