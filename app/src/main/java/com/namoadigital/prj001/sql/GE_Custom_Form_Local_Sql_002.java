package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_Local_Sql_002 implements Specification {

    private String s_customer_code;
    private String s_formtype_code;
    private String s_form_code;
    private String s_formversion_code;

    public GE_Custom_Form_Local_Sql_002(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " select " +
                        "   ifnull(max(CFL.CUSTOM_FORM_DATA), 0) + 1 id " +
                        " from  " +
                        "     GE_Custom_Forms_Local CFL ")
//                        " where " +
//                        " cfl.customer_code = '"+s_customer_code+"' " +
//                        " and cfl.custom_form_type = '"+s_formtype_code+"' " +
//                        " and cfl.custom_form_code = '"+s_form_code+"' " +
//                        " and cfl.custom_form_version = '"+s_formversion_code+"' ")
                .append(";")
                //.append("id")
                .toString();
    }
}
