package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 *  BARRIONUEVO 14/10/2020
 *  Query que lista os tickts que vieram do proximo ticket, excluindo os tickets locais.
 *
 */
public class TK_Ticket_Brief_Sql_004 implements Specification {
    private long customer_code;
    private String site_logged;

    public TK_Ticket_Brief_Sql_004(long customerCode, String site_logged) {
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
                        "       tb.step_order_seq,\n" +
                        "       tb.fcm,\n" +
                        "        1 user_focus, \n" +
                        "        CASE WHEN t.sync_required is null \n" +
                        "            THEN 0\n" +
                        "            ELSE t.sync_required\n" +
                        "        END sync_required,\n" +
                        "        CASE WHEN t.update_required is null \n" +
                        "            THEN 0\n" +
                        "            ELSE t.update_required\n" +
                        "        END update_required,\n" +
                        "        0 local_ticket," +
                        "       tb.client_code,\n" +
                        "       tb.client_name,\n" +
                        "       tb.contract_code,\n" +
                        "       tb.contract_desc\n" +
                        " FROM tk_ticket_brief  tb\n" +
                        " left join  tk_ticket t \n" +
                        "      on t.customer_code =  tb.customer_code \n" +
                        "     and t.ticket_prefix =  tb.ticket_prefix\n" +
                        "     and t.ticket_code  =  tb.ticket_code\n" +
                        " WHERE  \n" +
                        "       tb.customer_code =\n" + customer_code + "\n" +
                        "       and tb.fcm = 0\n" +
                        "       and t.ticket_code is null\n" +
                        " GROUP BY\n" +
                        "       tb.customer_code,\n" +
                        "       tb.ticket_prefix,\n" +
                        "       tb.ticket_code " +
                        "\n")
                .toString();
    }
}
