package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 03/12/2019.
 * <p>
 * Verifica qtd de ticket pendentes de envio
 */

public class TK_Ticket_Sql_To_Send implements Specification {

    public static final String BADGE_TO_SEND_QTY = "badge_to_send_qty";

    private long customer_code;

    public TK_Ticket_Sql_To_Send(long customer_code) {
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
                                "  "+ TK_TicketDao.TABLE +" t\n" +
                                "WHERE\n" +
                                "  t.customer_code = '" + customer_code + "'\n" +
                                "  and t.update_required = '1'\n" )
                        //.append(BADGE_TO_SEND_QTY)
                        .toString();
    }
}
