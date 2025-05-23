package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsVgDao
import com.namoadigital.prj001.dao.MdOrderTypeDao
import com.namoadigital.prj001.database.Specification

class GeOsVg_Sql_001 (
    private val customerCode: Long,
    private val customFormType: Int,
    private val customFormCode: Int,
    private val customFormVersion: Int,
    private val customFormData: Int,
    private val productCode: Int,
    private val serialCode: Int,
    private val vgCode: Int,
): Specification {
    override fun toSqlQuery(): String {
        val s = """
                    SELECT
                        o.*
                    FROM
                        ${GeOsVgDao.TABLE_NAME} o
                    WHERE
                            o.${GeOsVgDao.CUSTOMER_CODE} = '$customerCode'
                        and o.${GeOsVgDao.CUSTOM_FORM_TYPE} = '$customFormType'
                        and o.${GeOsVgDao.CUSTOM_FORM_CODE} = '$customFormCode'
                        and o.${GeOsVgDao.CUSTOM_FORM_VERSION} = '$customFormVersion'
                        and o.${GeOsVgDao.CUSTOM_FORM_DATA} = '$customFormData'
                        and o.${GeOsVgDao.PRODUCT_CODE} = '$productCode'
                        and o.${GeOsVgDao.SERIAL_CODE} = '$serialCode'
                        and o.${GeOsVgDao.VG_CODE} = '$vgCode'
        """.trimIndent()
        return s
    }
}