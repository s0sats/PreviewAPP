package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceDao
import com.namoadigital.prj001.database.Specification

/**
 * BARRIONUEVO - 04/10/2021
 * Query que seleciona GeOsDevice pela pk
 */
class GeOsDeviceSql_001(
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
                        ${GeOsDeviceDao.TABLE} o
                    WHERE
                       o.${GeOsDeviceDao.CUSTOMER_CODE} = '$customerCode'
                       AND o.${GeOsDeviceDao.CUSTOM_FORM_TYPE} = '$customFormType'
                       AND o.${GeOsDeviceDao.CUSTOM_FORM_CODE} = '$customFormCode'
                       AND o.${GeOsDeviceDao.CUSTOM_FORM_VERSION} = '$customFormVersion'
                       AND o.${GeOsDeviceDao.CUSTOM_FORM_DATA} = '$customFormData'                   
                       AND o.${GeOsDeviceDao.PRODUCT_CODE} = '$productCode'                   
                       AND o.${GeOsDeviceDao.SERIAL_CODE} = '$serialCode'                   
                       AND o.${GeOsDeviceDao.DEVICE_TP_CODE} = '$deviceTpCode'                   
                    """.trimIndent()
        return s
    }
}