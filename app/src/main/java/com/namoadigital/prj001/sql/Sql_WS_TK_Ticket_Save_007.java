package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

/**
 * query que seta update required e update_required_product no cabeçalho do ticket
 */

public class Sql_WS_TK_Ticket_Save_007 implements Specification {

    private long customer_code;
    private long ticket_prefix;
    private long ticket_code;
    private int scn;

    public Sql_WS_TK_Ticket_Save_007(long customer_code, long ticket_prefix, long ticket_code, int scn) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.scn = scn;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ TK_TicketDao.TABLE+" set\n" +
                        "   scn = '"+scn+"'\n" +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and ticket_prefix = '"+ticket_prefix+"'\n" +
                        "  and ticket_code = '"+ticket_code+"'")
                .toString();
    }
}
