package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 30/07/2020
 * Seleciona todos os ticket que tenham update required no ticket, step, product ou ctrls.
 * LUCHE - 17/09/2020
 * Modificado query para não selecionar tickest que tenham form compendencia de gps
 */

public class Sql_WS_TK_Ticket_Save_001 implements Specification {

    private long customer_code;

    public Sql_WS_TK_Ticket_Save_001(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "    t.*\n" +
                    " FROM\n" +
                        TK_TicketDao.TABLE + " t,\n" +
                        TK_Ticket_StepDao.TABLE + " s \n" +
                    " \n" +
                    " LEFT JOIN\n" +
                        TK_Ticket_CtrlDao.TABLE + " c on s.customer_code = c.customer_code\n" +
                    "                        and s.ticket_prefix = c.ticket_prefix\n" +
                    "                        and s.ticket_code = c.ticket_code  \n" +
                    "                        and s.step_code = c.step_code\n" +
                    "                      \n" +
                    " WHERE\n" +
                    "      t.customer_code = s.customer_code\n" +
                    "      and t.ticket_prefix = s.ticket_prefix\n" +
                    "      and t.ticket_code = s.ticket_code \n" +
                    "\n" +
                    "      and t.customer_code = '"+customer_code+"' \n" +
                    "      and \n" +
                    "          ( t.update_required = 1 \n" +
                    "            OR t.update_required_product = 1\n" +
                    "            OR s.update_required = 1\n" +
                    "            OR c.update_required = 1\n" +
                    "           )\n" +
                    "      and not exists( SELECT 1\n" +
                    "                      FROM "+ GE_Custom_Form_DataDao.TABLE +" g\n" +
                    "                      WHERE g.customer_code = t.current_step_order\n" +
                    "                            and g.ticket_prefix = t.ticket_prefix\n" +
                    "                            and g.ticket_code = t.ticket_code  \n" +
                    "                            and g.custom_form_status = '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC +"'\n" +
                    "                            and g.location_pendency = 1  \n" +
                    "                     )  " +
                    " GROUP BY\n" +
                    "     t.customer_code,\n" +
                    "     t.ticket_prefix,\n" +
                    "     t.ticket_code\n")
            .toString();
    }
}
