package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SmPriorityDao
import com.namoadigital.prj001.database.Specification

class SmPrioritySql002(
    private val customerCode: Int,
) : Specification {
    override fun toSqlQuery(): String {
        return """
                SELECT
                  t.*
                FROM
                    ${SmPriorityDao.TABLE} t
                WHERE
                    t.${SmPriorityDao.CUSTOMER_CODE} = '$customerCode'
                    ORDER BY 
                        t.${SmPriorityDao.PRIORITY_WEIGHT} DESC,
                        t.${SmPriorityDao.PRIORITY_DESC} ASC
                 """.trimIndent()
    }
}