package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * BARRIONUEVO 01-09-2020
 * Query responsavel por definir chamada de WS_save antes do save do ticket
 *
 */
public class Sql_Act070_005 implements Specification {

    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;

    public Sql_Act070_005(long customer_code, int ticket_prefix, int ticket_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "    d.*\n" +
                        " FROM\n" +
                        "   " + GE_Custom_Form_DataDao.TABLE +"  d,\n" +
                        "   " + TK_Ticket_CtrlDao.TABLE +"  c\n" +
                        " WHERE\n" +
                        "      d.customer_code = '" + customer_code +"'\n" +
                        "      and d.ticket_prefix = '" + ticket_prefix +"'\n" +
                        "      and d.ticket_code = '" + ticket_code +"'\n" +
                        "      and d.ticket_prefix =   c.ticket_prefix \n" +
                        "      and d.ticket_code =   c.ticket_code \n" +
                        "      and c.ctrl_status = " + "'" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"'"+
                        "      and d.custom_form_status = " + "'" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"'"+
                        "      and d.location_pendency =   0 \n"
                )
                .toString();
    }
}
