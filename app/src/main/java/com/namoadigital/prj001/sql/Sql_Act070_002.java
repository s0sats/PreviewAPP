package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 23/07/2020
 * Query que seleciona os controles de um step ja ordenados.
 * LUCHE - 10/08/2020
 * Modificado order by para que os tmp sem seq sejam colocados no final da lista *
 */

public class Sql_Act070_002 implements Specification {

    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int step_code;

    public Sql_Act070_002(long customer_code, int ticket_prefix, int ticket_code, int step_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.step_code = step_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "    c.*\n" +
                    " FROM\n" +
                    "   " + TK_Ticket_CtrlDao.TABLE +"  c\n" +
                    " WHERE\n" +
                    "      c.customer_code = '" + customer_code +"'\n" +
                    "      and c.ticket_prefix = '" + ticket_prefix +"'\n" +
                    "      and c.ticket_code = '" + ticket_code +"'\n" +
                    "      and c.step_code = '" + step_code +"'\n" +
                    " ORDER BY \n" +
                    "       CASE WHEN c.ticket_seq <> 0 \n" +
                    "            THEN c.ticket_seq \n" +
                    "            ELSE c.ticket_seq_tmp \n" +
                    "       END,\n" +
                    "       c.step_order,\n" +
                    "       c.step_code \n"
            )
            .toString();
    }
}
