package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 25/05/2017.
 *
 * Verifica qtd de SO's pendentes de envio
 */

public class Sql_Act021_001 implements Specification {
    public static final String UPDATE_REQUIRED_QTY = "update_required_qty";

    private long customer_code;

    public Sql_Act021_001(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb =  new StringBuilder();
        return
                sb
                .append("SELECT\n" +
                        "  count(1) "+UPDATE_REQUIRED_QTY+"\n" +
                        "FROM\n" +
                        "  sm_sos s\n" +
                        "WHERE\n" +
                        "  s.customer_code = '"+customer_code+"'\n" +
                        "  and s.update_required = 1;")
                .append(UPDATE_REQUIRED_QTY)
                .toString();
    }
}
