package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;


/**
 * LUCHE -11/03/2020
 *<p></p>
 * Query que retorna o ticket vinculado ao agendamento
 */
public class TK_Ticket_Sql_009 implements Specification {

    private long customer_code;
    private String schedule_prefix;
    private String schedule_code;
    private String schedule_exec;

    public TK_Ticket_Sql_009(long customer_code, String schedule_prefix, String schedule_code, String schedule_exec) {
        this.customer_code = customer_code;
        this.schedule_prefix = schedule_prefix;
        this.schedule_code = schedule_code;
        this.schedule_exec = schedule_exec;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "  t.*\n" +
                    " FROM\n" +
                    "   " + TK_TicketDao.TABLE+ " t\n" +
                    " WHERE\n" +
                    "  t.customer_code = '"+customer_code+"'\n" +
                    "  and t.schedule_prefix = '"+schedule_prefix+"'\n"+
                    "  and t.schedule_code = '"+schedule_code+"'\n"+
                    "  and t.schedule_exec = '"+schedule_exec+"'\n")
            .toString();
    }
}
