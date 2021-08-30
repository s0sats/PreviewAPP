package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_DeviceDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 30/08/2021
 *  query que seleciona o device pela pk
 */
class MD_Product_Serial_Tp_Device_Sql_001(
    private val customerCode: Long,
    private val productCode: Long,
    private val serialCode: Long,
    private val device_tp_code: Int
) : Specification {
    override fun toSqlQuery(): String {
        var query = """SELECT
                         d.*
                       FROM
                            ${MD_Product_Serial_Tp_DeviceDao.TABLE} d                
                       WHERE
                             d.${MD_Product_Serial_Tp_DeviceDao.CUSTOMER_CODE} = '$customerCode'  
                            AND d.${MD_Product_Serial_Tp_DeviceDao.PRODUCT_CODE} = '$productCode'                           
                            AND d.${MD_Product_Serial_Tp_DeviceDao.SERIAL_CODE} = '$serialCode'                           
                            AND d.${MD_Product_Serial_Tp_DeviceDao.DEVICE_TP_CODE} = '$device_tp_code'           
                    """.trimMargin()

        return query
    }
}