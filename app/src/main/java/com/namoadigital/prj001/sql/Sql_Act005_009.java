package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * BARRIONUEVO
 * Contador de tickets por status.
 */
public class Sql_Act005_009 implements Specification{
    public static final String PENDING_QTY = "pending_qty";
    private String status_filtered;
    private long customer_code;

    public Sql_Act005_009(long customer_code, boolean statusProcess, boolean statusPending, boolean statusWaitingSync, boolean statusDone, boolean statusSyncRequired, boolean statusUpdateRequired, boolean hasUserFocus) {
        this.customer_code = customer_code;
        if(statusPending || statusProcess || statusWaitingSync || statusDone) {
            status_filtered = "\n   and s.ticket_status in (";
            status_filtered += statusPending ? "'" + ConstantBaseApp.SYS_STATUS_PENDING + "', " : "";
            status_filtered += statusProcess ? "'" + ConstantBaseApp.SYS_STATUS_PROCESS + "', " : "";
            status_filtered += statusWaitingSync ? "'" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC + "', " : "";
            status_filtered += statusDone ? "'" + ConstantBaseApp.SYS_STATUS_DONE + "', " : "";
            status_filtered = status_filtered.substring(0, status_filtered.length() - ", ".length());
            status_filtered = status_filtered + " )";
        }
        if(statusSyncRequired){
            status_filtered = "\n and s.sync_required = 1";
        }
        if(statusUpdateRequired){
            status_filtered = "\n and s.update_required = 1";
        }
        if(hasUserFocus){
            status_filtered += "\n and s.user_focus = 1";
        }
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append( "SELECT\n" +
                        "     count(1) "+PENDING_QTY+"\n " +
                        " FROM\n" +
                        TK_TicketDao.TABLE +" s\n" +
                        " WHERE \n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "  and s.ticket_prefix > 0 \n" +
                        status_filtered +
                        "; \n")
                .toString();
    }
}
