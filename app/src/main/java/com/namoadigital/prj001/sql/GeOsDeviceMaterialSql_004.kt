package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.database.Specification

class GeOsDeviceMaterialSql_004(
    private val customerCode: Long,
    private val productCode: Long,
    private val serialCode: Long,
    private val deviceTpCode: String,
    private val itemCheckCode: String,
    private val itemCheckSeq: String
) : Specification {
    override fun toSqlQuery(): String {

        var query = """
            SELECT
                          mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.PRODUCT_CODE},
                          mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.SERIAL_CODE},
                          mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.DEVICE_TP_CODE},
                          mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.ITEM_CHECK_CODE},
                          mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.ITEM_CHECK_SEQ},
                          mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.QTY},
                          mdp.${MD_All_ProductDao.UN},
                          mdp.${MD_All_ProductDao.PRODUCT_DESC}
                       FROM
                            ${MD_Product_Serial_Tp_Device_Item_MaterialDao.TABLE} mtp     
                       INNER JOIN  ${MD_All_ProductDao.TABLE} mdp 
                               ON  mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.CUSTOMER_CODE} = mdp.${MD_All_ProductDao.CUSTOMER_CODE}
                              AND  mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.PRODUCT_CODE} = mdp.${MD_All_ProductDao.PRODUCT_CODE}
                       WHERE
                             mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.CUSTOMER_CODE} = $customerCode  
                            AND mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.PRODUCT_CODE} = $productCode                           
                            AND mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.SERIAL_CODE} = $serialCode                                                            
                            AND mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.DEVICE_TP_CODE} = $deviceTpCode                                                            
                            AND mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.ITEM_CHECK_CODE} = $itemCheckCode                                                            
                            AND mtp.${MD_Product_Serial_Tp_Device_Item_MaterialDao.ITEM_CHECK_SEQ} = $itemCheckSeq                                                                                                                        
                    """.trimMargin()

        return query
    }
}