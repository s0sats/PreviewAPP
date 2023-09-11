package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_All_ProductDao
import com.namoadigital.prj001.dao.MdProductSerialTpDeviceItemHistMatDao
import com.namoadigital.prj001.database.Specification

class MdProductSerialTpDeviceItemHistMatSql001(
    val customerCode:Long,
    val productCode:Int,
    val serialCode:Int,
    val itemCheckSeq:Int,
    val seq:Int,
): Specification {
    override fun toSqlQuery(): String {
        val sb = StringBuilder()
        return sb.append(
            """
                        SELECT p.*,
                                ap.${MD_All_ProductDao.PRODUCT_DESC}
                        FROM
                            ${MdProductSerialTpDeviceItemHistMatDao.TABLE} p
                        LEFT JOIN ${MD_All_ProductDao.TABLE} ap
                            on p.${MdProductSerialTpDeviceItemHistMatDao.CUSTOMER_CODE} =  ap.${MD_All_ProductDao.CUSTOMER_CODE}
                           and p.${MdProductSerialTpDeviceItemHistMatDao.MATERIAL_CODE} =  ap.${MD_All_ProductDao.PRODUCT_CODE}
                        WHERE
                            p.customer_code = $customerCode
                        and p.product_code = $productCode
                        and p.serial_code = $serialCode
                        and p.itemCheckSeq = $itemCheckSeq
                        and p.${MdProductSerialTpDeviceItemHistMatDao.SEQ} = $seq
                        and ap.${MD_All_ProductDao.SPARE_PART} = 1
                        ORDER BY ap.${MD_All_ProductDao.PRODUCT_DESC}
            """.trimIndent()
        ).toString()
    }
}