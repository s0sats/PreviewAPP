package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 01/03/2017.
 */

public class Sql_Act005_001 implements Specification {
    public static final String BADGE_IN_PROCESSING_QTY = "in_processing_qty";
    private String customer_code;

    public Sql_Act005_001(String customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) " + BADGE_IN_PROCESSING_QTY + " \n" +
                        " FROM\n" +
                        "   ge_custom_forms_local l\n" +
                        " WHERE\n" +
                        "   l.customer_code = '" + customer_code + "' \n" +
                        "   and l.custom_form_status = '" + Constant.SYS_STATUS_IN_PROCESSING + "'\n")
                .append(";" + BADGE_IN_PROCESSING_QTY)
                .toString();
        /**
         * QUERY ABAIXO É EXEMPLO DE COMO FICARA
         * QUANDO TIVERMOS OUTROS MODULOS.
         */
       /* return
                sb
                        .append(" SELECT\n" +
                                "   SUM(sent_qty) sent_qty\n" +
                                " FROM\n" +
                                "    (\n" +
                                "      SELECT\n" +
                                "        count(1) sent_qty\n" +
                                "      FROM\n" +
                                "        ge_custom_forms_local l\n" +
                                "      WHERE\n" +
                                "        l.customer_code = 1 \n" +
                                "        and l.custom_form_status = 'SENT'\n" +
                                "        \n" +
                                "      UNION ALL\n" +
                                "      \n" +
                                "      SELECT\n" +
                                "        count(1) sent_qty\n" +
                                "      FROM\n" +
                                "        ge_service_order_local D\n" +
                                "      WHERE\n" +
                                "        D.customer_code = 1 \n" +
                                "        and D.custom_form_status = 'SENT'\n" +
                                "    )T")
                        .toString();*/


    }
}
