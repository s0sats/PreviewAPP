package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sql_Act069_004 implements Specification {
    private long customer_code;
    private String site_logged;
    //
    public Sql_Act069_004(long customer_code, String site_logged) {
        this.customer_code = customer_code;
        this.site_logged = site_logged;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
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
                        "       s.step_desc,\n" +
                        "       s.forecast_start,\n" +
                        "       s.forecast_end\n" +
                        " FROM\n" +
                        "     " + TK_TicketDao.TABLE + " t \n" +
                        " LEFT JOIN\n" +
                        "     " + TK_Ticket_StepDao.TABLE + " s ON \n" +
                        "       t.ticket_code = s.ticket_code \n" +
                        "       AND t.ticket_prefix = s.ticket_prefix\n" +
                        " WHERE\n" +
                        " t.customer_code = '" + customer_code + "'\n" +
                        " and s.step_status != '" + ConstantBaseApp.SYS_STATUS_DONE + "'  \n" +
                        " GROUP BY\n" +
                        "  T.customer_code,\n" +
                        "  t.ticket_prefix,\n" +
                        "  t.ticket_code \n" +
                        " ORDER BY \n" +
                        "  t.forecast_date desc,\n" +
                        "  t.ticket_prefix,\n" +
                        "  t.ticket_code\n"
                )
                .toString();
    }
}
