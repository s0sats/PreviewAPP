package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_ItemDao
import com.namoadigital.prj001.database.Specification

/**
 * Query usada para selecionar o itens de um device.
 */
class MD_Product_Serial_Tp_DeviceDao_Sql_001(
    private val customerCode: Long,
    private val productCode: Long,
    private val serialCode: Long,
    private val device_tp_code: Int
) : Specification {
    override fun toSqlQuery(): String {
        var query = """SELECT
                         i.*
                       FROM
                            ${MD_Product_Serial_Tp_Device_ItemDao.TABLE} i                
                       WHERE
                             i.${MD_Product_Serial_Tp_Device_ItemDao.CUSTOMER_CODE} = '$customerCode'  
                            AND i.${MD_Product_Serial_Tp_Device_ItemDao.PRODUCT_CODE} = '$productCode'                           
                            AND i.${MD_Product_Serial_Tp_Device_ItemDao.SERIAL_CODE} = '$serialCode'                           
                            AND i.${MD_Product_Serial_Tp_Device_ItemDao.DEVICE_TP_CODE} = '$device_tp_code'    
                    """.trimMargin()

        return query
    }
}