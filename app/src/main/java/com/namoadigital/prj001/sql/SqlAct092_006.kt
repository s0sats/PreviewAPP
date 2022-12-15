package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class SqlAct092_006(
    private val customerCode: Int,
    private var productCode: Int?,
    private var serialId: String?,
) : Specification {
    private val statusFilter: String = getStatusFilter()


    private fun getStatusFilter() :String {
        return  """    and s.${MD_Schedule_ExecDao.STATUS} in('${ConstantBaseApp.SYS_STATUS_NOT_EXECUTED}', '${ConstantBaseApp.SYS_STATUS_DONE}') """.trimMargin()

    }

    override fun toSqlQuery(): String {
        var s = """ SELECT
                        s.*
                    FROM
                        ${MD_Schedule_ExecDao.TABLE} s 
                    LEFT JOIN (SELECT 
                                    d.${GE_Custom_Form_DataDao.CUSTOMER_CODE} ,       
                                    d.${GE_Custom_Form_DataDao.CUSTOM_FORM_TYPE},   
                                    d.${GE_Custom_Form_DataDao.CUSTOM_FORM_CODE} ,  
                                    d.${GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION},
                                    d.${GE_Custom_Form_DataDao.CUSTOM_FORM_DATA} ,                                                         
                                    MAX(d.${GE_Custom_Form_DataDao.SCHEDULE_PREFIX}) ${GE_Custom_Form_DataDao.SCHEDULE_PREFIX},
                                    MAX(d.${GE_Custom_Form_DataDao.SCHEDULE_CODE}) ${GE_Custom_Form_DataDao.SCHEDULE_CODE},  
                                    MAX(d.${GE_Custom_Form_DataDao.SCHEDULE_EXEC}) ${GE_Custom_Form_DataDao.SCHEDULE_EXEC},   
                                    MAX(d.${GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS}) custom_form_status,              
                                    MAX(case when instr(df.${GE_Custom_Form_Data_FieldDao.VALUE_EXTRA},'${ConstantBaseApp.CONST_NONCONFORMITY_INSTR_SEARCH}') > 0
                                             then 1
                                             else 0
                                         end                    
                                    ) ${MD_Schedule_ExecDao.HAS_NC} 
                            FROM  
                                ${GE_Custom_Form_DataDao.TABLE} d,
                                ${GE_Custom_Form_Data_FieldDao.TABLE} df
                            WHERE                 
                                 d.${GE_Custom_Form_DataDao.CUSTOMER_CODE}          = df.${GE_Custom_Form_Data_FieldDao.CUSTOMER_CODE}      
                                 and d.${GE_Custom_Form_DataDao.CUSTOM_FORM_TYPE}   = df.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_TYPE}   
                                 and d.${GE_Custom_Form_DataDao.CUSTOM_FORM_CODE}   = df.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_CODE}   
                                 and d.${GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION} = df.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_VERSION} 
                                 and d.${GE_Custom_Form_DataDao.CUSTOM_FORM_DATA}   = df.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_DATA}  
                            GROUP BY
                                    d.${GE_Custom_Form_DataDao.CUSTOMER_CODE} ,       
                                    d.${GE_Custom_Form_DataDao.CUSTOM_FORM_TYPE},   
                                    d.${GE_Custom_Form_DataDao.CUSTOM_FORM_CODE} ,  
                                    d.${GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION},
                                    d.${GE_Custom_Form_DataDao.CUSTOM_FORM_DATA}   
                                  
                    ) d ON  d.${GE_Custom_Form_DataDao.CUSTOMER_CODE} = s.${MD_Schedule_ExecDao.CUSTOMER_CODE} 
                           and d.${GE_Custom_Form_DataDao.SCHEDULE_PREFIX}= s.${MD_Schedule_ExecDao.SCHEDULE_PREFIX}
                           and d.${GE_Custom_Form_DataDao.SCHEDULE_CODE} = s.${MD_Schedule_ExecDao.SCHEDULE_CODE} 
                           and d.${GE_Custom_Form_DataDao.SCHEDULE_EXEC} = s.${MD_Schedule_ExecDao.SCHEDULE_EXEC} 
                           and d.${GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS} = '${ConstantBaseApp.SYS_STATUS_DONE}'
                    WHERE
                     s.${MD_Schedule_ExecDao.CUSTOMER_CODE} = $customerCode   
                     and s.${MD_Schedule_ExecDao.PRODUCT_CODE} = $productCode   
                     and s.${MD_Schedule_ExecDao.SERIAL_ID} = '$serialId'   
                     $statusFilter           
                      """.replace("'null'","null")
        return s
    }
}