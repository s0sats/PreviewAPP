package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

/**
 *
 * Reseta sync_required
 */
public class TK_Ticket_Sql_004 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int sync_required;

    public TK_Ticket_Sql_004(long customer_code, int ticket_prefix, int ticket_code,int sync_required) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.sync_required = sync_required;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(
                          " UPDATE " + TK_TicketDao.TABLE + " set\n" +
                          "   sync_required = '" + sync_required + "'\n" +
                          " WHERE\n" +
                          "  customer_code = '" + customer_code + "'\n" +
                          "  and ticket_prefix = '" + ticket_prefix + "'\n" +
                          "  and ticket_code = '" + ticket_code + "'\n")
                .toString();
    }
}
