package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao
import com.namoadigital.prj001.database.Specification

/**
 * Selecao de lista de historico do item.
 */
class MD_Product_Serial_Tp_Device_Item_Hist_Sql_003(
    private val customerCode: String,
    private val productCode: String,
    private val serialCode: String,
    private val device_tp_code: String,
    private val itemCheckCode: String,
    private val itemCheckSeq: String

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