package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceMaterialDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 30/09/2021
 * Query que seleciona Material  pela pk
 */

class GeOsDeviceMaterialSql_001(
    private val customerCode: Long,
    private val customFormType: Int,
    private val customFormCode: Int,
    private val customFormVersion: Int,
    private val customFormData: Int,
    private val productCode: Int,
    private val serialCode: Int,
    private val deviceTpCode: Int,
    private val itemCheckCode: Int,
    private val itemCheckSeq: Int,
    private val materialCode: Int,
) : Specification {
    override fun toSqlQuery(): String {
        val s = """ SELECT
                        m.*
                    FROM
                        ${GeOsDeviceMaterialDao.TABLE} m
                    WHERE
                        ${GeOsDeviceMaterialDao.CUSTOMER_CODE} = '$customerCode'  
                        AND ${GeOsDeviceMaterialDao.CUSTOM_FORM_TYPE} = '$customFormType'                           
                        AND ${GeOsDeviceMaterialDao.CUSTOM_FORM_CODE} = '$customFormCode'                           
                        AND ${GeOsDeviceMaterialDao.CUSTOM_FORM_VERSION} = '$customFormVersion'                           
                        AND ${GeOsDeviceMaterialDao.CUSTOM_FORM_DATA} = '$customFormData'                           
                        AND ${GeOsDeviceMaterialDao.PRODUCT_CODE} = '$productCode'                           
                        AND ${GeOsDeviceMaterialDao.SERIAL_CODE} = '$serialCode'                           
                        AND ${GeOsDeviceMaterialDao.DEVICE_TP_CODE} = '$deviceTpCode'                           
                        AND ${GeOsDeviceMaterialDao.ITEM_CHECK_CODE} = '$itemCheckCode'                           
                        AND ${GeOsDeviceMaterialDao.ITEM_CHECK_SEQ} = '$itemCheckSeq',
                        AND ${GeOsDeviceMaterialDao.MATERIAL_CODE} = '$materialCode'                                                                                                                                           
        """.trimMargin()
        return s
    }
}