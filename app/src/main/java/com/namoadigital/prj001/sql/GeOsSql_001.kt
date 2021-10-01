package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 30/09/2021
 * Query que seleciona GeOs pela pk
 */

class GeOsSql_001(
    private val customerCode: String,
    private val customFormType: String,
    private val customFormCode: String,
    private val customFormVersion: String,
    private val customFormData: String
) :Specification {
    constructor(customerCode: Long,
                customFormType: Int,
                customFormCode: Int,
                customFormVersion: Int,
                customFormData: Int):
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
                        ${GeOsDao.TABLE} o
                    WHERE
                       o.${GeOsDao.CUSTOMER_CODE} = '$customerCode'
                       AND o.${GeOsDao.CUSTOM_FORM_TYPE} = '$customFormType'
                       AND o.${GeOsDao.CUSTOM_FORM_CODE} = '$customFormCode'
                       AND o.${GeOsDao.CUSTOM_FORM_VERSION} = '$customFormVersion'
                       AND o.${GeOsDao.CUSTOM_FORM_DATA} = '$customFormData'                   
                    """.trimIndent()
        return s
    }
}