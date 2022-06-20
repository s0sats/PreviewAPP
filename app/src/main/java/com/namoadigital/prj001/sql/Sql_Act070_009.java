package com.namoadigital.prj001.sql;

import static com.namoadigital.prj001.util.ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_FormDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sql_Act070_009 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;


    public Sql_Act070_009(long customer_code, int ticket_prefix, int ticket_code) {
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
                        "   " + TK_Ticket_CtrlDao.TABLE + " c, \n" +
                        "   " + TK_Ticket_FormDao.TABLE + " f \n" +
                        " WHERE\n" +
                        "    c.customer_code = f.customer_code\n" +
                        "    and c.ticket_prefix = f.ticket_prefix\n" +
                        "    and c.ticket_code = f.ticket_code\n" +
                        "    and c.ticket_seq_tmp = f.ticket_seq_tmp\n" +
                        "    and c.step_code = f.step_code\n" +

                        "   and c.customer_code = '" + customer_code + "'\n" +
                        "   and c.ticket_prefix = '" + ticket_prefix + "'\n" +
                        "   and c.ticket_code = '" + ticket_code + "'\n" +
                        //"   and c.ticket_seq ='0\n" +
                        "   and c.ctrl_status = '" + ConstantBaseApp.SYS_STATUS_PROCESS + "'\n" +
                        "   and c.ctrl_type = '" + TK_TICKET_CRTL_TYPE_FORM + "'\n" +
                        "   and exists(SELECT 1 \n" +
                        "               FROM "+ GE_Custom_Form_DataDao.TABLE + " d\n" +
                        "               WHERE d.customer_code = f.customer_code\n" +
                        "                     and d.custom_form_type = f.custom_form_type\n" +
                        "                     and d.custom_form_code = f.custom_form_code\n" +
                        "                     and d.custom_form_version = f.custom_form_version  \n" +
                        "                     and (d.custom_form_data = f.custom_form_data \n" +
                        "                        or d.custom_form_data = f.custom_form_data_tmp)" +
                        "                     and d.custom_form_status = '"+ConstantBaseApp.SYS_STATUS_IN_PROCESSING+"'\n" +
                        "               ) "
                )
                .toString();

    }
}
