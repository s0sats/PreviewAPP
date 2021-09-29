package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_DeviceDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 27/09/2021
 *  query que seleciona a lista de devices de um serial.(nome inaporpriado?)
 */
class MD_Product_Serial_Tp_Device_Sql_002(
    private val customerCode: Long,
    private val productCode: Long,
    private val serialCode: Long
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
                    """.trimMargin()

        return query
    }
}