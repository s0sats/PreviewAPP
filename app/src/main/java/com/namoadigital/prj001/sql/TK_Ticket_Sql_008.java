package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;


/**
 *
 * LUCHE -21/01/2020
 *
 * Query que retorna qtd de tickets pendentes
 *
 */
public class TK_Ticket_Sql_008 implements Specification {

    public static final String PENDENCIES_QTY = "PENDENCIES_QTY";

    private long customer_code;

    public TK_Ticket_Sql_008(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "  IFNULL(count(1),0) "+PENDENCIES_QTY+" \n" +
                    " FROM\n" +
                    "   " + TK_TicketDao.TABLE+ " t\n" +
                    " WHERE\n" +
                    "  t.customer_code = '"+customer_code+"'\n" +
                    "  and t.ticket_prefix > 0 \n" +
                    "  and t.ticket_status in ('"+ ConstantBaseApp.SYS_STATUS_PENDING +"'," +
                                              "'"+ ConstantBaseApp.SYS_STATUS_PROCESS +"'," +
                                              "'"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC +"'" +
                ");")
            .toString()
            ;
    }
}
