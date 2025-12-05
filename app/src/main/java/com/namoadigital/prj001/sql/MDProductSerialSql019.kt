package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.database.Specification

class MDProductSerialSql019 (
    val customerCode: Long,
): Specification {

    override fun toSqlQuery(): String {

        return """
                       SELECT *
                         FROM  ${MD_Product_SerialDao.TABLE} p
                        WHERE p.${MD_Product_SerialDao.CUSTOMER_CODE} = $customerCode
                          AND p.${MD_Product_SerialDao.HAS_ITEM_CHECK} = 1
                          AND p.${MD_Product_SerialDao.SYNC_STRUCTURE} = 1
                        LIMIT 500
            """.trimIndent()
    }
}