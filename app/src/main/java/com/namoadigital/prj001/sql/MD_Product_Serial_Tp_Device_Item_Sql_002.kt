package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_ItemDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 29/09/2021
 * Query que seleciona todos os dos itens de verificação de um serial
 */
class MD_Product_Serial_Tp_Device_Item_Sql_002(
    private val customerCode: Long,
    private val productCode: Long,
    private val serialCode: Long

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
                    """.trimMargin()

        return query
    }
}