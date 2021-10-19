package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.database.Specification

class GeOsDeviceItem_Sql_002(
    private val customerCode: String,
    private val customFormType: String,
    private val customFormCode: String,
    private val customFormVersion: String,
    private val customFormData: String,
    private val productCode: String,
    private val serialCode: String,
    private val deviceTpCode: String
) : Specification {
    constructor(customerCode: Long,
                customFormType: Int,
                customFormCode: Int,
                customFormVersion: Int,
                customFormData: Int,
                productCode: Long,
                serialCode: Long,
                deviceTpCode: Int

                ):
            this(
                customerCode.toString(),
                customFormType.toString(),
                customFormCode.toString(),
                customFormVersion.toString(),
                customFormData.toString(),
                productCode.toString(),
                serialCode.toString(),
                deviceTpCode.toString()

            )
    override fun toSqlQuery(): String {
        val s = """
                    SELECT
                        o.*
                    FROM
                        ${GeOsDeviceItemDao.TABLE} o
                    WHERE
                       o.${GeOsDeviceItemDao.CUSTOMER_CODE} = '$customerCode'
                       AND o.${GeOsDeviceItemDao.CUSTOM_FORM_TYPE} = '$customFormType'
                       AND o.${GeOsDeviceItemDao.CUSTOM_FORM_CODE} = '$customFormCode'
                       AND o.${GeOsDeviceItemDao.CUSTOM_FORM_VERSION} = '$customFormVersion'
                       AND o.${GeOsDeviceItemDao.CUSTOM_FORM_DATA} = '$customFormData'                   
                       AND o.${GeOsDeviceItemDao.PRODUCT_CODE} = '$productCode'                   
                       AND o.${GeOsDeviceItemDao.SERIAL_CODE} = '$serialCode'                   
                       AND o.${GeOsDeviceItemDao.DEVICE_TP_CODE} = '$deviceTpCode'   
                    ORDER BY o.${GeOsDeviceItemDao.ORDER_SEQ}   
                    """.trimIndent()
        return s
    }
}