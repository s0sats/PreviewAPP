package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.database.Specification

class GeOsDeviceItem_Sql_001(
    private val customerCode: String,
    private val customFormType: String,
    private val customFormCode: String,
    private val customFormVersion: String,
    private val customFormData: String,
    private val productCode: String,
    private val serialCode: String,
    private val deviceTpCode: String,
    private val itemCheckCode: String,
    private val itemCheckSeq: String
) : Specification {
    constructor(customerCode: Long,
                customFormType: Int,
                customFormCode: Int,
                customFormVersion: Int,
                customFormData: Int,
                productCode: Long,
                serialCode: Long,
                deviceTpCode: Int,
                itemCheckCode: Int,
                itemCheckSeq: Int,

    ):
            this(
                customerCode.toString(),
                customFormType.toString(),
                customFormCode.toString(),
                customFormVersion.toString(),
                customFormData.toString(),
                productCode.toString(),
                serialCode.toString(),
                deviceTpCode.toString(),
                itemCheckCode.toString(),
                itemCheckSeq.toString()

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
                       AND o.${GeOsDeviceItemDao.ITEM_CHECK_CODE} = '$itemCheckCode'                   
                       AND o.${GeOsDeviceItemDao.ITEM_CHECK_SEQ} = '$itemCheckSeq'                   
                    """.trimIndent()
        return s
    }
}