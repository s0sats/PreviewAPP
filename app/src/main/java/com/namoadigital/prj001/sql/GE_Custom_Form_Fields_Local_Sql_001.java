package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 08/02/17.
 */

public class GE_Custom_Form_Fields_Local_Sql_001 implements Specification {

    private String s_customer_code;
    private String s_formtype_code;
    private String s_form_code;
    private String s_formversion_code;
    private String s_form_data;

    public GE_Custom_Form_Fields_Local_Sql_001(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code, String s_form_data) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;
        this.s_form_data = s_form_data;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append(
                " SELECT " +
                        " * " +
                        " FROM " +
                        GE_Custom_Form_Field_LocalDao.TABLE +
                        " WHERE " +
                        GE_Custom_Form_LocalDao.CUSTOMER_CODE + "= '" + s_customer_code + "' " +
                        "     AND " + GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_TYPE + " = '" + s_formtype_code + "' " +
                        "     AND " + GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_CODE + " = '" + s_form_code + "' " +
                        "     AND " + GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_VERSION + " = '" + s_formversion_code + "' " +
                        "     AND " + GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA + " = '" + s_form_data + "' " +
                        "     ORDER BY " +
                        "         " + GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_TYPE +
                        "      ,  " + GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_CODE +
                        "      ,  " + GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_VERSION +
                        "      ,  " + GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA +
                        "      ,  " + GE_Custom_Form_Field_LocalDao.PAGE +
                        "      ,  " + GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_ORDER  +
                        ";CUSTOMER_CODE#CUSTOM_FORM_TYPE#CUSTOM_FORM_CODE#CUSTOM_FORM_VERSION#CUSTOM_FORM_DATA#CUSTOM_FORM_SEQ#CUSTOM_FORM_DATA_TYPE#CUSTOM_FORM_DATA_SIZE#CUSTOM_FORM_DATA_MASK#CUSTOM_FORM_DATA_CONTENT#CUSTOM_FORM_LOCAL_LINK#CUSTOM_FORM_ORDER#PAGE#REQUIRED#AUTOMATIC#COMMENT#CUSTOM_FORM_FIELD_DESC;")

                .toString();
    }
}
