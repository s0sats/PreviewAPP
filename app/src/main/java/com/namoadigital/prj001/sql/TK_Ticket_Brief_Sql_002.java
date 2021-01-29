package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
/**
 *  BARRIONUEVO 14/10/2020
 *  Query que lista os tickts que vieram do proximo ticket mergeando com os locais, mantendo as infos
 *  Query que lista os tickts que vieram do proximo ticket mergeando com os locais, mantendo as infos
 *  do proximos tickets e os controles dos tickets locais.
 *
 */
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
                .append("SELECT  \n" +
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
                        "       tb.step_order_seq,\n" +
                        "       tb.fcm,\n" +
                        "       1 user_focus,\n" +
                        "       t.sync_required,\n" +
                        "       CASE WHEN t.update_required + t.update_required_product > 0 \n" +
                        "            THEN 1\n" +
                        "            ELSE 0\n" +
                        "       END update_required,\n" +
                        " CASE WHEN t.ticket_prefix = null \n" +
                        "            THEN 0\n" +
                        "            ELSE 1\n" +
                        "       END local_ticket\n," +
                        "       tb.client_code,\n" +
                        "       tb.client_name,\n" +
                        "       tb.contract_code,\n" +
                        "       tb.contract_desc\n" +
                        " FROM\n" +
                        "     tk_ticket t \n" +
                        " INNER JOIN tk_ticket_brief tb ON\n" +
                        "      tb.customer_code = t.customer_code\n" +
                        "       AND  tb.ticket_prefix = t.ticket_prefix \n" +
                        "       AND  tb.ticket_code = t.ticket_code  \n" +
                        " WHERE\n" +
                        "       t.customer_code =\n" +customer_code+ "\n" +
                        " and tb.customer_code = t.customer_code\n" +
                        " AND  tb.ticket_prefix = t.ticket_prefix \n" +
                        " AND  tb.ticket_code = t.ticket_code\n" +
                        " \n" +
                        "GROUP BY\n" +
                        "  T.customer_code,\n" +
                        "  t.ticket_prefix,\n" +
                        "  t.ticket_code")
                .toString();
    }
}
