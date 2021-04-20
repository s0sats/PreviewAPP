package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdTagDao
import com.namoadigital.prj001.database.Specification

class MdTagSqlTruncate : Specification {

    override fun toSqlQuery(): String {
       return "DELETE FROM ${MdTagDao.TABLE}"
    }
}