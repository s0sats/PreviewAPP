package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 11/09/2020
 * Modificado query para excluir os ticket com form pendente de GPS, pois não podem ser sincronizados.
 */

public class Sql_Act005_011 implements Specification {
    public static final String UPDATE_SYNC_REQUIRED_QTY = "update_sync_required_qty";

    private long customer_code;

    public Sql_Act005_011(long customer_code) {
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
                                "  and c.update_required = 0\n" +
                                "  and NOT EXISTS(SELECT 1\n" +
                                "                      FROM "+ GE_Custom_Form_DataDao.TABLE +" g \n"+
                                "                      WHERE g.customer_code = t.customer_code\n" +
                                "                            and g.ticket_prefix = t.ticket_prefix\n" +
                                "                            and g.ticket_code = t.ticket_code  \n" +
                                "                            and ( " +
                                "                                    (g.ticket_seq > 0 and g.custom_form_status = '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC +"'  and g.location_pendency = 1)\n" +
                                "                                 or ( g.ticket_seq = 0 " +"\n" +
                                "                                  and g.ticket_seq_tmp > 0 " +"\n" +
                                "                                  and g.custom_form_status = '"+ ConstantBaseApp.SYS_STATUS_IN_PROCESSING +"') \n" +
                                "                            ) \n " +
                                ")\n" +
                                "  ;").toString();
    }
}
