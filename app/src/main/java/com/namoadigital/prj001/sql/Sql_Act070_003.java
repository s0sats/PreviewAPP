package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 24/08/2020
 * Query que seleciona o form baseado na pk do ticket_ctrl
 */

public class Sql_Act070_003 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int ticket_seq;
    private int ticket_seq_tmp;
    private int step_code;

    public Sql_Act070_003(long customer_code, int ticket_prefix, int ticket_code, int ticket_seq, int ticket_seq_tmp, int step_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.ticket_seq = ticket_seq;
        this.ticket_seq_tmp = ticket_seq_tmp;
        this.step_code = step_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "    d.*\n" +
                    " FROM\n" +
                    "   " + GE_Custom_Form_DataDao.TABLE +"  d\n" +
                    " WHERE\n" +
                    "      d.customer_code = '" + customer_code +"'\n" +
                    "      and d.ticket_prefix = '" + ticket_prefix +"'\n" +
                    "      and d.ticket_code = '" + ticket_code +"'\n" +
                    "      and d.ticket_seq = '" + ticket_seq +"'\n" +
                    "      and d.ticket_seq_tmp = '" + ticket_seq_tmp +"'\n" +
                    "      and d.step_code = '" + step_code +"'\n"
            )
            .toString();
    }
}
