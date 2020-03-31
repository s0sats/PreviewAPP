package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 01/03/2017.
 *
 * LUCHE - 30/03/2020
 * Modificado query, renomeado constante e substituindo o status FINALIZED pelo WAITING_SYNC
 */

public class Sql_Act005_002 implements Specification {

    public static final String BADGE_WAITING_SYNC_QTY = "waiting_sync_qty";
    private String customer_code;

    public Sql_Act005_002(String customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb =  new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) "+ BADGE_WAITING_SYNC_QTY +" \n" +
                        " FROM\n" +
                        "   ge_custom_forms_local l\n" +
                        " WHERE\n" +
                        "   l.customer_code = '"+customer_code+"' \n" +
                        "   and l.custom_form_status = '"+ Constant.SYS_STATUS_WAITING_SYNC+"'\n")
                .append(";")
                .toString();

    }
}
