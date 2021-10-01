package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemHistDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 30/09/2021
 * Query que seleciona todos historico de um item
 */

class GeOsDeviceItemHistSql_002(
    private val customerCode: Long,
    private val customFormType: Int,
    private val customFormCode: Int,
    private val customFormVersion: Int,
    private val customFormData: Int,
    private val productCode: Int,
    private val serialCode: Int,
    private val deviceTpCode: Int,
    private val itemCheckCode: Int,
    private val itemCheckSeq: Int
) : Specification {
    override fun toSqlQuery(): String {
        val s = """ SELECT
                        h.*
                    FROM
                        ${GeOsDeviceItemHistDao.TABLE} h
                    WHERE
                        ${GeOsDeviceItemHistDao.CUSTOMER_CODE} = '$customerCode'  
                        AND ${GeOsDeviceItemHistDao.CUSTOM_FORM_TYPE} = '$customFormType'                           
                        AND ${GeOsDeviceItemHistDao.CUSTOM_FORM_CODE} = '$customFormCode'                           
                        AND ${GeOsDeviceItemHistDao.CUSTOM_FORM_VERSION} = '$customFormVersion'                           
                        AND ${GeOsDeviceItemHistDao.CUSTOM_FORM_DATA} = '$customFormData'                           
                        AND ${GeOsDeviceItemHistDao.PRODUCT_CODE} = '$productCode'                           
                        AND ${GeOsDeviceItemHistDao.SERIAL_CODE} = '$serialCode'                           
                        AND ${GeOsDeviceItemHistDao.DEVICE_TP_CODE} = '$deviceTpCode'                           
                        AND ${GeOsDeviceItemHistDao.ITEM_CHECK_CODE} = '$itemCheckCode'                           
                        AND ${GeOsDeviceItemHistDao.ITEM_CHECK_SEQ} = '$itemCheckSeq',
                    ORDER BY
                        ${GeOsDeviceItemHistDao.DEVICE_TP_CODE} ,
                        ${GeOsDeviceItemHistDao.ITEM_CHECK_CODE},
                        ${GeOsDeviceItemHistDao.ITEM_CHECK_SEQ},                                                                                                                                
                        ${GeOsDeviceItemHistDao.SEQ}                                                                                                                               
        """.trimMargin()
        return s
    }
}