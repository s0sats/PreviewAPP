package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Query que seta update required no ticket
 */

public class Sql_SV_Location_Tracker_001 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;

    public Sql_SV_Location_Tracker_001(long customer_code, int ticket_prefix, int ticket_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ TK_TicketDao.TABLE+" set\n" +
                        "   update_required = 1 \n" +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and ticket_prefix = '"+ticket_prefix+"'\n" +
                        "  and ticket_code = '"+ticket_code+"'\n"
                        )
                .toString();
    }
}
