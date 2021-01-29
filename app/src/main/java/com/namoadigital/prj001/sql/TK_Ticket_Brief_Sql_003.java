package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_BriefDao;
import com.namoadigital.prj001.database.Specification;

public class TK_Ticket_Brief_Sql_003 implements Specification {
    private long customer_code;

    public TK_Ticket_Brief_Sql_003(long customerCode) {
        this.customer_code = customerCode;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" DELETE FROM " + TK_Ticket_BriefDao.TABLE + " \n" +
                        " WHERE\n" +
                        "  customer_code  = '" + customer_code + "' \n "+
                        "  and fcm  = 0 \n "
                )
                .toString();
    }
}
