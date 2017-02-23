package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 22/02/2017.
 */

public class GE_Custom_Form_Data_Sql_003 implements Specification {

    private String customer_code;
    private String custom_form_type;
    private String custom_form_code;
    private String custom_form_version;
    private String custom_form_data;

    public GE_Custom_Form_Data_Sql_003(String customer_code, String custom_form_type, String custom_form_code, String custom_form_version, String custom_form_data) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
        this.custom_form_data = custom_form_data;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("update \n" +
                        "\n" +
                        "ge_custom_form_datas \n" +
                        "\n" +
                        "set \n" +
                        "\n")
                .append("custom_form_status = '").append("DELETE").append("' \n" +
                        "\n" +
                        "where \n" +
                        "\n")
                .append("customer_code = '").append(customer_code).append("' ")
                .append(("and "))
                .append("custom_form_type = '").append(custom_form_type).append("' ")
                .append(("and "))
                .append("custom_form_code = '").append(custom_form_code).append("' ")
                .append(("and "))
                .append("custom_form_version = '").append(custom_form_version).append("' ")
                .append(("and "))
                .append("custom_form_data = '").append(custom_form_data).append("'")
                .append(";")
                .toString();

    }
}
