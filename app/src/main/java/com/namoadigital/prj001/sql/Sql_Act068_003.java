package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 02/09/2020
 * Query que verifica se algum ticket possui form pendente de envio
 * LUCHE - 10/09/2020
 * Corrigido relacionamento da query evitando multiplicação de resultado.
 */

public class Sql_Act068_003 implements Specification {
    private long customer_code;

    public Sql_Act068_003(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        sb
            .append(" SELECT\n" +
                    "    d.*\n" +
                    " FROM\n" +
                    "   " + GE_Custom_Form_DataDao.TABLE +"  d,\n" +
                    "   " + TK_TicketDao.TABLE +"  t,\n" +
                    "   " + TK_Ticket_CtrlDao.TABLE +"  c\n" +
                    " WHERE\n" +
                    "      t.ticket_prefix = c.ticket_prefix \n" +
                    "      and t.ticket_code = c.ticket_code \n" +
                    "\n" +
                    "      and d.ticket_prefix = c.ticket_prefix \n" +
                    "      and d.ticket_code = c.ticket_code\n" +
                    "      and d.step_code =   c.step_code \n" +
                    "      and d.ticket_seq = c.ticket_seq\n" +
                    "      and d.ticket_seq_tmp = c.ticket_seq_tmp\n" +
                    "\n" +
                    "      and d.customer_code = '" + customer_code +"'\n" +
                    "      and d.custom_form_status = " + "'" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"'\n"+
                    "      and t.ticket_status in " + "(\n" +
                    "                                       '" + ConstantBaseApp.SYS_STATUS_PENDING+"',\n" +
                    "                                       '" + ConstantBaseApp.SYS_STATUS_PROCESS+"',\n" +
                    "                                       '" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"'\n" +
                    "                                   )"+
                    "      and c.ctrl_status = " + "'" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"'\n"
            );
        return sb.toString();
    }
}
