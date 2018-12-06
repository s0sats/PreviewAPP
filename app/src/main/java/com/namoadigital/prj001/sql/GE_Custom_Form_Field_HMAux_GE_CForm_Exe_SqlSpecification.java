package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_Field_HMAux_GE_CForm_Exe_SqlSpecification implements Specification {

    private String s_customer_code;
    private String s_formtype_code;
    private String s_form_code;
    private String s_formversion_code;
    private String s_translate_code;

    public GE_Custom_Form_Field_HMAux_GE_CForm_Exe_SqlSpecification(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code, String s_translate_code) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;
        this.s_translate_code = s_translate_code;
    }

    @Override
    public String toSqlQuery() {

        String opc = s_customer_code + "|" + s_formtype_code + "|" + s_form_code + "|" + s_formversion_code;

        StringBuilder sb = new StringBuilder();

        return sb
                .append("select * from ge_custom_form_fields ge inner join ( \n" +
                        "\n" +
                        "select resource_code, txt_code, txt_value from (\n" +
                        "select * from ev_module_res_txt_transs as tr where tr.resource_code in (\n" +
                        " \n" +
                        "select mr.resource_code from ev_module_ress as mr where mr.resource_name = '")
                .append(opc)
                .append("' and mr.module_code = 'cust_form'\n" +
                        "\n" +
                        ") and tr.translate_code = '")
                .append(s_translate_code)
                .append("' and tr.module_code = 'cust_form' ) as resultado ) on ge.custom_form_seq = txt_code and ge.customer_code  || '|' || ge.custom_form_type  || '|' || ge.custom_form_code  || '|' || ge.custom_form_version = '")
                .append(opc)
                .append("' order by ge.custom_form_order ")
                .append(";")
                //.append("customer_code#custom_form_type#custom_form_code#custom_form_version#custom_form_seq#custom_form_data_type#custom_form_data_size#custom_form_data_mask#custom_form_data_content#custom_form_order#page#required#resource_code#txt_code#txt_value#custom_form_local_link")
                .toString();
    }
}
