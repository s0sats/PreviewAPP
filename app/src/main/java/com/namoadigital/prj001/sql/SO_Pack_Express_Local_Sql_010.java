package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 03/08/2017.
 * <p>
 * Atualiza origin_change para APP
 */

public class SO_Pack_Express_Local_Sql_010 implements Specification {
    public static final String BADGE_IN_NEW_QTY = "in_new_qty";
    private long customer_code;

    public SO_Pack_Express_Local_Sql_010(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) " + BADGE_IN_NEW_QTY + " \n" +
                        " FROM\n" +
                        "   so_pack_expresss_local s\n" +
                        " WHERE\n" +
                        "   s.customer_code = '" + customer_code + "' \n" +
                        "   and s.status = '" + "NEW" + "'\n")
                .append(";")
                //.append(BADGE_IN_NEW_QTY)
                .toString();
    }
}
