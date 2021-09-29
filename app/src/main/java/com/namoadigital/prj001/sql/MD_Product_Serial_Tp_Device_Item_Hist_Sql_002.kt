package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao
import com.namoadigital.prj001.database.Specification


/**
 * LUCHE 29/09/2021
 * Query que seleciona tod  o historico dos itens de verificação de um serial
 */
class MD_Product_Serial_Tp_Device_Item_Hist_Sql_002(
    private val customerCode: Long,
    private val productCode: Long,
    private val serialCode: Long
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
                    """.trimMargin()

        return query
    }
}