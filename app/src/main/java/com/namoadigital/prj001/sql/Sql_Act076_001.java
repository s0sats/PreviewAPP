package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

import static com.namoadigital.prj001.dao.TK_Ticket_StepDao.STEP_QTY;

public class Sql_Act076_001 implements Specification {
    private long customer_code;
    private long ticketProductCode;
    private long ticketSerialCode;
    private String site_logged;
    private String serial_filter;
    //
    public Sql_Act076_001(long customer_code, String site_logged) {
        this.customer_code = customer_code;
        this.site_logged = site_logged;
        serial_filter = "";
    }

    public Sql_Act076_001(long customer_code, String site_logged, long ticketProductCode, long ticketSerialCode) {
        this.customer_code = customer_code;
        this.site_logged = site_logged;
        this.ticketProductCode = ticketProductCode;
        this.ticketSerialCode = ticketSerialCode;
        serial_filter = " and t.open_product_code = '" + ticketProductCode + "'\n" +
                        " and t.open_serial_code = '" + ticketSerialCode + "'\n" ;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "       t.customer_code,\n" +
                        "       t.scn,\n" +
                        "       t.ticket_prefix,\n" +
                        "       t.ticket_code,\n" +
                        "       t.ticket_id,\n" +
                        "       t.ticket_status,\n" +
                        "       t.type_path,\n" +
                        "       t.type_desc,\n" +
                        "       t.open_comments,\n" +
                        "       t.open_date,\n" +
                        "       t.current_step_order,\n" +
                        "       t.forecast_date,\n" +
                        "       CASE WHEN t.open_site_code <> '" + site_logged + "' \n" +
                        "            THEN t.open_site_desc\n" +
                        "            ELSE null\n" +
                        "       END open_site_desc ,\n" +
                        "       t.open_product_desc,\n" +
                        "       t.open_serial_id,\n" +
                        "       t.sync_required\n," +
                        "       s.step_code,\n" +
                        "       s.step_id,\n" +
                        "       s.step_desc,\n" +
                        "       s.step_order,\n" +
                        "       s.step_order_seq,\n" +
                        "       min(s.forecast_start) forecast_start,\n" +
                        "       max(s.forecast_end) forecast_end,\n" +
                        "        count(s.step_code) " + STEP_QTY + "\n"+
                        " FROM\n" +
                        "     " + TK_TicketDao.TABLE + " t \n" +
                        " LEFT JOIN\n" +
                        "     " + TK_Ticket_StepDao.TABLE + " s ON \n" +
                        "       t.ticket_code = s.ticket_code \n" +
                        "       AND t.ticket_prefix = s.ticket_prefix\n" +
                        " WHERE\n" +
                        " t.customer_code = '" + customer_code + "'\n" +
                        serial_filter +
                        " and s.step_status != '" + ConstantBaseApp.SYS_STATUS_DONE + "'  \n" +
                        " and t.ticket_status in ('" + ConstantBaseApp.SYS_STATUS_PENDING + "' , '" +
                                                       ConstantBaseApp.SYS_STATUS_PROCESS + "' , '" +
                                                       ConstantBaseApp.SYS_STATUS_WAITING_SYNC +
                        "')  \n" +
                        "and t.current_step_order = s.step_order"+
                        " GROUP BY\n" +
                        "  T.customer_code,\n" +
                        "  t.ticket_prefix,\n" +
                        "  t.ticket_code \n" +
                        " ORDER BY \n" +
                        "  t.user_focus desc,\n" +
                        "  t.forecast_date asc,\n" +
                        "  t.ticket_prefix,\n" +
                        "  t.ticket_code\n"
                )
                .toString();
    }
}
