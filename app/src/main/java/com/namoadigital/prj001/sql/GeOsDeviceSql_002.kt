package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceDao
import com.namoadigital.prj001.database.Specification

/**
 * BARRIONUEVO - 04/10/2021
 * Query que seleciona GeOsDevice pela pk
 */
class GeOsDeviceSql_002(
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
                customFormData: Int
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
                        ${GeOsDeviceDao.TABLE} o
                    WHERE
                       o.${GeOsDeviceDao.CUSTOMER_CODE} = '$customerCode'
                       AND o.${GeOsDeviceDao.CUSTOM_FORM_TYPE} = '$customFormType'
                       AND o.${GeOsDeviceDao.CUSTOM_FORM_CODE} = '$customFormCode'
                       AND o.${GeOsDeviceDao.CUSTOM_FORM_VERSION} = '$customFormVersion'
                       AND o.${GeOsDeviceDao.CUSTOM_FORM_DATA} = '$customFormData'                                 
                    """.trimIndent()
        return s
    }
}