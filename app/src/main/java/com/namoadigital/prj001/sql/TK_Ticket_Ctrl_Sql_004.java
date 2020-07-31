package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 30/07/2020
 * Query que seleciona o proximo ticket_seq_tmp para ser usado no ctrl e processos filhos
 * Usada no TK_Ticket_CtrlDao
 */
public class TK_Ticket_Ctrl_Sql_004 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int ticket_seq_tmp;

    public TK_Ticket_Ctrl_Sql_004(long customer_code, int ticket_prefix, int ticket_code,int ticket_seq_tmp) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.ticket_seq_tmp = ticket_seq_tmp;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "     c.* \n"+
                    " FROM\n" +
                    "   " + TK_Ticket_CtrlDao.TABLE +" c\n" +
                    " WHERE\n" +
                    "      c.customer_code = '" + customer_code +"'\n" +
                    "      and c.ticket_prefix = '" + ticket_prefix +"'\n" +
                    "      and c.ticket_code = '" + ticket_code +"'\n" +
                    //"      and c.ticket_seq ='0\n" +
                    "      and c.ticket_seq_tmp = '" + ticket_seq_tmp +"'\n"
            )
            .toString();
    }
}
