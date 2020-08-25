package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;

public class Sql_Act068_001 implements Specification {
    public static final String UPDATE_SYNC_REQUIRED_QTY = "update_sync_required_qty";

    private long customer_code;

    public Sql_Act068_001(long customer_code) {
        this.customer_code = customer_code;
    }


    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return
                sb
                        .append("SELECT\n" +
                                "  distinct t.*\n" +
                                "FROM\n" +
                                "     "+ TK_TicketDao.TABLE +" t,\n" +
                                "     "+ TK_Ticket_StepDao.TABLE +" s,\n" +
                                "     "+ TK_Ticket_CtrlDao.TABLE +" c\n" +
                                "WHERE\n" +
                                "  t.customer_code = '" + customer_code + "'\n" +
                                "   and t.ticket_prefix = s.ticket_prefix \n" +
                                "  and t.ticket_code = s.ticket_code\n" +
                                "  and s.ticket_prefix = c.ticket_prefix\n" +
                                "  and s.ticket_code = c.ticket_code\n" +
                                "  and t.sync_required = 1\n" +
                                "  and t.update_required_product = 0\n" +
                                "  and t.update_required = 0\n" +
                                "  and s.update_required = 0\n" +
                                "  and c.update_required = 0" +
                                "  ;").toString();
    }
}
