package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.database.Specification

/**
 * Query que lista item com fotos para serem inseidas na listagem de geFiles
 */

class GeOsDeviceItem_Sql_003(
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
            ):this(
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
                       AND ( o.${GeOsDeviceItemDao.EXEC_PHOTO1} IS NOT NULL 
                             OR o.${GeOsDeviceItemDao.EXEC_PHOTO2} IS NOT NULL
                             OR o.${GeOsDeviceItemDao.EXEC_PHOTO3} IS NOT NULL
                             OR o.${GeOsDeviceItemDao.EXEC_PHOTO4} IS NOT NULL
                           )
                    ORDER BY
                        o.${GeOsDeviceItemDao.CUSTOM_FORM_TYPE},
                        o.${GeOsDeviceItemDao.CUSTOM_FORM_CODE}, 
                        o.${GeOsDeviceItemDao.CUSTOM_FORM_VERSION},
                        o.${GeOsDeviceItemDao.CUSTOM_FORM_DATA}                                                
                    """.trimIndent()
        return s
    }
}