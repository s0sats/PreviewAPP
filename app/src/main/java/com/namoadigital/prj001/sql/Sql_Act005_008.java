package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 25/05/2017.
 * <p>
 * Verifica qtd de SO's pendentes de envio
 */

public class Sql_Act005_008 implements Specification {
    public static final String BADGE_TO_SEND_QTY = "badge_to_send_qty";

    private long customer_code;

    public Sql_Act005_008(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();
        return
                sb
                        .append("SELECT\n" +
                                "  count(1) " + BADGE_TO_SEND_QTY + "\n" +
                                "FROM\n" +
                                "  md_product_serials ps\n" +
                                "WHERE\n" +
                                "  ps.customer_code = '" + customer_code + "'\n" +
                                "  and (ps.update_required = 1\n" +
                                "  );")
                        //.append(BADGE_TO_SEND_QTY)
                        .toString();
    }
}
