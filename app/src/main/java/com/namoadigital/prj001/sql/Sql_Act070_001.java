package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 23/07/2020
 * Query que seleciona os steps do ticket, excluindo a origem(step_code == 0)
 */

public class Sql_Act070_001 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;

    public Sql_Act070_001(long customer_code, int ticket_prefix, int ticket_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "      s.*\n" +
                    " FROM\n" +
                    "   " + TK_Ticket_StepDao.TABLE +" s\n" +
                    " WHERE\n" +
                    "      s.customer_code = '" + customer_code +"'\n" +
                    "      and s.ticket_prefix = '" + ticket_prefix +"'\n" +
                    "      and s.ticket_code = '" + ticket_code +"'" +
                    "      and s.step_code > 0 \n" +
                    " order by\n" +
                    "       s.step_order,\n" +
                    "       s.step_code\n;"
            )
            .toString();
    }
}
