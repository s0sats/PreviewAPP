package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 04/10/2021
 * Query que busca a maquina reserva localmente. A query elimina do resultado, o produto serial usado na abertura da os
 */

class Act087Sql_001(
    private val customerCode: Long,
    private val productCode: Long,
    private val serialId: String,
    private val bkpProductCode: Long,
    private val bkpSerialId: String,
    private val siteCode: Int
) : Specification {
    override fun toSqlQuery(): String {
        var s = """SELECT
                      s.*
                    FROM
                      ${MD_Product_SerialDao.TABLE} s
                    WHERE
                      s.${MD_Product_SerialDao.CUSTOMER_CODE} = $customerCode
                      and s.${MD_Product_SerialDao.PRODUCT_CODE} = $bkpProductCode
                      and s.${MD_Product_SerialDao.SERIAL_ID} like '%$bkpSerialId%'
                      and ( s.${MD_Product_SerialDao.CUSTOMER_CODE}||'.'||
                            s.${MD_Product_SerialDao.PRODUCT_CODE}||'.'||
                            s.${MD_Product_SerialDao.SERIAL_ID} != '$customerCode.$productCode.$serialId'
                           )
                    ORDER BY
                     CASE WHEN s.${MD_Product_SerialDao.SITE_CODE} = $siteCode
                          THEN 0
                          WHEN s.${MD_Product_SerialDao.SITE_CODE} is not null
                          THEN 1
                          ELSE 2
                     END ,
                     s.${MD_Product_SerialDao.PRODUCT_ID},
                     s.${MD_Product_SerialDao.SERIAL_ID}"""
            .trimMargin()

        return s
    }
}