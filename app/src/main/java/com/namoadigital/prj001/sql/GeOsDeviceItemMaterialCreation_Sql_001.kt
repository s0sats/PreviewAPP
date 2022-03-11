package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.GeOsDeviceMaterialDao
import com.namoadigital.prj001.dao.MD_All_ProductDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_MaterialDao
import com.namoadigital.prj001.database.Specification

/**
 * LUCHE - 11/03/2022
 * Query que retorna insumos planejados
 */

class GeOsDeviceItemMaterialCreation_Sql_001(
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
                        m.*,
                        $customFormType ${GeOsDeviceItemDao.CUSTOM_FORM_TYPE} ,
                        $customFormCode  ${GeOsDeviceItemDao.CUSTOM_FORM_CODE} ,
                        $customFormVersion  ${GeOsDeviceItemDao.CUSTOM_FORM_VERSION} ,
                        $customFormData  ${GeOsDeviceItemDao.CUSTOM_FORM_DATA} ,
                        IFNULL(p.${MD_All_ProductDao.PRODUCT_ID},'') ${GeOsDeviceMaterialDao.MATERIAL_ID},  
                        IFNULL(p.${MD_All_ProductDao.PRODUCT_DESC},'') ${GeOsDeviceMaterialDao.MATERIAL_DESC},                          
                        0 ${GeOsDeviceMaterialDao.MATERIAL_QTY},
                        IFNULL(p.${MD_All_ProductDao.UN},'') ${GeOsDeviceMaterialDao.MATERIAL_UNIT},
                        strftime('%s','now') *1000 ${GeOsDeviceMaterialDao.CREATION_MS}, 
                        1 ${GeOsDeviceMaterialDao.MATERIAL_PLANNED},             
                        0 ${GeOsDeviceMaterialDao.MATERIAL_PLANNED_USED},             
                        m.${MD_Product_Serial_Tp_Device_Item_MaterialDao.QTY} ${GeOsDeviceMaterialDao.MATERIAL_PLANNED_QTY}             
                    FROM
                        ${MD_Product_Serial_Tp_Device_Item_MaterialDao.TABLE} m
                    LEFT JOIN                         
                        ${MD_All_ProductDao.TABLE} p ON  m.${MD_Product_Serial_Tp_Device_Item_MaterialDao.CUSTOMER_CODE} = p.${MD_All_ProductDao.CUSTOMER_CODE}                         
                                                         AND m.${MD_Product_Serial_Tp_Device_Item_MaterialDao.MATERIAL_CODE} =  p.${MD_All_ProductDao.PRODUCT_CODE}
                    WHERE                       
                        m.${MD_Product_Serial_Tp_Device_Item_MaterialDao.CUSTOMER_CODE} = '$customerCode'                            
                        AND m.${MD_Product_Serial_Tp_Device_Item_MaterialDao.PRODUCT_CODE} = '$productCode'                           
                        AND m.${MD_Product_Serial_Tp_Device_Item_MaterialDao.SERIAL_CODE} = '$serialCode'                          
                    ORDER BY 
                        m.${MD_Product_Serial_Tp_Device_Item_MaterialDao.DEVICE_TP_CODE} ,                                                                                                                                                                                                             
                        m.${MD_Product_Serial_Tp_Device_Item_MaterialDao.ITEM_CHECK_CODE},                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                        m.${MD_Product_Serial_Tp_Device_Item_MaterialDao.MATERIAL_CODE}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
        """.trimMargin()
        return s
    }
}