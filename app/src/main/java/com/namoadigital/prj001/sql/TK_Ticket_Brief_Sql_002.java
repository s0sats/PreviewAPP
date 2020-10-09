package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class TK_Ticket_Brief_Sql_002 implements Specification {
    private long customer_code;
    private String site_logged;

    public TK_Ticket_Brief_Sql_002(long customerCode, String site_logged) {
        this.customer_code = customerCode;
        this.site_logged = site_logged;
    }


    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append("\tSELECT \n" +
                        "   tb.customer_code,\n" +
                        "       tb.scn,\n" +
                        "       tb.ticket_prefix,\n" +
                        "       tb.ticket_code,\n" +
                        "       tb.ticket_id,\n" +
                        "       CASE WHEN tb.open_site_code <> '" + site_logged + "' \n" +
                        "            THEN tb.open_site_desc\n" +
                        "            ELSE null\n" +
                        "       END open_site_desc, \n" +
                        "       tb.open_product_desc,\n" +
                        "       tb.open_serial_id,\n" +
                        "       tb.current_step_order,\n" +
                        "       tb.ticket_status,\n" +
                        "       tb.origin_desc,\n" +
                        "       tb.step_desc,\n" +
                        "       tb.forecast_start,\n" +
                        "       tb.forecast_end,\n" +
                        "       tb.step_count,\n" +
                        "       tb.fcm,\n" +
                        "        CASE WHEN t.user_focus is null \n" +
                        "            THEN 1\n" +
                        "            ELSE t.user_focus\n" +
                        "        END user_focus, \n" +
                        "        CASE WHEN t.sync_required is null \n" +
                        "            THEN 0\n" +
                        "            ELSE t.sync_required\n" +
                        "        END sync_required,\n" +
                        "        CASE WHEN t.update_required is null \n" +
                        "            THEN 0\n" +
                        "            ELSE t.update_required\n" +
                        "        END update_required,\n" +
                        "        \"Ticket_Brief outer\"\n" +
                        " FROM tk_ticket_brief  tb\n" +
                        " left join  tk_ticket t \n" +
                        "      on t.customer_code =  tb.customer_code \n" +
                        "     and t.ticket_prefix =  tb.ticket_prefix\n" +
                        "     and t.ticket_code  =  tb.ticket_code\n" +
                        " WHERE  \n" +
                        "       tb.customer_code =\n" +customer_code+ "\n" +
                        "       and tb.fcm = 0\n" +
                        "       and t.ticket_code is null\n" +
                        "\n" +
                        "union\n" +
                        "SELECT  \n" +
                        "tb.customer_code,\n" +
                        "       tb.scn,\n" +
                        "       tb.ticket_prefix,\n" +
                        "       tb.ticket_code,\n" +
                        "       tb.ticket_id,\n" +
                        "       CASE WHEN tb.open_site_code <> '" + site_logged + "' \n" +
                        "            THEN tb.open_site_desc\n" +
                        "            ELSE null\n" +
                        "       END open_site_desc ,\n" +
                        "       tb.open_product_desc,\n" +
                        "       tb.open_serial_id,\n" +
                        "       tb.current_step_order,\n" +
                        "       tb.ticket_status,\n" +
                        "       tb.origin_desc,\n" +
                        "       tb.step_desc,\n" +
                        "       tb.forecast_start,\n" +
                        "       tb.forecast_end,\n" +
                        "       tb.step_count STEP_QTY, \n" +
                        "       tb.fcm,\n" +
                        "       t.user_focus,\n" +
                        "       t.sync_required,\n" +
                        "       CASE WHEN t.update_required + t.update_required_product > 0 \n" +
                        "            THEN 1\n" +
                        "            ELSE 0\n" +
                        "       END update_required,\n" +
                        "       \"JOIN\"\n" +
                        " FROM\n" +
                        "     tk_ticket t \n" +
                        "INNER JOIN tk_ticket_brief tb ON\n" +
                        "      tb.customer_code = t.customer_code\n" +
                        "       AND  tb.ticket_prefix = t.ticket_prefix \n" +
                        "       AND  tb.ticket_code = t.ticket_code  \n" +
                        " WHERE\n" +
                        "       t.customer_code =\n" +customer_code+ "\n" +
                        " and tb.customer_code = t.customer_code\n" +
                        " AND  tb.ticket_prefix = t.ticket_prefix \n" +
                        " AND  tb.ticket_code = t.ticket_code\n" +
                        " \n" +
                        "union\n" +
                        "\n" +
                        "SELECT distinct  t.customer_code,\n" +
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
                        "      t.origin_desc,\n" +
                        "       s.step_desc,\n" +
                        "       s.forecast_start,\n" +
                        "       s.forecast_end,\n" +
                        "       count(s.step_code) STEP_QTY, \n" +
                        "      0 fcm,\n" +
                        "       t.user_focus,\n" +
                        "       t.sync_required,\n" +
                        "       CASE WHEN t.update_required + t.update_required_product > 0 \n" +
                        "            THEN 1\n" +
                        "            ELSE 0\n" +
                        "       END update_required,\n" +
                        "       \"Ticket outer\" \n" +
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
