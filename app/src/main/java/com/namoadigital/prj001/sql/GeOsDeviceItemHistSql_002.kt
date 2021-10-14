package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemHistDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 30/09/2021
 * Query que seleciona todos historico de um item
 * - Add segundo construtor com os parametros do construtor original que agora tem todos datatype como string
 */

class GeOsDeviceItemHistSql_002(
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
                itemCheckSeq: Int
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
                        AND ${GeOsDeviceItemHistDao.ITEM_CHECK_SEQ} = '$itemCheckSeq'
                    ORDER BY
                        ${GeOsDeviceItemHistDao.DEVICE_TP_CODE},
                        ${GeOsDeviceItemHistDao.ITEM_CHECK_CODE},
                        ${GeOsDeviceItemHistDao.ITEM_CHECK_SEQ},                                                                                                                                
                        ${GeOsDeviceItemHistDao.SEQ}                                                                                                                               
        """.trimMargin()
        return s
    }
}