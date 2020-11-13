package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_FormDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * BARRIONUEVO   11-11-2020
 * Verifica qtde de form espontaneos nao finalizados.
 */
public class TK_Ticket_Form_Sql_006 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int step_code;

    public TK_Ticket_Form_Sql_006(long customer_code, int ticket_prefix, int ticket_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;

//        this.step_code = step_code;
    }

    
    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT *\n" +
                        " FROM\n" +
                        "   " + TK_Ticket_FormDao.TABLE +"  f\n" +
                        " WHERE\n" +
                        "      f.customer_code = '" + customer_code +"'\n" +
                        "      and f.ticket_prefix = '" + ticket_prefix +"'\n" +
                        "      and f.ticket_code = '" + ticket_code +"'\n" +
//                        "      and f.step_code= '" + step_code +"'\n" +
                        "      and f.ticket_seq = 0 " +"\n" +
                        "      and f.ticket_seq_tmp > 0 " +"\n" +
                        "      and f.form_status IN ('" + ConstantBaseApp.SYS_STATUS_PROCESS +"')")
                .toString();
    }
}
