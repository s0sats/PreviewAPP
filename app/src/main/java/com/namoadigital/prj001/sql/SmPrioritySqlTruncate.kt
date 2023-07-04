package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SO_Pack_ExpressDao
import com.namoadigital.prj001.database.Specification

class SmPrioritySqlTruncate: Specification {
    override fun toSqlQuery(): String {
        val sb = StringBuilder()

        sb.append("DELETE FROM " + SO_Pack_ExpressDao.TABLE)

        return sb.toString()
    }
}