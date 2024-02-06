package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.database.Specification

class MDProductSerialSql018 (
    val customerCode: Long,
): Specification {

    override fun toSqlQuery(): String {

        return """
                        SELECT *
                        FROM  ${MD_Product_SerialDao.TABLE} p
                        WHERE p.customer_code = $customerCode
                          AND p.has_item_check = 1
                          AND p.scn_item_check = 0
            """.trimIndent()
    }
}