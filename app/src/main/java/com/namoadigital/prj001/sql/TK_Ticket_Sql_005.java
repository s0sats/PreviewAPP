package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

public class TK_Ticket_Sql_005 implements Specification {

    private long customer_code;
    private long ticket_prefix;
    private long ticket_code;
    private long update_required;
    private String syncUpdate ="";

    public TK_Ticket_Sql_005(long customer_code, long ticket_prefix, long ticket_code, long update_required) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.update_required = update_required;
    }

    public TK_Ticket_Sql_005(long customer_code, long ticket_prefix, long ticket_code, long update_required, int sync_required) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.update_required = update_required;
        syncUpdate = ",\n sync_required = '"+sync_required+"'\n";
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ TK_TicketDao.TABLE+" set\n" +
                        "   update_required = '"+update_required+"'\n" +
                            syncUpdate +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and ticket_prefix = '"+ticket_prefix+"'\n" +
                        "  and ticket_code = '"+ticket_code+"'")
                .toString();
    }
}
