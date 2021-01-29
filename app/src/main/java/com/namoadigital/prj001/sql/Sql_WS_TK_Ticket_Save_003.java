package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Query que seta update required no step
 */

public class Sql_WS_TK_Ticket_Save_003 implements Specification {

    private long customer_code;
    private long ticket_prefix;
    private long ticket_code;

    public Sql_WS_TK_Ticket_Save_003(long customer_code, long ticket_prefix, long ticket_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ TK_Ticket_StepDao.TABLE+" set\n" +
                        "   update_required = 0 \n" +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and ticket_prefix = '"+ticket_prefix+"'\n" +
                        "  and ticket_code = '"+ticket_code+"'\n")
                .toString();
    }
}
