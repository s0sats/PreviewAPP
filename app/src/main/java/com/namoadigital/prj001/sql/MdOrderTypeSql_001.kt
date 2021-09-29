package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdOrderTypeDao
import com.namoadigital.prj001.database.Specification

class MdOrderTypeSql_001(
    private val customerCode: Long,
    private val orderTypeCode: Int
): Specification {
    override fun toSqlQuery(): String {
        val s = """
                    SELECT
                        o.*
                    FROM
                        ${MdOrderTypeDao.TABLE} o
                    WHERE
                        o.${MdOrderTypeDao.CUSTOMER_CODE} = '$customerCode'
                        and o.${MdOrderTypeDao.ORDER_TYPE_CODE} = '$orderTypeCode'
        """.trimIndent()
        return s
    }
}