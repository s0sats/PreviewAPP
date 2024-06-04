package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SM_SO_Product_Event_FileDao
import com.namoadigital.prj001.database.Specification

class SmSoProductEventFileSqlAll  : Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT * 
            FROM ${SM_SO_Product_Event_FileDao.TABLE}
            WHERE (${SM_SO_Product_Event_FileDao.FILE_URL_LOCAL} is not null
              AND ${SM_SO_Product_Event_FileDao.FILE_URL_LOCAL} != '')
        """.trimIndent()
    }

}