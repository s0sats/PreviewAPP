package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao
import com.namoadigital.prj001.database.Specification


/**
 * LUCHE 30/08/2021
 * Query que seleciona o historico pela pk
 */
class MD_Product_Serial_Tp_Device_Item_Hist_Sql_001(
    private val customerCode: Long,
    private val productCode: Long,
    private val serialCode: Long,
    private val device_tp_code: Int,
    private val itemCheckCode: Int,
    private val itemCheckSeq: Int,
    private val seq: Int

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
                            AND h.${MD_Product_Serial_Tp_Device_Item_HistDao.SEQ} = '$seq'     
                    """.trimMargin()

        return query
    }
}