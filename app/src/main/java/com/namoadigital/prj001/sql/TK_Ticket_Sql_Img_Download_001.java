package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class TK_Ticket_Sql_Img_Download_001 implements Specification {
    public static final String FILE_LOCAL_NAME = "FILE_LOCAL_NAME";

    private long customer_code;

    public TK_Ticket_Sql_Img_Download_001(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
            .append(" SELECT\n" +
                    "  t.*,\n" +
                    "  '"+ ConstantBaseApp.TK_TICKET_PREX_IMG+"'||t.customer_code||'_'||t.ticket_prefix||'_'||t.ticket_code '"+FILE_LOCAL_NAME+"'\n" +
                    " FROM\n" +
                    TK_TicketDao.TABLE +"  t\n" +
                    " WHERE\n" +
                    "   t.customer_code = '"+customer_code+"'\n" +
                    "   and t.open_photo is not null\n" +
                    "   and t.open_photo_local is null\n"+
                    " ORDER BY\n" +
                    "   t.ticket_prefix, \n"+
                    "   t.ticket_code \n")
            .append(";")
            .toString();
    }
}
