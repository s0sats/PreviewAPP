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

    /**
     * LUCHE - 13/03/2020
     * Construtor usado pelo WS que salva os tickets.
     * @param customer_code - Codigo do customer
     * @param schedule_prefix - Prefix do agendamento
     * @param schedule_code - Codigo do agendamento
     * @param schedule_exec - Exec do agendamento
     */
    public TK_Ticket_Sql_009(long customer_code, Integer schedule_prefix, Integer schedule_code, Integer schedule_exec) {
        this.customer_code = customer_code;
        this.schedule_prefix = String.valueOf(schedule_prefix);
        this.schedule_code = String.valueOf(schedule_code);
        this.schedule_exec = String.valueOf(schedule_exec);
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
