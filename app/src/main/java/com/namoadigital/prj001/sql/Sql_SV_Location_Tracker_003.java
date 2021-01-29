package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Query que seta update required no ctrl
 */

public class Sql_SV_Location_Tracker_003 implements Specification {

    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int step_code;
    private int ticket_seq;
    private int ticket_seq_tmp;

    public Sql_SV_Location_Tracker_003(long customer_code, int ticket_prefix, int ticket_code, int step_code, int ticket_seq, int ticket_seq_tmp) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.step_code = step_code;
        this.ticket_seq = ticket_seq;
        this.ticket_seq_tmp = ticket_seq_tmp;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ TK_Ticket_CtrlDao.TABLE+" set\n" +
                        "   update_required = 1 \n" +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and ticket_prefix = '"+ticket_prefix+"'\n" +
                        "  and ticket_code = '"+ticket_code+"'\n" +
                        "  and ticket_seq = '"+ticket_seq+"'\n" +
                        "  and ticket_seq_tmp = '"+ticket_seq_tmp+"'\n" +
                        "  and step_code = '"+step_code+"'\n"
                )
                .toString();
    }
}
