package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 03/08/2020
 * Query que retorna o custom form local para o tk_ticket_form
 */

public class TK_Ticket_Form_Sql_003 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int ticket_seq_tmp;
    private int step_code;

    public TK_Ticket_Form_Sql_003(long customer_code, int ticket_prefix, int ticket_code, int ticket_seq_tmp, int step_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.ticket_seq_tmp = ticket_seq_tmp;
        this.step_code = step_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "     *\n" +
                    " FROM\n" +
                    "   " + GE_Custom_Form_LocalDao.TABLE +"  fl \n" +
                    " WHERE\n" +
                    "      fl.customer_code = '" + customer_code +"'\n" +
                    "      and fl.ticket_prefix = '" + ticket_prefix +"'\n" +
                    "      and fl.ticket_code = '" + ticket_code +"'\n" +
                    "      and fl.step_code= '" + step_code +"'\n" +
                    "      and fl.ticket_seq_tmp = '" + ticket_seq_tmp +"'" +
                    "      and fl.custom_form_status in (" +
                                    "'"+ ConstantBaseApp.SYS_STATUS_PENDING +"'," +
                                    "'"+ ConstantBaseApp.SYS_STATUS_IN_PROCESSING +"'," +
                                    "'"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC +"'" +
                    ")"
            )
            .toString();
    }
}
