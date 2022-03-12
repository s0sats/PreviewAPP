package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceMaterialDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 30/09/2021
 * Query que seleciona todos os materiais do item
 */

class GeOsDeviceMaterialSql_003(
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
                        AND ${GeOsDeviceMaterialDao.ITEM_CHECK_SEQ} = '$itemCheckSeq'
                        AND ${GeOsDeviceMaterialDao.MATERIAL_PLANNED} = 1
                   ORDER BY
                        ${GeOsDeviceMaterialDao.MATERIAL_DESC} 
                        ${GeOsDeviceMaterialDao.CREATION_MS} 
                                                                                                                          
        """.trimMargin()
        return s
    }
}