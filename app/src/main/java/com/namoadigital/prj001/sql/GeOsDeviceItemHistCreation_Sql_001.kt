package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 30/09/2021
 * Query que retorna dados para registrod a table GeOsDeviceItemHist
 */

class GeOsDeviceItemHistCreation_Sql_001(
    private val customerCode: Long,
    private val customFormType: Int,
    private val customFormCode: Int,
    private val customFormVersion: Int,
    private val customFormData: Int,
    private val productCode: Int,
    private val serialCode: Int
) : Specification {
    override fun toSqlQuery(): String {
        val s = """ SELECT           
                        h.*,
                        $customFormType ${GeOsDeviceItemDao.CUSTOM_FORM_TYPE} ,
                        $customFormCode  ${GeOsDeviceItemDao.CUSTOM_FORM_CODE} ,
                        $customFormVersion  ${GeOsDeviceItemDao.CUSTOM_FORM_VERSION} ,
                        $customFormData  ${GeOsDeviceItemDao.CUSTOM_FORM_DATA}               
                    FROM
                        ${MD_Product_Serial_Tp_Device_Item_HistDao.TABLE} h
                    WHERE
                        h.${MD_Product_Serial_Tp_Device_Item_HistDao.CUSTOMER_CODE} = '$customerCode'                            
                        AND h.${MD_Product_Serial_Tp_Device_Item_HistDao.PRODUCT_CODE} = '$productCode'                           
                        AND h.${MD_Product_Serial_Tp_Device_Item_HistDao.SERIAL_CODE} = '$serialCode'                          
                    ORDER BY 
                        h.${MD_Product_Serial_Tp_Device_Item_HistDao.DEVICE_TP_CODE} ,                                                                                                                                                                                                             
                        h.${MD_Product_Serial_Tp_Device_Item_HistDao.ITEM_CHECK_CODE},                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                        h.${MD_Product_Serial_Tp_Device_Item_HistDao.SEQ},                                                                                                                                                                                                                                                                                                                                                                                                                                                   
                        h.${MD_Product_Serial_Tp_Device_Item_HistDao.ITEM_CHECK_SEQ}                                                                                                                                                                                                                                                                                                                                                                                                                                                    
        """.trimMargin()
        return s
    }
}