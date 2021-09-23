package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MeMeasureTpDao
import com.namoadigital.prj001.database.Specification

class MeMeasureTpSql_001(
    private val customerCode: Long,
    private val measureTpCode: Int
): Specification {
    override fun toSqlQuery(): String {
        val s = """
                    SELECT
                        m.*
                    FROM
                        ${MeMeasureTpDao.TABLE} m
                    WHERE
                        m.${MeMeasureTpDao.CUSTOMER_CODE} = '$customerCode'
                        and m.${MeMeasureTpDao.MEASURE_TP_CODE} = '$measureTpCode'
        """.trimIndent()
        return s
    }
}