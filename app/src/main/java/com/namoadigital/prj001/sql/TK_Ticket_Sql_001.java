package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

public class TK_Ticket_Sql_001 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;

    public TK_Ticket_Sql_001(long customer_code, int ticket_prefix, int ticket_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "       t.*\n" +
                    " FROM\n" +
                    "   " + TK_TicketDao.TABLE +"  t\n" +
                    " WHERE\n" +
                    "      t.customer_code = '" + customer_code +"'\n" +
                    "      and t.ticket_prefix = '" + ticket_prefix +"'\n" +
                    "      and t.ticket_code = '" + ticket_code +"'\n")
            .toString();
    }
}
