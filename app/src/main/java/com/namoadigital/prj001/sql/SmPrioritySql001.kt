package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SmPriorityDao
import com.namoadigital.prj001.database.Specification

class SmPrioritySql001(
    private val customerCode: Int,
    private val priorityCode: Int,
) : Specification {
    override fun toSqlQuery(): String {
        return """
                SELECT
                  t.*
                FROM
                    ${SmPriorityDao.TABLE} t
                WHERE
                    t.${SmPriorityDao.CUSTOMER_CODE} = '$customerCode'
                    AND  t.${SmPriorityDao.PRIORITY_CODE} = '$priorityCode'
                    ORDER BY ${SmPriorityDao.PRIORITY_WEIGHT} DESC
                 """.trimIndent()
    }
}