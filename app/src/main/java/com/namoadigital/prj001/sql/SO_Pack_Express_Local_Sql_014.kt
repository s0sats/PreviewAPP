package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

/**
 * LUCHE - 29/11/2021
 * Criado query busca a ultima ordem expressa feita no site e operação
 */

class SO_Pack_Express_Local_Sql_014(
    private val customerCode: Long,
    private val siteCode: String,
    private val operationCode: Long
) : Specification {

    override fun toSqlQuery(): String {
        var s = """
          SELECT
            s.*
          FROM
            ${SO_Pack_Express_LocalDao.TABLE} s
          WHERE
            ${SO_Pack_Express_LocalDao.CUSTOMER_CODE} = '$customerCode'
            AND ${SO_Pack_Express_LocalDao.SITE_CODE} = '$siteCode'  
            AND ${SO_Pack_Express_LocalDao.OPERATION_CODE} = '$operationCode'
            AND ${SO_Pack_Express_LocalDao.STATUS} = '${ConstantBaseApp.SYS_STATUS_SENT}'
          ORDER BY
            ${SO_Pack_Express_LocalDao.LOG_DATE} DESC
          LIMIT 1                                                
       """.trimIndent()
        //
        return s
    }
}