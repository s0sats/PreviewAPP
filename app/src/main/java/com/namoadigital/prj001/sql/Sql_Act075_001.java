package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;

public class Sql_Act075_001 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;

    public Sql_Act075_001(long customer_code, int ticket_prefix, int ticket_code) {
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
                        "     "+ TK_TicketDao.TABLE +" t,\n" +
                        "     "+ TK_Ticket_StepDao.TABLE +" s,\n" +
                        "     "+ TK_Ticket_CtrlDao.TABLE +" c\n" +
                        " WHERE\n" +
                        "      t.customer_code = '" + customer_code +"'\n" +
                        "      and t.ticket_prefix = '" + ticket_prefix +"'\n" +
                        "      and t.ticket_code = '" + ticket_code +"'\n" +
                        "  and t.customer_code = s.customer_code \n" +
                        "  and t.ticket_prefix = s.ticket_prefix \n" +
                        "  and t.ticket_code = s.ticket_code\n" +
                        "  and s.ticket_prefix = c.ticket_prefix\n" +
                        "  and s.ticket_code = c.ticket_code\n" +
                        "  and s.step_code = c.step_code\n" +
                        "  and (t.update_required_product = 1\n" +
                        "  or t.update_required = 1\n" +
                        "  or s.update_required = 1\n" +
                        "  or c.update_required = 1)"
                )
                .toString();
    }
}
