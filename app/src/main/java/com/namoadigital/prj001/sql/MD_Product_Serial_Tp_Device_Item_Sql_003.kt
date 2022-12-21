package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_ItemDao
import com.namoadigital.prj001.dao.MdDeviceTpDao
import com.namoadigital.prj001.dao.MdItemCheckDao
import com.namoadigital.prj001.database.Specification

class MD_Product_Serial_Tp_Device_Item_Sql_003(
    private val customerCode: Long,
    private val productCode: Long,
    private val serialCode: Long

) : Specification {
    override fun toSqlQuery(): String {

        var query = """SELECT
                          tdi.customer_code,
                          tdi.product_code,
                          tdi.serial_code,
                          tdi.device_tp_code,
                          tdi.item_check_code,
                          tdi.item_check_seq,
                          tdi.item_check_status,
                          tdi.critical_item,
                          mic.device_tp_desc,
                          mdt.item_check_desc
                       FROM
                            ${MD_Product_Serial_Tp_Device_ItemDao.TABLE} tdi     
                       INNER JOIN  ${MdItemCheckDao.TABLE} mic 
                               ON  tp.${MD_Product_Serial_Tp_Device_ItemDao.CUSTOMER_CODE} = mic.${MdItemCheckDao.CUSTOMER_CODE}
                              AND  tp.${MD_Product_Serial_Tp_Device_ItemDao.ITEM_CHECK_CODE} = mic.${MdItemCheckDao.ITEM_CHECK_CODE}
                       INNER JOIN  ${MdDeviceTpDao.TABLE} mdt 
                               ON  tp.${MD_Product_Serial_Tp_Device_ItemDao.CUSTOMER_CODE} = mdt.${MdDeviceTpDao.CUSTOMER_CODE}
                              AND  tp.${MD_Product_Serial_Tp_Device_ItemDao.DEVICE_TP_CODE} = mdt.${MdDeviceTpDao.DEVICE_TP_CODE}
                       WHERE
                             i.${MD_Product_Serial_Tp_Device_ItemDao.CUSTOMER_CODE} = '$customerCode'  
                            AND i.${MD_Product_Serial_Tp_Device_ItemDao.PRODUCT_CODE} = '$productCode'                           
                            AND i.${MD_Product_Serial_Tp_Device_ItemDao.SERIAL_CODE} = '$serialCode'                                                            
                    """.trimMargin()

        return query
    }
}