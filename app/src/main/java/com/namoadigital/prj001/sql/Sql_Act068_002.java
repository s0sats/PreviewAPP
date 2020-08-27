package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;

public class Sql_Act068_002 implements Specification {

    public static final String TICKET_PK = "TICKET_PK";
    private long customer_code;

    public Sql_Act068_002(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "      distinct t.customer_code ||'|'|| t.ticket_prefix ||'|'|| t.ticket_code ||'|'|| t.scn "+TICKET_PK+",\n" +
                        "       t.ticket_prefix,\n" +
                        "       t.ticket_code,\n" +
                        "       t.scn\n" +
                        " FROM\n" +
                        "     "+ TK_TicketDao.TABLE +" t,\n" +
                        "     "+ TK_Ticket_StepDao.TABLE +" s,\n" +
                        "     "+ TK_Ticket_CtrlDao.TABLE +" c\n" +
                        "WHERE\n" +
                        "  t.customer_code = '" + customer_code + "'\n" +
                        "   and t.ticket_prefix = s.ticket_prefix \n" +
                        "  and t.ticket_code = s.ticket_code\n" +
                        "  and s.ticket_prefix = c.ticket_prefix\n" +
                        "  and s.ticket_code = c.ticket_code\n" +
                        "  \n" +
                        "  and t.sync_required = 1\n" +
                        "  and t.update_required_product = 0\n" +
                        "  and t.update_required = 0\n" +
                        "  and s.update_required = 0\n" +
                        "  and c.update_required = 0" +
                        "  ;"
                ).toString();
    }
}
