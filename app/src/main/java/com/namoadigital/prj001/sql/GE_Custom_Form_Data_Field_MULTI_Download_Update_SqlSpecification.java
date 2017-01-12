package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_Data_Field_MULTI_Download_Update_SqlSpecification implements Specification {

    private String l_customer_code;
    private String l_custom_form_type;
    private String l_custom_form_code;
    private String l_custom_form_version;
    private String l_custom_form_seq;
    private String s_nome;

    public GE_Custom_Form_Data_Field_MULTI_Download_Update_SqlSpecification(String l_customer_code, String l_custom_form_type, String l_custom_form_code, String l_custom_form_version, String l_custom_form_seq, String s_nome) {
        this.l_customer_code = l_customer_code;
        this.l_custom_form_type = l_custom_form_type;
        this.l_custom_form_code = l_custom_form_code;
        this.l_custom_form_version = l_custom_form_version;
        this.l_custom_form_seq = l_custom_form_seq;
        this.s_nome = s_nome;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("update \n" +
                        "\n" +
                        "ge_custom_form_fields \n" +
                        "\n" +
                        "set\n" +
                        "\n")
                .append("custom_form_local_link = '").append(s_nome).append("' \n" +
                        "\n" +
                        "where \n" +
                        "\n")
                .append("customer_code = '").append(l_customer_code).append("' and \n")
                .append("custom_form_type = '").append(l_custom_form_type).append("' and \n")
                .append("custom_form_code = '").append(l_custom_form_code).append("' and \n")
                .append("custom_form_version = '").append(l_custom_form_version).append("' and \n")
                .append("custom_form_seq = '").append(l_custom_form_seq).append("' ")
                .append(";")

                .toString();
    }
}
