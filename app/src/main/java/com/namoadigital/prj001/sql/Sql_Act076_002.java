package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sql_Act076_002 implements Specification {
    private long customer_code;
    private long ticketProductCode;
    private long ticketSerialCode;
    private String site_logged;
    private String serial_filter ="";

    public Sql_Act076_002(long customerCode, String site_logged, long ticketProductCode, long ticketSerialCode) {
        this.customer_code = customerCode;
        this.site_logged = site_logged;
        if(ticketProductCode > 0
        && ticketSerialCode > 0 ) {
            serial_filter = " and (\n(t.open_product_code = '" + ticketProductCode + "'\n" +
                    " and t.open_serial_code = '" + ticketSerialCode + "')\n" +
                    " or( c.product_code = '" + ticketProductCode + "'\n" +
                    " and c.serial_code = '" + ticketSerialCode + "')\n)\n";
        }
    }


    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append("SELECT distinct  t.customer_code,\n" +
                        "       t.scn,\n" +
                        "       t.ticket_prefix,\n" +
                        "       t.ticket_code,\n" +
                        "       t.ticket_id,\n" +
                        "       CASE WHEN t.open_site_code <> '" + site_logged + "' \n" +
                        "            THEN t.open_site_desc\n" +
                        "            ELSE null\n" +
                        "       END open_site_desc ,\n" +
                        "       t.open_product_desc,\n" +
                        "       t.open_serial_id,\n" +
                        "       t.current_step_order,\n" +
                        "       t.ticket_status,\n" +
                        "       t.origin_desc,\n" +
                        "       s.step_desc,\n" +
                        "       CASE WHEN t.user_focus = 0 "+
                        "       THEN t.forecast_date"+
                        "       ELSE s.forecast_start"+
                        "       END  forecast_start,\n" +
                        "       CASE WHEN t.user_focus = 0 "+
                        "       THEN null  " +
                        "       ELSE s.forecast_end"+
                        "       END  forecast_end,\n" +
                        "       count(s.step_code) STEP_QTY, \n" +
                        "      0 fcm,\n" +
                        "       t.user_focus,\n" +
                        "       t.sync_required,\n" +
                        "       CASE WHEN t.update_required + t.update_required_product > 0 \n" +
                        "            THEN 1\n" +
                        "            ELSE 0\n" +
                        "       END update_required,\n" +
                        "       1 local_ticket \n" +
                        " FROM\n" +
                        "     tk_ticket t,\n" +
                        "     tk_ticket_ctrl c\n" +
                        " LEFT JOIN\n" +
                        "     tk_ticket_step s ON \n" +
                        "       t.ticket_code = s.ticket_code \n" +
                        "       AND t.ticket_prefix = s.ticket_prefix\n" +
                        "   LEFT JOIN\n" +
                        "     tk_ticket_brief tb ON\n" +
                        "      tb.customer_code = t.customer_code\n" +
                        "       AND  tb.ticket_prefix = t.ticket_prefix \n" +
                        "       AND tb.ticket_code = t.ticket_code       \n" +
                        " WHERE\n" +
                        "     t.customer_code =\n" +customer_code+ "\n" +
                                serial_filter +
                        " and s.step_status != '" + ConstantBaseApp.SYS_STATUS_DONE + "'  \n" +
                        " and t.ticket_status in ('" + ConstantBaseApp.SYS_STATUS_PENDING + "' , '" +
                        ConstantBaseApp.SYS_STATUS_PROCESS + "' , '" +
                        ConstantBaseApp.SYS_STATUS_WAITING_SYNC +
                        "')  \n" +
                        " and t.current_step_order = s.step_order\n" +
                        " and t.ticket_code = s.ticket_code \n" +
                        " AND t.ticket_prefix = s.ticket_prefix\n" +
                        " and s.ticket_prefix = c.ticket_prefix \n" +
                        " AND s.ticket_code = c.ticket_code \n" +
                        " AND s.step_code = c.step_code \n" +
                        " and  tb.ticket_prefix is null\n" +
                        "GROUP BY\n" +
                        "  T.customer_code,\n" +
                        "  t.ticket_prefix,\n" +
                        "  t.ticket_code")
                .toString();
    }
}
