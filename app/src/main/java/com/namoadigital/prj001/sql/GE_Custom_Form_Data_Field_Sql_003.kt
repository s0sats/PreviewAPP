package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao
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
             WHERE DF.${GE_Custom_Form_Data_FieldDao.CUSTOMER_CODE} = $customer_code 
               AND DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_TYPE} = $custom_form_type 
               AND DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_CODE} = $custom_form_code 
               AND DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_VERSION} = $custom_form_version 
               AND DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_DATA} = $custom_form_data 
             ORDER BY
                   DF.${GE_Custom_Form_Data_FieldDao.CUSTOMER_CODE}, 
                   DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_TYPE}, 
                   DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_CODE}, 
                   DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_VERSION}, 
                   DF.${GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_DATA};
        """.trimIndent()

}                    