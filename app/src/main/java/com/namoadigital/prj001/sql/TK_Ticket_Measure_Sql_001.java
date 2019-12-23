package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_MeasureDao;
import com.namoadigital.prj001.database.Specification;

public class TK_Ticket_Measure_Sql_001 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int ticket_seq;

    public TK_Ticket_Measure_Sql_001(long customer_code, int ticket_prefix, int ticket_code, int ticket_seq) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.ticket_seq = ticket_seq;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "       *\n" +
                    " FROM\n" +
                    "   " + TK_Ticket_MeasureDao.TABLE +"  m\n" +
                    " WHERE\n" +
                    "      m.customer_code = '" + customer_code +"'\n" +
                    "      and m.ticket_prefix = '" + ticket_prefix +"'\n" +
                    "      and m.ticket_code = '" + ticket_code +"'\n" +
                    "      and m.ticket_seq = '" + ticket_seq +"'")
            .toString();
    }
}
