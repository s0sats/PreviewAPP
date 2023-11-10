package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.database.Specification

class Sql_WS_Save_Device_Item_002(
    private val customerCode: String,
    private val customFormType: String,
    private val customFormCode: String,
    private val customFormVersion: String,
    private val customFormData: String
) : Specification {
    constructor(customerCode: Long,
                customFormType: Int,
                customFormCode: Int,
                customFormVersion: Int,
                customFormData: Long

    ):
            this(
                customerCode.toString(),
                customFormType.toString(),
                customFormCode.toString(),
                customFormVersion.toString(),
                customFormData.toString()

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
                       AND o.${GeOsDeviceItemDao.EXEC_TYPE} IS NOT NULL
                    ORDER BY o.${GeOsDeviceItemDao.ORDER_SEQ}   
                    """.trimIndent()
        return s
    }
}