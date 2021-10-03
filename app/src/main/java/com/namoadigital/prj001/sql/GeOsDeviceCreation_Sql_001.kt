package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_DeviceDao
import com.namoadigital.prj001.dao.MdDeviceTpDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 30/09/2021
 * Query que retorna dados para registrod a table GeOsDeviceCreation
 */
class GeOsDeviceCreation_Sql_001(
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
                        sd.${MD_Product_Serial_Tp_DeviceDao.CUSTOMER_CODE},
                        $customFormType ${GeOsDeviceDao.CUSTOM_FORM_TYPE} ,
                        $customFormCode  ${GeOsDeviceDao.CUSTOM_FORM_CODE} ,
                        $customFormVersion  ${GeOsDeviceDao.CUSTOM_FORM_VERSION} ,
                        $customFormData  ${GeOsDeviceDao.CUSTOM_FORM_DATA} ,
                        sd.${MD_Product_Serial_Tp_DeviceDao.PRODUCT_CODE} ,
                        sd.${MD_Product_Serial_Tp_DeviceDao.SERIAL_CODE},
                        d.${MdDeviceTpDao.DEVICE_TP_CODE},
                        d.${MdDeviceTpDao.DEVICE_TP_ID},
                        d.${MdDeviceTpDao.DEVICE_TP_DESC},
                        sd.${MD_Product_Serial_Tp_DeviceDao.ORDER_SEQ},
                        sd.${MD_Product_Serial_Tp_DeviceDao.TRACKING_NUMBER}                        
                    FROM
                        ${MD_Product_Serial_Tp_DeviceDao.TABLE} sd,
                        ${MdDeviceTpDao.TABLE} d
                    WHERE
                        sd.${MD_Product_Serial_Tp_DeviceDao.CUSTOMER_CODE} = d.${MdDeviceTpDao.CUSTOMER_CODE}
                        AND sd.${MD_Product_Serial_Tp_DeviceDao.DEVICE_TP_CODE} = d.${MdDeviceTpDao.DEVICE_TP_CODE}                        
                    
                        AND sd.${MD_Product_Serial_Tp_DeviceDao.CUSTOMER_CODE} = '$customerCode'                            
                        AND sd.${MD_Product_Serial_Tp_DeviceDao.PRODUCT_CODE} = '$productCode'                           
                        AND sd.${MD_Product_Serial_Tp_DeviceDao.SERIAL_CODE} = '$serialCode'  
                    ORDER BY 
                        sd.${MD_Product_Serial_Tp_DeviceDao.ORDER_SEQ}                                                                                                                                                                                                               
        """.trimMargin()
        return s
    }
}