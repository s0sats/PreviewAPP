package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_ItemDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 30/08/2021
 * query que seleciona o device_item pela pk
 */
class MD_Product_Serial_Tp_Device_Item_Sql_001(
    private val customerCode: Long,
    private val productCode: Long,
    private val serialCode: Long,
    private val device_tp_code: Int,
    private val itemCheckCode: Int,
    private val itemCheckSeq: Int

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
                            AND i.${MD_Product_Serial_Tp_Device_ItemDao.ITEM_CHECK_CODE} = '$itemCheckCode'                           
                            AND i.${MD_Product_Serial_Tp_Device_ItemDao.ITEM_CHECK_SEQ} = '$itemCheckSeq'     
                    """.trimMargin()

        return query
    }
}