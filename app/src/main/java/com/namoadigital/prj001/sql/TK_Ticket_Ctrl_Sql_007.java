package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

import static com.namoadigital.prj001.util.ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM;

public class TK_Ticket_Ctrl_Sql_007 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;


    public TK_Ticket_Ctrl_Sql_007(long customer_code, int ticket_prefix, int ticket_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;

    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT *\n" +
                        " FROM\n" +
                        "   " + TK_Ticket_CtrlDao.TABLE +"\n" +
                        " WHERE\n" +
                        "      customer_code = '" + customer_code +"'\n" +
                        "      and ticket_prefix = '" + ticket_prefix +"'\n" +
                        "      and ticket_code = '" + ticket_code +"'\n" +
                        //"      and c.ticket_seq ='0\n" +
                        "      and ctrl_status = '" + ConstantBaseApp.SYS_STATUS_PROCESS +"'\n" +
                        "      and ctrl_type = '" + TK_TICKET_CRTL_TYPE_FORM +"'\n" +
                        "      and obj_planned = 0 "
                )
                .toString();
    }
}
