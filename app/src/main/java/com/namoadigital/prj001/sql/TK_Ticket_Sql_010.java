package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;


/**
 * LUCHE -11/03/2020
 *<p></p>
 * Query que retorna o proximo ticketCode para itens agendados
 */
public class TK_Ticket_Sql_010 implements Specification {
    public static final String NEXT_SCHEDULE_TICKET_CODE = "NEXT_SCHEDULE_TICKET_CODE";
    private long customer_code;

    public TK_Ticket_Sql_010(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "  IFNULL(MAX(t.ticket_code),0) + 1 " +NEXT_SCHEDULE_TICKET_CODE+" \n" +
                    " FROM\n" +
                    "   " + TK_TicketDao.TABLE+ " t\n" +
                    " WHERE\n" +
                    "  t.customer_code = '"+customer_code+"'\n" +
                    "  and t.ticket_prefix = 0 \n")
            .toString();
    }
}
