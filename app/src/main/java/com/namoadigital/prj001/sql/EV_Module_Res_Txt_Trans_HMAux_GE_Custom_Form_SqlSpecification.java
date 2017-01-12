package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class EV_Module_Res_Txt_Trans_HMAux_GE_Custom_Form_SqlSpecification implements Specification {

    private String s_customer_code;
    private String s_custom_form_type;
    private String s_translate_code;


    public EV_Module_Res_Txt_Trans_HMAux_GE_Custom_Form_SqlSpecification(String s_customer_code, String s_custom_form_type, String s_translate_code) {
        this.s_customer_code = s_customer_code;
        this.s_custom_form_type = s_custom_form_type;
        this.s_translate_code = s_translate_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select mr.module_code, mr.resource_code, mr.resource_name, tr.txt_code, tr.txt_value, tr.translate_code from ev_module_ress as mr inner join ev_module_res_txt_transs as tr on\n" +
                        "mr.module_code = tr.module_code and mr.resource_code = tr.resource_code and mr.module_code = 'cust_form'\n" +
                        "\n" +
                        "and mr.resource_name in \n" +
                        "(\n" +
                        "\n" +
                        "select  customer_code  || '|' || custom_form_type  || '|' || custom_form_code  || '|' || custom_form_version as resource_name from ge_custom_forms \n" +
                        "where \n" +
                        "customer_code = '")
                .append(s_customer_code)
                .append("' \n" +
                        "and custom_form_type = '")
                .append(s_custom_form_type)
                .append("' \n" +
                        "and custom_form_status = 'ACTIVE'\n" +
                        "\n" +
                        ") \n" +
                        "\n" +
                        "and tr.txt_code = 'title' \n" +
                        "and tr.translate_code = '")
                .append(s_translate_code)
                .append("' ")
                .append(";")
                .append("module_code#resource_code#resource_name#txt_code#txt_value#translate_code")
                .toString();
    }
}
