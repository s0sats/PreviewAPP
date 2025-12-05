package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification

class MDProductSerialSql020 (
    val customerCode: Long,
    val siteCode: String,
): Specification {

    override fun toSqlQuery(): String {

        return """
                   SELECT p.*
                     FROM ${MD_Product_SerialDao.TABLE} p    
                    WHERE p.${MD_Product_SerialDao.CUSTOMER_CODE} = $customerCode
                      AND p.${MD_Product_SerialDao.HAS_ITEM_CHECK} = 1
                      AND p.${MD_Product_SerialDao.SYNC_STRUCTURE} = 0
                      AND p.${MD_Product_SerialDao.SYNC_BIG_FILE} = 1
                      AND NOT EXISTS (
                        SELECT 1
                          FROM ${TkTicketCacheDao.TABLE} c
                         WHERE c.${TkTicketCacheDao.CUSTOMER_CODE} = p.${MD_Product_SerialDao.CUSTOMER_CODE}
                           AND c.${TkTicketCacheDao.OPEN_PRODUCT_CODE} = p.${MD_Product_SerialDao.PRODUCT_CODE}
                           AND c.${TkTicketCacheDao.OPEN_SERIAL_ID} = p.${MD_Product_SerialDao.SERIAL_ID}
                           AND c.${TkTicketCacheDao.SYNC_BIG_FILE} = 1
                      )
                      ORDER BY (p.${MD_Product_SerialDao.SITE_CODE}  = $siteCode) DESC
                    LIMIT 500
            """.trimIndent()
    }
}