package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.CH_MessageDao
import com.namoadigital.prj001.database.Specification

class CHMessageSql026(): Specification {
    override fun toSqlQuery(): String {
        return """
             SELECT *
             FROM ${CH_MessageDao.TABLE }
            WHERE ${CH_MessageDao.MSG_TYPE} = "IMAGE" 
            ORDER BY ${CH_MessageDao.MESSAGE_IMAGE_LOCAL} DESC
        """.trimIndent()
    }
}