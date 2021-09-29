package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdOrderTypeDao
import com.namoadigital.prj001.database.Specification

class MdOrderTypeSql_002(
    private val customerCode: Long
): Specification {
    override fun toSqlQuery(): String {
        val s = """
                    SELECT
                        o.*
                    FROM
                        ${MdOrderTypeDao.TABLE} o
                    WHERE
                        o.${MdOrderTypeDao.CUSTOMER_CODE} = '$customerCode'
                    ORDER BY
                        o.${MdOrderTypeDao.ORDER_TYPE_CODE}
        """.trimIndent()
        return s
    }
}