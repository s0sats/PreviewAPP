package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 3/24/17.
 */

public class GE_Custom_Form_Local_Sql_010 implements Specification {


    public GE_Custom_Form_Local_Sql_010() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("delete\n" +
                        "\n" +
                        "from ge_custom_forms_local\n" +
                        "\n" +
                        "where customer_code  || '|' || custom_form_type  || '|' || custom_form_code  || '|' || custom_form_version || '|' || custom_form_data  not in (\n" +
                        "\n" +
                        "select customer_code  || '|' || custom_form_type  || '|' || custom_form_code  || '|' || custom_form_version || '|' || custom_form_data  id from ge_custom_form_datas\n" +
                        "\n" +
                        ") " +
                        "AND custom_form_data_serv = 0")
                .toString();
    }
}
