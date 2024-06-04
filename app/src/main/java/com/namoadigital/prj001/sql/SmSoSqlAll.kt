package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.database.Specification

class SmSoSqlAll : Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT * 
            FROM ${SM_SODao.TABLE}
            WHERE (${SM_SODao.CLIENT_APPROVAL_IMAGE_URL_LOCAL} is not null
              AND ${SM_SODao.CLIENT_APPROVAL_IMAGE_URL_LOCAL} != '')
        """.trimIndent()
    }

}