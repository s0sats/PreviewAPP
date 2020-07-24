package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 24/07/2020
 * Query que seleciona a aprovação de um step
 */

public class TK_Ticket_Approval_Sql_001 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int ticket_seq;
    private int step_code;

    public TK_Ticket_Approval_Sql_001(long customer_code, int ticket_prefix, int ticket_code, int ticket_seq, int step_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.ticket_seq = ticket_seq;
        this.step_code = step_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "       *\n" +
                    " FROM\n" +
                    "   " + TK_Ticket_ApprovalDao.TABLE +"  a\n" +
                    " WHERE\n" +
                    "      a.customer_code = '" + customer_code +"'\n" +
                    "      and a.ticket_prefix = '" + ticket_prefix +"'\n" +
                    "      and a.ticket_code = '" + ticket_code +"'\n" +
                    "      and a.step_code = '" + step_code +"'\n" +
                    "      and a.ticket_seq = '" + ticket_seq +"'")
            .toString();
    }
}
