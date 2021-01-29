package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

public class Sql_Act069_002 implements Specification {

    public static final String TICKET_PK = "TICKET_PK";
    private long customer_code;

    public Sql_Act069_002(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "       t.customer_code ||'|'|| t.ticket_prefix ||'|'|| t.ticket_code ||'|'|| t.scn "+TICKET_PK+",\n" +
                    "       t.ticket_prefix,\n" +
                    "       t.ticket_code,\n" +
                    "       t.scn\n" +
                    " FROM\n" +
                    "     "+ TK_TicketDao.TABLE +" t\n" +
                    " WHERE\n" +
                    "       t.customer_code = '"+customer_code+"'\n" +
                    "      and t.sync_required = '1'\n" +
                    " ORDER BY \n" +
                    "  t.ticket_prefix,\n" +
                    "  t.ticket_code\n"
                    )
            .toString();
    }
}
