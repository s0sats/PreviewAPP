package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - QUERY que retorna o proximo item_check_seq para criação de item manual.
 * */

class GeOsDeviceItem_Sql_005(
    private val customerCode: String,
    private val customFormType: String,
    private val customFormCode: String,
    private val customFormVersion: String,
    private val customFormData: String,
    private val productCode: String,
    private val serialCode: String,
    private val deviceTpCode: String
): Specification {

    override fun toSqlQuery(): String {
        val s = """
                    SELECT                        
                        IFNULL(MAX(i.${GeOsDeviceItemDao.ITEM_CHECK_SEQ}),0) + 1 ${GeOsDeviceItemDao.ITEM_CHECK_SEQ}
                    FROM
                        ${GeOsDeviceItemDao.TABLE} i
                    WHERE
                       i.${GeOsDeviceItemDao.CUSTOMER_CODE} = '$customerCode'
                       AND i.${GeOsDeviceItemDao.CUSTOM_FORM_TYPE} = '$customFormType'
                       AND i.${GeOsDeviceItemDao.CUSTOM_FORM_CODE} = '$customFormCode'
                       AND i.${GeOsDeviceItemDao.CUSTOM_FORM_VERSION} = '$customFormVersion'
                       AND i.${GeOsDeviceItemDao.CUSTOM_FORM_DATA} = '$customFormData'                   
                       AND i.${GeOsDeviceItemDao.PRODUCT_CODE} = '$productCode'                   
                       AND i.${GeOsDeviceItemDao.SERIAL_CODE} = '$serialCode'                   
                       AND i.${GeOsDeviceItemDao.DEVICE_TP_CODE} = '$deviceTpCode'                                    
                    """.trimIndent()
        return s
    }
}