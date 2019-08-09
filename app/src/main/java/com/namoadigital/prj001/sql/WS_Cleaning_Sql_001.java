package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 23/02/17.
 */

public class WS_Cleaning_Sql_001 implements Specification {

    private String s_date;

    public WS_Cleaning_Sql_001(String s_date) {
        this.s_date = s_date;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append("select * FROM ge_custom_form_datas where custom_form_status in ('"+
                        ConstantBase.SYS_STATUS_SENT + "', '"+ ConstantBase.SYS_STATUS_DELETED + "') and Date(date_end) <")
                .append("Date('")
                .append(s_date)
                .append("');")
                //.append(";customer_code#custom_form_type#custom_form_code#custom_form_version#custom_form_data#custom_form_status#product_code")
                .toString();
    }
}
