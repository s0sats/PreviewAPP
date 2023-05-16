package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.database.Specification

class GE_Custom_Form_Field_Local_Sql_007(
    val customer_code : Long,
    val custom_form_type : Int,
    val custom_form_code : Int,
    val custom_form_version : Int,
):Specification {
    override fun toSqlQuery(): String {
        val query = """ SELECT      
    fl.${GE_Custom_Form_Field_LocalDao.CUSTOMER_CODE},      
    fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_TYPE},      
    fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_CODE},      
    fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_VERSION},      
    fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ},        
    fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE},      
    fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_SIZE},      
    fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_MASK},      
    fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_CONTENT},      
    fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_LOCAL_LINK},      
    fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_ORDER},      
    fl.${GE_Custom_Form_Field_LocalDao.PAGE},      
    fl.${GE_Custom_Form_Field_LocalDao.REQUIRED}    
 FROM  ${GE_Custom_Form_Field_LocalDao.TABLE}  fl     
 WHERE fl.${GE_Custom_Form_Field_LocalDao.CUSTOMER_CODE} = $customer_code     
   and fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_TYPE} = $custom_form_type     
   and fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_CODE} = $custom_form_code     
   and fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_VERSION} = $custom_form_version            
   and fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE} = 'PHOTO'     
   and fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_CONTENT} like '%https%'
   and fl.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_LOCAL_LINK} = ''    
     
 UNION ALL    
 SELECT     
    fl.${GE_Custom_Form_FieldDao.CUSTOMER_CODE},      
    fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_TYPE},      
    fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_CODE},      
    fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_VERSION},      
    fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_SEQ},        
    fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_DATA_TYPE},      
    fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_DATA_SIZE},      
    fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_DATA_MASK},      
    fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_DATA_CONTENT},      
    fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_LOCAL_LINK},      
    fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_ORDER},      
    fl.${GE_Custom_Form_FieldDao.PAGE},      
    fl.${GE_Custom_Form_FieldDao.REQUIRED}    
 FROM   ${GE_Custom_Form_FieldDao.TABLE}  fl     
 WHERE fl.${GE_Custom_Form_FieldDao.CUSTOMER_CODE} = $customer_code
   and fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_TYPE} = $custom_form_type      
   and fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_CODE} = $custom_form_code      
   and fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_VERSION} = $custom_form_version     
   and fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_DATA_TYPE} = 'PHOTO'     
   and fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_DATA_CONTENT} like '%https%' 
   and fl.${GE_Custom_Form_FieldDao.CUSTOM_FORM_LOCAL_LINK} = '' ;      
   """

    return query
    }
}