package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.database.Specification;

public class Sql_Act070_006 implements Specification {
    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;

    public Sql_Act070_006(long customer_code, int custom_form_type, int custom_form_code) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
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
                        "      order by f.custom_form_version desc \n" +
                        "      limit 1 \n"
                )
                .toString();
    }
}
