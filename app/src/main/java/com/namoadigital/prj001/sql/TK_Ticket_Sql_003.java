package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE
 * Atualiza syn_required
 * LUCHE - 16/12/2020
 * Modificado query para que , caso o SCN recebido seja != de 0, seta tb a atualização do  FCM_SCN
 */
public class TK_Ticket_Sql_003 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int scn;

    public TK_Ticket_Sql_003(long customer_code, int ticket_prefix, int ticket_code, int scn) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.scn = scn;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        String sResp = "";
        String updateFCM_Scn = "";

        if (scn != 0) {
            sResp = "  and scn < '" + scn + "'";
            updateFCM_Scn = ", fcm_scn = '" + scn + "'\n";
        }

        return sb
                .append(
                          " UPDATE " + TK_TicketDao.TABLE + " set\n" +
                          "   sync_required = '" + "1" + "'\n" +
                              updateFCM_Scn +
                          " WHERE\n" +
                          "  customer_code = '" + customer_code + "'\n" +
                          "  and ticket_prefix = '" + ticket_prefix + "'\n" +
                          "  and ticket_code = '" + ticket_code + "'\n")
                .append(sResp)
                .toString();
    }
}
