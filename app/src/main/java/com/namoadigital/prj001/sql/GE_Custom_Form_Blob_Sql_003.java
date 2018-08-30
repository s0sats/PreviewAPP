package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 08/02/17.
 */

public class GE_Custom_Form_Blob_Sql_003 implements Specification {

    private String s_customer_code;
    private String s_formtype_code;
    private String s_form_code;
    private String s_formversion_code;
    private String s_blob_code;
    private String s_name;

    public GE_Custom_Form_Blob_Sql_003(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code, String s_blob_code, String s_name) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;
        this.s_blob_code = s_blob_code;
        this.s_name = s_name;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("update \n" +
                        "\n" +
                        "ge_custom_form_blobs \n" +
                        "\n" +
                        "set\n" +
                        "\n")
                .append("blob_url_local = '").append(s_name).append("' \n" +
                        "\n" +
                        "where \n" +
                        "\n")
                .append("customer_code = '").append(s_customer_code).append("' and \n")
                .append("custom_form_type = '").append(s_formtype_code).append("' and \n")
                .append("custom_form_code = '").append(s_form_code).append("' and \n")
                .append("custom_form_version = '").append(s_formversion_code).append("' and \n")
                .append("blob_code = '").append(s_blob_code).append("' ")
                .append(";")
                .toString();
    }
}
