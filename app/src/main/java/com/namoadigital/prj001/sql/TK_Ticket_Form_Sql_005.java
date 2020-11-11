package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_FormDao;
import com.namoadigital.prj001.database.Specification;

public class TK_Ticket_Form_Sql_005 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int ticket_seq_tmp;
    private int step_code;

    public TK_Ticket_Form_Sql_005(long customer_code, int ticket_prefix, int ticket_code, int ticket_seq_tmp, int step_code) {
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
                .append(" DELETE FROM " + "   " + TK_Ticket_FormDao.TABLE +"   \n" +
                        " WHERE\n" +
                        "      customer_code = '" + customer_code +"'\n" +
                        "      and ticket_prefix = '" + ticket_prefix +"'\n" +
                        "      and ticket_code = '" + ticket_code +"'\n" +
                        "      and step_code= '" + step_code +"'\n" +
                        "      and ticket_seq_tmp = '" + ticket_seq_tmp +"'"
                )
                .toString();
    }
}
