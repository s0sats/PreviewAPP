package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class SO_Pack_Express_Local_Sql_015(private val customerCode: Long) : Specification {

    override fun toSqlQuery(): String {
        var s = """
          SELECT
            s.*
          FROM
            ${SO_Pack_Express_LocalDao.TABLE} s
          WHERE
            ${SO_Pack_Express_LocalDao.CUSTOMER_CODE} = '$customerCode'
            and ${SO_Pack_Express_LocalDao.SO_STATUS} = '${ConstantBaseApp.SYS_STATUS_PROCESS}'           
       """.trimIndent()
        //
        return s
    }
}