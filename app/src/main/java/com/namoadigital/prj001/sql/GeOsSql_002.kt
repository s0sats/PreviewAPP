package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

/**
 * LUCHE - 30/09/2021
 * Query que verifica se existe o GeOs e GE_Custom_Form_Data
 */

class GeOsSql_002(
    private val customerCode: String,
    private val customFormType: String,
    private val customFormCode: String,
    private val customFormVersion: String
) :Specification {
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
                       AND EXISTS (SELECT 1
                                   FROM ${GE_Custom_Form_DataDao.TABLE} d
                                   WHERE d.${GE_Custom_Form_DataDao.CUSTOMER_CODE} = o.${GeOsDao.CUSTOMER_CODE}
                                         AND d.${GE_Custom_Form_DataDao.CUSTOM_FORM_TYPE} = o.${GeOsDao.CUSTOM_FORM_TYPE}
                                         AND d.${GE_Custom_Form_DataDao.CUSTOM_FORM_CODE} = o.${GeOsDao.CUSTOM_FORM_CODE}
                                         AND d.${GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION} = o.${GeOsDao.CUSTOM_FORM_VERSION}
                                         AND d.${GE_Custom_Form_DataDao.CUSTOM_FORM_DATA} = o.${GeOsDao.CUSTOM_FORM_DATA}
                                         AND d.${GE_Custom_Form_DataDao.DATE_START} = o.${GeOsDao.DATE_START}                                                                                               
                                         AND d.${GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS} IN ('${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}',
                                                                                                '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                                                                                                )                                                                                               
                                   )                  
                    """.trimIndent()
        return s
    }
}