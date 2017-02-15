package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 08/02/17.
 */

public class GE_Custom_Form_Local_Sql_004 implements Specification {

    private String s_customer_code;
    private String s_formtype_code;
    private String s_form_code;
    private String s_formversion_code;
    private String s_form_data;
    private String s_form_status;

    public GE_Custom_Form_Local_Sql_004(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code, String s_form_data, String s_form_status) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;
        this.s_form_data = s_form_data;
        this.s_form_status = s_form_status;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(
                        " UPDATE  ge_custom_forms_local SET " +
                                "        custom_form_status = '" + s_form_status + "' " +
                                " WHERE               " +
                                "  customer_code = '" + s_customer_code + "' " +
                                "  AND custom_form_type = '" + s_formtype_code + "' " +
                                "  AND custom_form_code = '" + s_form_code + "' " +
                                "  AND custom_form_version = '" + s_formversion_code + "' " +
                                "  AND custom_form_data = '" + s_form_data + "'")
                .toString();
    }
}
