package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Query que seta update required no step
 */

public class Sql_SV_Location_Tracker_002 implements Specification {

    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int step_code;

    public Sql_SV_Location_Tracker_002(long customer_code, int ticket_prefix, int ticket_code, int step_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.step_code = step_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ TK_Ticket_StepDao.TABLE+" set\n" +
                        "   update_required = 1 \n" +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and ticket_prefix = '"+ticket_prefix+"'\n" +
                        "  and ticket_code = '"+ticket_code+"'\n" +
                        "  and step_code = '"+step_code+"'\n")
                .toString();
    }
}
