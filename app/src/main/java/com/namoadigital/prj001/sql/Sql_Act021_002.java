package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 25/05/2017.
 * <p>
 * Verifica qtd de SO's pendentes de envio
 */

public class Sql_Act021_002 implements Specification {
    public static final String PENDING_PROCESS_QTY = "pending_process_qty";

    private String customer_code;

    public Sql_Act021_002(String customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

//        return sb
//                .append(" SELECT\n" +
//                        "   count(1) " + PENDING_PROCESS_QTY + " \n" +
//                        " FROM\n" +
//                        "   sm_sos s\n" +
//                        " WHERE\n" +
//                        "   s.customer_code = '" + customer_code + "' \n" +
//                        "   and s.status = '" + Constant.SYS_STATUS_PROCESS + "'\n")
//                .append(";" + PENDING_PROCESS_QTY)
//                .toString();

        return sb
                .append(" SELECT\n" +
                        "   count(1) " + PENDING_PROCESS_QTY + " \n" +
                        " FROM\n" +
                        "   sm_sos s\n" +
                        " WHERE\n" +
                        "   s.customer_code = '" + customer_code + "' \n" +
                        "   and s.status != '" + Constant.SYS_STATUS_DONE + "'\n" +
                        "   and s.status != '" + Constant.SYS_STATUS_CANCELLED + "'\n")
                .append(";" + PENDING_PROCESS_QTY)
                .toString();

    }
}
