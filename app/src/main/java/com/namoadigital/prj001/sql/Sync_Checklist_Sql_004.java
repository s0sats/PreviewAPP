package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_FormDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sync_Checklist_Sql_004 implements Specification {
    private long customer_code;

    public Sync_Checklist_Sql_004(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();
        sb.append("SELECT\n" +
                " distinct c.product_code\n" +
                "FROM\n" +
                TK_TicketDao.TABLE +" t,\n" +
                TK_Ticket_StepDao.TABLE + " s,\n" +
                TK_Ticket_CtrlDao.TABLE + " c,\n" +
                TK_Ticket_FormDao.TABLE +"  f\n" +
                "WHERE\n" +
                " t.customer_code = " +customer_code + "\n" +
                " and t.customer_code = s.customer_code\n" +
                " and t.ticket_prefix = s.ticket_prefix\n" +
                " and t.ticket_code = s.ticket_code\n" +
                " and t.current_step_order = s.step_order\n" +
                " and s.ticket_prefix = c.ticket_prefix\n" +
                " and s.ticket_code = c.ticket_code\n" +
                " and s.customer_code = c.customer_code\n" +
                " and s.step_code = c.step_code\n" +
                " and c.customer_code = f.customer_code\n" +
                " and c.ticket_prefix = f.ticket_prefix\n" +
                " and c.ticket_code = f.ticket_code\n" +
                " and c.ticket_seq_tmp = f.ticket_seq_tmp\n" +
                " and s.step_code = f.step_code\n" +
                " and t.ticket_status in ('"+ ConstantBase.SYS_STATUS_PENDING +"','"+ ConstantBase.SYS_STATUS_PROCESS+ "')\n" +
                " and s.step_status in ('"+ ConstantBase.SYS_STATUS_PENDING +"','"+ ConstantBase.SYS_STATUS_PROCESS+ "')\n" +
                " and c.ctrl_status in ('"+ ConstantBase.SYS_STATUS_PENDING +"','"+ ConstantBase.SYS_STATUS_PROCESS+ "')\n" +
                " and c.ctrl_type = '"+ ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM + "' \n" +
                " and (not exists(SELECT 1\n" +
                "                FROM sync_checklist sc\n" +
                "                WHERE sc.customer_code = c.customer_code\n" +
                "                     and sc.product_code = c.product_code)\n" +
                "     or not exists(SELECT 1\n" +
                        " FROM\n" +
                        "   " + GE_Custom_FormDao.TABLE +"  ge\n" +
                        " WHERE\n" +
                        "      f.customer_code = ge.customer_code \n" +
                        "      and f.custom_form_type = ge.custom_form_type \n" +
                        "      and f.custom_form_code = ge.custom_form_code \n" +
                        "      and f.custom_form_version = ge.custom_form_version \n" +
                "               )" +
                ")");
        //"customer_code#product_code#last_update");

        return sb.toString();
    }
}
