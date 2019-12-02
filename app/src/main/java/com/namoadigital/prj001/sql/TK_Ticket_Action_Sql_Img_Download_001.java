package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_ActionDao;
import com.namoadigital.prj001.database.Specification;

public class TK_Ticket_Action_Sql_Img_Download_001 implements Specification {
    public static final String FILE_LOCAL_NAME = "FILE_LOCAL_NAME";

    private long customer_code;

    public TK_Ticket_Action_Sql_Img_Download_001(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
            .append(" SELECT\n" +
                    "  a.*,\n" +
                    "  't_'||a.customer_code||'_'||a.ticket_prefix||'_'||a.ticket_code||'_'||a.ticket_seq  '"+FILE_LOCAL_NAME+"'\n" +
                    " FROM\n" +
                    TK_Ticket_ActionDao.TABLE +"  a\n" +
                    " WHERE\n" +
                    "   a.customer_code = '"+customer_code+"'\n" +
                    "   and a.action_photo is not null\n" +
                    "   and a.action_photo_local is null\n"+
                    " ORDER BY\n" +
                    "   a.ticket_prefix,\n" +
                    "   a.ticket_code,\n" +
                    "   a.ticket_seq\n")
            .append(";")
            .toString();
    }
}
