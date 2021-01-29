package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE 09/09/2020
 * Query que verifica se algum ctrl do step possui form pendencia de GPS
 * LUCHE 10/09/2020
 * Corrigido relacionamento da query evitando multiplicação de resultado.
 */
public class Sql_Act070_007 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int step_code;

    public Sql_Act070_007(long customer_code, int ticket_prefix, int ticket_code, int step_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.step_code = step_code;
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
                        "      d.ticket_prefix =   c.ticket_prefix \n" +
                        "      and d.ticket_code =   c.ticket_code \n" +
                        "      and d.step_code =   c.step_code \n" +
                        "      and d.ticket_seq = c.ticket_seq\n" +
                        "      and d.ticket_seq_tmp = c.ticket_seq_tmp\n" +
                        "\n"+
                        "      and d.customer_code = '" + customer_code +"'\n" +
                        "      and d.ticket_prefix = '" + ticket_prefix +"'\n" +
                        "      and d.ticket_code = '" + ticket_code +"'\n" +
                        "      and d.step_code = '" + step_code +"'\n" +
                        "\n"+
                        "      and c.ctrl_status = " + "'" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"'"+
                        "      and d.custom_form_status = " + "'" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"'"+
                        "      and d.location_pendency = 1 \n" +
                        "\n"
                )
                .toString();
    }
}
