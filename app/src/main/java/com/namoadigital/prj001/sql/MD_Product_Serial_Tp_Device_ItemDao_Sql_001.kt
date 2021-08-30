package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao
import com.namoadigital.prj001.database.Specification

/**
 * Query usada pelo MD_Product_Serial_Tp_Device_ItemDao para selecionar o historico.
 */
class MD_Product_Serial_Tp_Device_ItemDao_Sql_001(
    private val customerCode: Long,
    private val productCode: Long,
    private val serialCode: Long,
    private val device_tp_code: Int,
    private val itemCheckCode: Int,
    private val itemCheckSeq: Int
) : Specification {
    override fun toSqlQuery(): String {
        var query = """SELECT
                         h.*
                       FROM
                            ${MD_Product_Serial_Tp_Device_Item_HistDao.TABLE} h              
                       WHERE
                             h.${MD_Product_Serial_Tp_Device_Item_HistDao.CUSTOMER_CODE} = '$customerCode'  
                            AND h.${MD_Product_Serial_Tp_Device_Item_HistDao.PRODUCT_CODE} = '$productCode'                           
                            AND h.${MD_Product_Serial_Tp_Device_Item_HistDao.SERIAL_CODE} = '$serialCode'                           
                            AND h.${MD_Product_Serial_Tp_Device_Item_HistDao.DEVICE_TP_CODE} = '$device_tp_code'  
                            AND h.${MD_Product_Serial_Tp_Device_Item_HistDao.ITEM_CHECK_CODE} = '$itemCheckCode'                           
                            AND h.${MD_Product_Serial_Tp_Device_Item_HistDao.ITEM_CHECK_SEQ} = '$itemCheckSeq'                            
                    """.trimMargin()

        return query
    }
}