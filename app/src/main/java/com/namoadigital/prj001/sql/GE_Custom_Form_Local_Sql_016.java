package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

public class GE_Custom_Form_Local_Sql_016 implements Specification {

    String customer_code;
    String custom_form_type;
    String custom_form_code;
    String custom_form_version;
    String custom_form_data;
    String custom_form_status;
    String date_end;

    public GE_Custom_Form_Local_Sql_016(String customer_code, String custom_form_type, String custom_form_code, String custom_form_version, String custom_form_data, String custom_form_status, String date_end) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
        this.custom_form_data = custom_form_data;
        this.custom_form_status = custom_form_status;
        this.date_end = date_end;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " + GE_Custom_Form_LocalDao.TABLE + " set\n" +
                        "   custom_form_status = '" + custom_form_status + "'\n" +
//                        "   date_end = '" + date_end + "'\n" +
                        " WHERE\n" +
                        "   customer_code = " + customer_code + "\n" +
                        " and custom_form_type = " + custom_form_type + "\n" +
                        " and custom_form_code = " + custom_form_code + "\n" +
                        " and custom_form_version = "+custom_form_version +"\n" +
                        " and custom_form_data = " + custom_form_data + ";")
                .toString();
    }
}
