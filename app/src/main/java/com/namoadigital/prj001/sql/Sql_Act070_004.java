package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 24/08/2020
 * Query que verifica se o master_data do form existe no banco
 */

public class Sql_Act070_004 implements Specification {
    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;

    public Sql_Act070_004(long customer_code, int custom_form_type, int custom_form_code, int custom_form_version) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "    f.*\n" +
                    " FROM\n" +
                    "   " + GE_Custom_FormDao.TABLE +"  f\n" +
                    " WHERE\n" +
                    "      f.customer_code = '" + customer_code +"'\n" +
                    "      and f.custom_form_type = '" + custom_form_type +"'\n" +
                    "      and f.custom_form_code = '" + custom_form_code +"'\n" +
                    "      and f.custom_form_version = '" + custom_form_version +"'\n"
            )
            .toString();
    }
}
