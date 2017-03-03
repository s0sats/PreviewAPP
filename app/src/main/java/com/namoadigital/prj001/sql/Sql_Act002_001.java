package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 03/03/2017.
 */

public class Sql_Act002_001 implements Specification {

    public static final String QTY_CUSTOMER_PENDENCIES = "qty_customer_pendencies";

    private String s_customer_code;

    public Sql_Act002_001(String s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "       count(1) "+QTY_CUSTOMER_PENDENCIES+"\n" +
                        "  FROM\n" +
                        "      ge_custom_forms_local l\n" +
                        "  WHERE\n" +
                        "   l.customer_code = '"+s_customer_code+"' \n" +
                        "   and l.custom_form_status in(" +
                        "'"+ Constant.CUSTOM_FORM_STATUS_IN_PROCESSING+"'," +
                        "'"+ Constant.CUSTOM_FORM_STATUS_FINALIZED+"'" +
                        ");" +
                        QTY_CUSTOMER_PENDENCIES)
                .toString();
    }
}
