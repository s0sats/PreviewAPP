package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.util.ConstantBaseApp

class SqlAct084_004(
        private val customerCode: Long,
        private val formLbl: String,
        private val ncFilterOn: Boolean,
        private val tabDone: Int
) : Specification {
    companion object{
        const val FIELD_NC = "FIELD_NC"
    }

    private var statusFilter = getStatusFilter()
    private val ncFilter: String = getNcFilter()
    //
    private fun getStatusFilter(): String {
        return when (tabDone) {
            1 ->    """    and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS}  = '${ConstantBaseApp.SYS_STATUS_DONE}'"""
            else -> """    and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} in('${ConstantBaseApp.SYS_STATUS_NOT_EXECUTED}',
                                                                   '${ConstantBaseApp.SYS_STATUS_CANCELLED}',
                                                                   '${ConstantBaseApp.SYS_STATUS_ERROR}'                
                                                                  ) """
        }
    }

    private fun getNcFilter(): String {
        return when (ncFilterOn) {
            true -> """and instr(df.${GE_Custom_Form_Data_FieldDao.VALUE_EXTRA},'${ConstantBaseApp.CONST_NONCONFORMITY_INSTR_SEARCH}') > 0 """
            else -> ""
        }
    }

    override fun toSqlQuery(): String {
        var s = """ SELECT
                         l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE},
                         l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE},
                         l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE},
                         l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION},
                         l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA},
                         l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS},
                         d.${GE_Custom_Form_DataDao.DATE_START},
                         l.${GE_Custom_Form_LocalDao.TAG_OPERATIONAL_DESC},
                         l.${GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC},
                         l.${GE_Custom_Form_LocalDao.SERIAL_ID},                     
                         '$formLbl' ${MyActions.MY_ACTION_TYPE_FORM},
                         l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC},
                         l.${GE_Custom_Form_LocalDao.SITE_CODE},
                         l.${GE_Custom_Form_LocalDao.SITE_DESC},
                         d.${GE_Custom_Form_DataDao.SO_PREFIX},
                         d.${GE_Custom_Form_DataDao.SO_CODE},
                         d.${GE_Custom_Form_DataDao.DATE_END},                 
                         l.${GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE} ,                         
                         l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC},
                         MAX(case when instr(df.${GE_Custom_Form_Data_FieldDao.VALUE_EXTRA},'${ConstantBaseApp.CONST_NONCONFORMITY_INSTR_SEARCH}') > 0
                                 then 1
                                 else 0
                             end                    
                        ) $FIELD_NC
                         
                    FROM 
                         ${GE_Custom_Form_LocalDao.TABLE} l,
                         ${GE_Custom_Form_DataDao.TABLE} d,
                         ${GE_Custom_Form_Data_FieldDao.TABLE} df                  
                    WHERE
                         l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = d.${GE_Custom_Form_DataDao.CUSTOMER_CODE}
                         and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE} = d.${GE_Custom_Form_DataDao.CUSTOM_FORM_TYPE}
                         and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE} = d.${GE_Custom_Form_DataDao.CUSTOM_FORM_CODE}
                         and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION} = d.${GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION}
                         and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA} = d.${GE_Custom_Form_DataDao.CUSTOM_FORM_DATA}
                         --
                         and d.${GE_Custom_Form_DataDao.CUSTOMER_CODE}  = df.${GE_Custom_Form_Data_FieldDao.CUSTOMER_CODE}      
                         and d.${GE_Custom_Form_DataDao.CUSTOM_FORM_TYPE} = df.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_TYPE}   
                         and d.${GE_Custom_Form_DataDao.CUSTOM_FORM_CODE} = df.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_CODE}   
                         and d.${GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION} = df.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_VERSION} 
                         and d.${GE_Custom_Form_DataDao.CUSTOM_FORM_DATA} = df.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_DATA}   
                         --
                         and l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = $customerCode
                         and l.${GE_Custom_Form_LocalDao.SCHEDULE_PREFIX} is null
                         and l.${GE_Custom_Form_LocalDao.SCHEDULE_CODE} is null				
                         and l.${GE_Custom_Form_LocalDao.SCHEDULE_EXEC} is null				
                         and l.${GE_Custom_Form_LocalDao.TICKET_PREFIX} is null				
                         and l.${GE_Custom_Form_LocalDao.TICKET_CODE} is null			
                         and l.${GE_Custom_Form_LocalDao.TICKET_SEQ} is null				
                         and l.${GE_Custom_Form_LocalDao.TICKET_SEQ_TMP} is null		
                         and l.${GE_Custom_Form_LocalDao.STEP_CODE} is null	
                         $statusFilter
                         $ncFilter
                    GROUP BY 
                         l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE},
                         l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE},
                         l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE},
                         l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION},
                         l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA}
                         
              """.replace("'null'","null")
        return s
    }
}