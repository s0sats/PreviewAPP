package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

public class TK_Ticket_Sql_Img_Download_002 implements Specification {
    public static final String FILE_LOCAL_NAME = "FILE_LOCAL_NAME";

    private long customer_code;
    private String ticket_prefix;
    private String ticket_code;
    private String open_photo_local;

    public TK_Ticket_Sql_Img_Download_002(long customer_code, String ticket_prefix, String ticket_code, String open_photo_local) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.open_photo_local = open_photo_local;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
            .append(" UPDATE " + TK_TicketDao.TABLE +"\n" +
                    " SET open_photo_local =  '"+open_photo_local+"'\n" +
                    " WHERE\n" +
                    "   customer_code = '"+customer_code+"'\n" +
                    "   and ticket_prefix = '"+ticket_prefix+"'\n" +
                    "   and ticket_code = '"+ticket_code+"'\n")
            .append(";")
            .toString();
    }
}
