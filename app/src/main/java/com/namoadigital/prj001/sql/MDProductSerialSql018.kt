package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.database.Specification

class MDProductSerialSql018 (
    val customerCode: Long,
    val productCode: Long,
    val serialCode: Long,
    val deviceTpCodeMain: Int

): Specification {

    override fun toSqlQuery(): String {
        val sb = StringBuilder()
        val toString = sb.append("""
                        SELECT *
                        FROM
                            ${MD_Product_SerialDao.TABLE} p
                        WHERE
                            p.customer_code = ${customerCode}
                        and p.product_code = ${productCode}
                        and p.serial_code = ${serialCode}
                        and p.device_tp_code_main = ${deviceTpCodeMain}
                        group by  c.customer_code, c.product_code, c.serial_code
            """.trimIndent()
        ).toString()
        return toString
    }
}