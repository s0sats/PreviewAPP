package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

public class Sql_Act005_010 implements Specification{
    public static final String QTY = "qty";

    private long customer_code;

    public Sql_Act005_010(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append( "SELECT\n" +
                        "     count(1) "+QTY+"\n " +
                        " FROM\n" +
                        TK_TicketDao.TABLE +" s\n" +
                        " WHERE \n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   and (s.update_required = 1" +
                        "        or s.update_required_product = 1 )\n" +
                        "; \n")
                .toString();
    }
}
