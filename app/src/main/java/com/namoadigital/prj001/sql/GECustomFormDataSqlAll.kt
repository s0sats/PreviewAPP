package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.database.Specification

class GECustomFormDataSqlAll:Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT ge.*
              FROM ${GE_Custom_Form_DataDao.TABLE} ge
            WHERE  (${GE_Custom_Form_DataDao.SIGNATURE} is not null
              AND ${GE_Custom_Form_DataDao.SIGNATURE} != '') 
        """.trimIndent()
    }
}