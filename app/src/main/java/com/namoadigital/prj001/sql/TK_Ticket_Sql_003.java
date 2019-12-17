package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

/**
 *
 * Atualiza syn_required
 */
public class TK_Ticket_Sql_003 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int scn;

    public TK_Ticket_Sql_003(long customer_code, int ticket_prefix, int ticket_code, int scn) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.scn = scn;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        String sResp = "";

        if (scn != 0) {
            sResp = "  and scn < '" + scn + "'";
        }

        return sb
                .append(
                          " UPDATE " + TK_TicketDao.TABLE + " set\n" +
                          "   sync_required = '" + "1" + "'\n" +
                          " WHERE\n" +
                          "  customer_code = '" + customer_code + "'\n" +
                          "  and ticket_prefix = '" + ticket_prefix + "'\n" +
                          "  and ticket_code = '" + ticket_code + "'\n")
                .append(sResp)
                .toString();
    }
}
