package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

public class TK_Ticket_Sql_006 implements Specification {

    private long customer_code;

    public TK_Ticket_Sql_006(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "       t.*\n" +
                    " FROM\n" +
                        TK_TicketDao.TABLE + " t\n" +
                    " WHERE\n" +
                    "    t.customer_code =  '" + customer_code + "'\n" +
                    "    AND t.update_required = 1")
            .toString();
    }
}
