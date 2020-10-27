package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 *  BARRIONUEVO 16-10-2020
 *  CARREGA LISTA DE TICKETS LOCAIS FORMATANDO PARA OS ADAPTER DAS TELAS ACT076
 */
public class Sql_Act076_002 implements Specification {
    private long customer_code;
    private String site_logged;
    private String serial_filter ="";
    private String ticket_filter ="";

    public Sql_Act076_002(long customerCode, String site_logged) {
        this.customer_code = customerCode;
        this.site_logged = site_logged;
        this.serial_filter ="";
        this.ticket_filter = "";
    }
    public Sql_Act076_002(long customerCode, String site_logged, long ticketProductCode, long ticketSerialCode) {
        this.customer_code = customerCode;
        this.site_logged = site_logged;
        if(ticketProductCode > 0
                && ticketSerialCode > 0 ) {
            serial_filter = " and (\n(t.open_product_code = '" + ticketProductCode + "'\n" +
                    " and t.open_serial_code = '" + ticketSerialCode + "')\n" +
                    " or exists ( " +
                    "       select 1 \n" +
                    "              from tk_ticket_ctrl c \n" +
                    "       where s.customer_code = c.customer_code\n" +
                    "       and s.ticket_prefix = c.ticket_prefix\n" +
                    "       and s.ticket_code = c.ticket_code \n" +
                    "       and s.step_code = c.step_code \n" +
                    " and c.ctrl_status in ('" + ConstantBaseApp.SYS_STATUS_PENDING + "',  \n" +
                    " '" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC + "',  \n" +
                    " '" + ConstantBaseApp.SYS_STATUS_PROCESS + "' ) \n" +
                    ") \n )";
        }
    }

    public Sql_Act076_002(long customer_code, String site_code, long ticketProductCode, long ticketSerialCode, String ticketContractId, String ticketClientId, String ticketId) {
        this.customer_code = customer_code;
        this.site_logged = site_logged;
        if(ticketProductCode > 0
                && ticketSerialCode > 0 ) {
            serial_filter = " and (\n(t.open_product_code = '" + ticketProductCode + "'\n" +
                    " and t.open_serial_code = '" + ticketSerialCode + "')\n" +
                    " or exists ( " +
                    "       select 1 \n" +
                    "              from tk_ticket_ctrl c \n" +
                    "       where s.customer_code = c.customer_code\n" +
                    "       and s.ticket_prefix = c.ticket_prefix\n" +
                    "       and s.ticket_code = c.ticket_code \n" +
                    "       and s.step_code = c.step_code \n" +
                    " and c.ctrl_status in ('" + ConstantBaseApp.SYS_STATUS_PENDING + "',  \n" +
                    " '" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC + "',  \n" +
                    " '" + ConstantBaseApp.SYS_STATUS_PROCESS + "' ) \n" +
                    ") \n )";
        }
        if((ticketContractId != null && !ticketContractId.isEmpty())
            || (ticketClientId != null && !ticketClientId.isEmpty())
            || (ticketId != null && !ticketId.isEmpty())
        ){
            this.ticket_filter += ticketContractId != null && !ticketContractId.isEmpty() ? " and t.contract_id = '"+ticketContractId+"' \n" : "";
            this.ticket_filter += ticketClientId != null && !ticketClientId.isEmpty() ? " and t.client_id = '"+ticketClientId+"' \n"  : "";
            this.ticket_filter += ticketId != null && !ticketId.isEmpty() ? " and t.ticket_id = '"+ticketId+"' \n" : "";
        }
    }


    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append("SELECT t.customer_code,\n" +
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
                        "       t.forecast_date, \n " +
                        "       CASE WHEN t.user_focus = 0 "+
                        "       THEN t.forecast_date "+
                        "       ELSE s.forecast_start"+
                        "       END  forecast_start,\n" +
                        "       CASE WHEN t.user_focus = 0 "+
                        "       THEN null  " +
                        "       ELSE s.forecast_end"+
                        "       END  forecast_end,\n" +
                        "       count(s.step_code) STEP_QTY, \n" +
                        "       s.step_order_seq,\n" +
                        "       0 fcm,\n" +
                        "       t.user_focus,\n" +
                        "       t.sync_required,\n" +
                        "       CASE WHEN t.update_required + t.update_required_product > 0 \n" +
                        "            THEN 1\n" +
                        "            ELSE 0\n" +
                        "       END update_required,\n" +
                        "       1 local_ticket, \n" +
                        "       t.client_code,\n" +
                        "       t.client_name,\n" +
                        "       t.contract_code,\n" +
                        "       t.contract_desc\n" +
                        " FROM\n" +
                        "     tk_ticket t\n" +
                        " JOIN\n" +
                        "     tk_ticket_step s ON \n" +
                        "           t.customer_code = s.customer_code \n" +
                        "       AND t.ticket_code = s.ticket_code \n" +
                        "       AND t.ticket_prefix = s.ticket_prefix\n" +
                        " WHERE\n" +
                        "     t.customer_code =\n" +customer_code+ "\n" +
                        serial_filter +
                        ticket_filter +
                        " and s.step_status in ('" + ConstantBaseApp.SYS_STATUS_PENDING + "',  \n" +
                        " '" + ConstantBaseApp.SYS_STATUS_PROCESS + "' ) \n" +
                        " and t.ticket_status in ('" + ConstantBaseApp.SYS_STATUS_PENDING + "' , '" +
                        ConstantBaseApp.SYS_STATUS_PROCESS + "' , '" +
                        ConstantBaseApp.SYS_STATUS_WAITING_SYNC +
                        "')  \n" +
                        " and t.current_step_order = s.step_order\n" +
                        " and t.customer_code = s.customer_code\n" +
                        " and t.ticket_code = s.ticket_code \n" +
                        " AND t.ticket_prefix = s.ticket_prefix\n" +
                        "GROUP BY\n" +
                        "  t.customer_code,\n" +
                        "  t.ticket_prefix,\n" +
                        "  t.ticket_code" +
                        " ORDER BY \n" +
                        "  forecast_start asc\n"
                )
                .toString();
    }
}
