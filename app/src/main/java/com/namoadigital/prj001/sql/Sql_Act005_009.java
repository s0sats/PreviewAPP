package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sql_Act005_009 implements Specification{
    public static final String PENDING_QTY = "pending_qty";
    private String status_filtered;
    private long customer_code;
    boolean statusProcess;
    boolean statusPending;
    boolean statusWaitingSync;
    boolean statusDone;

    public Sql_Act005_009(long customer_code, boolean statusProcess, boolean statusPending, boolean statusWaitingSync, boolean statusDone) {
        this.customer_code = customer_code;
        this.statusProcess = statusProcess;
        this.statusPending = statusPending;
        this.statusWaitingSync = statusWaitingSync;
        this.statusDone = statusDone;
        status_filtered = "   and s.ticket_status in (";
        status_filtered += statusPending ? "'"+ ConstantBaseApp.SYS_STATUS_PENDING +"', ":"";
        status_filtered += statusProcess ? "'"+ConstantBaseApp.SYS_STATUS_PROCESS +"', ":"";
        status_filtered += statusWaitingSync ? "'"+ConstantBaseApp.SYS_STATUS_WAITING_SYNC +"', ":"";
        status_filtered += statusDone ? "'"+ConstantBaseApp.SYS_STATUS_DONE +"', ":"";
        status_filtered = status_filtered.substring(0,status_filtered.length() - ", ".length());
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
                        status_filtered +
                        " ); \n")
                .toString();
    }
}
