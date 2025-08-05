package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.database.Specification

class GE_Custom_Form_Data_Field_Sql_003(
    private val customer_code: Long,
    private val custom_form_type: Int,
    private val custom_form_code: Int,
    private val custom_form_version: Int,
    private val custom_form_data: Long
) : Specification {
    override fun toSqlQuery() =
        """
            SELECT DF.*
              FROM ${GE_Custom_Form_Data_FieldDao.TABLE} DF
              JOIN ${GE_Custom_Form_Field_LocalDao.TABLE} FL
                ON FL.${GE_Custom_Form_Field_LocalDao.CUSTOMER_CODE} = DF.${GE_Custom_Form_Data_FieldDao.CUSTOMER_CODE}
               AND FL.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_TYPE} = DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_TYPE}  
               AND FL.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_CODE} = DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_CODE}  
               AND FL.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_VERSION} = DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_VERSION}  
               AND FL.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA} = DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_DATA}  
               AND FL.${GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ} = DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_SEQ}  
             WHERE DF.${GE_Custom_Form_Data_FieldDao.CUSTOMER_CODE} = $customer_code 
               AND DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_TYPE} = $custom_form_type 
               AND DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_CODE} = $custom_form_code 
               AND DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_VERSION} = $custom_form_version 
               AND DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_DATA} = $custom_form_data
               AND (DF.${GE_Custom_Form_Data_FieldDao.IS_ACTIVE} = 1
                   OR ( FL.${GE_Custom_Form_Field_LocalDao.CONDITIONAL_SEQ} IS NULL
                        AND FL.${GE_Custom_Form_Field_LocalDao.CONDITIONAL_NC} IS NULL
                      )
                   )
             ORDER BY
                   DF.${GE_Custom_Form_Data_FieldDao.CUSTOMER_CODE}, 
                   DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_TYPE}, 
                   DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_CODE}, 
                   DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_VERSION}, 
                   DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_DATA};
        """.trimIndent()

}                    