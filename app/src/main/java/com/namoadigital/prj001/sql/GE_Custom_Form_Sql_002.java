package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 08/02/17.
 */

public class GE_Custom_Form_Sql_002 implements Specification {

    private String s_customer_code;
    private String s_formtype_code;
    private String s_form_code;
    private String s_formversion_code;

    public GE_Custom_Form_Sql_002(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append(
                " SELECT " +
                        " * " +
                        " FROM " +
                        GE_Custom_FormDao.TABLE +
                        " WHERE " +
                        GE_Custom_FormDao.CUSTOMER_CODE + "= '" + s_customer_code + "' " +
                        "     AND " + GE_Custom_FormDao.CUSTOM_FORM_TYPE + " = '" + s_formtype_code + "' " +
                        "     AND " + GE_Custom_FormDao.CUSTOM_FORM_CODE + " = '" + s_form_code + "' " +
                        "     AND " + GE_Custom_FormDao.CUSTOM_FORM_VERSION + " = '" + s_formversion_code + "' ")
                .toString();
    }
}
