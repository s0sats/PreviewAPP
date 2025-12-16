package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_ItemDao
import com.namoadigital.prj001.dao.MdItemCheckDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusModificationType


/**
 * LUCHE - 30/09/2021
 * Query que retorna dados para registrod a table GeOsDeviceItem
 * Add param value_sufix no construtor e que será replicado como retorno da select para que o campo seja setado no obj.
 */

class GeOsDeviceItemCreation_Sql_001(
    private val customerCode: Long,
    private val customFormType: Int,
    private val customFormCode: Int,
    private val customFormVersion: Int,
    private val customFormData: Int,
    private val productCode: Int,
    private val serialCode: Int,
    private val valueSufix: String?,
    private val restrictionDecimal: Int?
) : Specification {
    override fun toSqlQuery(): String {
/*
//        --                        sd.${MD_Product_Serial_Tp_Device_ItemDao.CUSTOMER_CODE},
//        --                        $customFormType ${GeOsDeviceDao.CUSTOM_FORM_TYPE} ,
//        --                        $customFormCode  ${GeOsDeviceDao.CUSTOM_FORM_CODE} ,
//        --                        $customFormVersion  ${GeOsDeviceDao.CUSTOM_FORM_VERSION} ,
//        --                        $customFormData  ${GeOsDeviceDao.CUSTOM_FORM_DATA} ,
//        --                        sd.${MD_Product_Serial_Tp_Device_ItemDao.PRODUCT_CODE} ,
//        --                        sd.${MD_Product_Serial_Tp_Device_ItemDao.SERIAL_CODE},
//        --                        sd.${MD_Product_Serial_Tp_Device_ItemDao.DEVICE_TP_CODE},
//        --                        sd.${MD_Product_Serial_Tp_Device_ItemDao.ITEM_CHECK_CODE},
//        --                        sd.${MD_Product_Serial_Tp_Device_ItemDao.ITEM_CHECK_SEQ},
//        --                        i.${MdItemCheckDao.ITEM_CHECK_ID},
//        --                        i.${MdItemCheckDao.ITEM_CHECK_DESC},
//        --                        sd.${MD_Product_Serial_Tp_Device_ItemDao.ORDER_SEQ},
*/
        val sufix = if(valueSufix == null) null else "'$valueSufix'"
        val s = """ SELECT           
                        si.*,
                        $customFormType ${GeOsDeviceItemDao.CUSTOM_FORM_TYPE} ,
                        $customFormCode  ${GeOsDeviceItemDao.CUSTOM_FORM_CODE} ,
                        $customFormVersion  ${GeOsDeviceItemDao.CUSTOM_FORM_VERSION} ,
                        $customFormData  ${GeOsDeviceItemDao.CUSTOM_FORM_DATA} ,
                        i.${MdItemCheckDao.ITEM_CHECK_ID},
                        i.${MdItemCheckDao.ITEM_CHECK_DESC},
                        i.${MdItemCheckDao.ITEM_CHECK_DESC_ALT_VG},
                        i.${MdItemCheckDao.ITEM_CHECK_GROUP_CODE},
                        $sufix ${GeOsDeviceItemDao.VALUE_SUFIX},                                 
                        $restrictionDecimal ${GeOsDeviceItemDao.RESTRICTION_DECIMAL},                                 
                        null ${GeOsDeviceItemDao.EXEC_TYPE},
                        null ${GeOsDeviceItemDao.EXEC_DATE},
                        null ${GeOsDeviceItemDao.EXEC_COMMENT},
                        null ${GeOsDeviceItemDao.EXEC_PHOTO1},
                        null ${GeOsDeviceItemDao.EXEC_PHOTO2},
                        null ${GeOsDeviceItemDao.EXEC_PHOTO3},
                        null ${GeOsDeviceItemDao.EXEC_PHOTO4},
                        null ${GeOsDeviceItemDao.STATUS_ANSWER},
                        0 ${GeOsDeviceItemDao.HAS_EXPIRED_CYCLE},
                        0 ${GeOsDeviceItemDao.HIDE_DAYS_IN_ALERT},
                        ${GeOsDeviceItemDao.PARTITIONED_EXECUTION},
                        1 ${GeOsDeviceItemDao.IS_VISIBLE},
                        '${GeOsDeviceItemStatusModificationType.ITEM.status}' ${GeOsDeviceItemDao.STATUS_MODIFICATION_TYPE},
                        i.${MdItemCheckDao.LABEL_FIXED},
                        i.${MdItemCheckDao.LABEL_ALREADY_OK}
                    FROM
                        ${MD_Product_Serial_Tp_Device_ItemDao.TABLE} si,
                        ${MdItemCheckDao.TABLE} i
                    WHERE
                        si.${MD_Product_Serial_Tp_Device_ItemDao.CUSTOMER_CODE} = i.${MdItemCheckDao.CUSTOMER_CODE}
                        AND si.${MD_Product_Serial_Tp_Device_ItemDao.ITEM_CHECK_CODE} = i.${MdItemCheckDao.ITEM_CHECK_CODE}                                                                        
                    
                        AND si.${MD_Product_Serial_Tp_Device_ItemDao.CUSTOMER_CODE} = '$customerCode'                            
                        AND si.${MD_Product_Serial_Tp_Device_ItemDao.PRODUCT_CODE} = '$productCode'                           
                        AND si.${MD_Product_Serial_Tp_Device_ItemDao.SERIAL_CODE} = '$serialCode'                          
                    ORDER BY 
                        si.${MD_Product_Serial_Tp_Device_ItemDao.DEVICE_TP_CODE} ,                                                                                                                                                                                                              
                        si.${MD_Product_Serial_Tp_Device_ItemDao.ORDER_SEQ}                                                                                                                                                                                                                                                                                                                                                                                                                                                    
        """.trimMargin()
        return s
    }
}