package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 30/07/2020
 * Query que seleciona o proximo ticket_seq_tmp para ser usado no ctrl e processos filhos
 * Usada no TK_Ticket_CtrlDao
 */
public class TK_Ticket_Ctrl_Sql_003 implements Specification {
    public static final String NEXT_TICKET_SEQ_TMP = "NEXT_TICKET_SEQ_TMP";

    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int step_code;

    public TK_Ticket_Ctrl_Sql_003(long customer_code, int ticket_prefix, int ticket_code, int step_code) {
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
                    "       IFNULL(MAX(c.ticket_seq_tmp),1000) + 1 "+NEXT_TICKET_SEQ_TMP+"\n" +
                    " FROM\n" +
                    "   " + TK_Ticket_CtrlDao.TABLE +"  c\n" +
                    " WHERE\n" +
                    "      c.customer_code = '" + customer_code +"'\n" +
                    "      and c.ticket_prefix = '" + ticket_prefix +"'\n" +
                    "      and c.ticket_code = '" + ticket_code +"'\n"
                    //"      and c.step_code = '" + step_code +"'\n"
            )
            .toString();
    }
}
