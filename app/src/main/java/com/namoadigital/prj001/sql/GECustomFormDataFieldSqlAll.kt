package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao
import com.namoadigital.prj001.database.Specification

class GECustomFormDataFieldSqlAll: Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT ge.*
              FROM ${GE_Custom_Form_Data_FieldDao.TABLE} ge  
        """.trimIndent()
    }
}