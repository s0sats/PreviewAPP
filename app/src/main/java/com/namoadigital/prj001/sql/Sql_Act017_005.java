package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 14/08/2020
 * Criado query que tentará selecionar o ctrl do "ticket" agendado. Usado nos casos em que o ticket agendado esta em waiting sync
 */

public class Sql_Act017_005 implements Specification {
    private long customer_code;
    private String schedule_prefix;
    private String schedule_code;
    private String schedule_exec;
    private String ticket_prefix;
    private String ticket_code;

    public Sql_Act017_005(long customer_code, String schedule_prefix, String schedule_code, String schedule_exec, String ticket_prefix, String ticket_code) {
        this.customer_code = customer_code;
        this.schedule_prefix = schedule_prefix;
        this.schedule_code = schedule_code;
        this.schedule_exec = schedule_exec;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "  c.* \n" +
                    " FROM\n" +
                    "   "+ TK_TicketDao.TABLE +"  t,\n" +
                    "   "+ TK_Ticket_CtrlDao.TABLE +"  c\n" +
                    " WHERE\n" +
                    " t.customer_code = c.customer_code\n" +
                    " and t.ticket_prefix = c.ticket_prefix\n" +
                    " and t.ticket_code = c.ticket_code\n" +
                    " --\n" +
                    " and t.customer_code = '"+customer_code+"'\n" +
                    " and t.schedule_prefix = '"+schedule_prefix+"' \n" +
                    " and t.schedule_code = '"+schedule_code+"'\n" +
                    " and t.schedule_exec = '"+schedule_exec+"'\n" +
                    " --\n" +
                    " and c.customer_code = '"+customer_code+"'\n" +
                    " and c.ticket_prefix = '"+ticket_prefix+"' \n" +
                    " and c.ticket_code = '"+ticket_code+"' \n" +
                    " --\n" +
                    " and c.step_code = 0\n" +
                    " and c.ticket_seq = 0\n" +
                    " and c.ticket_seq_tmp > 0\n" +
                ";")
            .toString();
    }
}
