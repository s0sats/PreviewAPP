package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_Field_Sql_001 implements Specification {

    public GE_Custom_Form_Field_Sql_001() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("select \n" +
                        "\n" +
                        "customer_code, custom_form_type, custom_form_code, custom_form_version, custom_form_seq, custom_form_data_type, custom_form_data_content, \n" +
                        "(custom_form_data_type || \"_\" || customer_code || \"_\" || custom_form_type || \"_\" || custom_form_code || \"_\" || custom_form_version || \"_\" || custom_form_seq) as custom_name\n" +
                        "\n" +
                        "from \n" +
                        "\n" +
                        "ge_custom_form_fields \n" +
                        "\n" +
                        "where \n" +
                        "\n" +
                        "custom_form_local_link = '' \n" +
                        "\n" +
                        "and \n" +
                        "\n" +
                        "custom_form_data_type = 'PICTURE'\n" +
                        "\n" +
                        "order by \n" +
                        "\n" +
                        "customer_code, custom_form_type, custom_form_code, custom_form_version, custom_form_seq")
                .append(";")
                //.append("customer_code#custom_form_type#custom_form_code#custom_form_version#custom_form_seq#custom_form_data_type#custom_form_data_content#custom_name")
                .toString();
    }
}
