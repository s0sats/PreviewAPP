package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Query que seta update required no step
 */

public class Sql_WS_TK_Ticket_Save_005 implements Specification {

    private long customer_code;
    private long ticket_prefix;
    private long ticket_code;
    private int step_code;
    private int update_required;

    public Sql_WS_TK_Ticket_Save_005(long customer_code, long ticket_prefix, long ticket_code, int step_code, int update_required) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.step_code = step_code;
        this.update_required = update_required;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ TK_Ticket_StepDao.TABLE+" set\n" +
                        "   update_required = '"+update_required+"' \n" +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and ticket_prefix = '"+ticket_prefix+"'\n" +
                        "  and ticket_code = '"+ticket_code+"'\n" +
                        "  and step_code = '"+step_code+"'\n"
                )
                .toString();
    }
}
