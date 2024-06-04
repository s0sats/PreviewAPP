package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao
import com.namoadigital.prj001.database.Specification

class SmSoServiceExecTaskFileSqlAll  : Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT * 
            FROM ${SM_SO_Service_Exec_Task_FileDao.TABLE}
            WHERE (${SM_SO_Service_Exec_Task_FileDao.FILE_URL_LOCAL} is not null
              AND ${SM_SO_Service_Exec_Task_FileDao.FILE_URL_LOCAL} != '')
        """.trimIndent()
    }

}