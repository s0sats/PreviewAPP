package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sql_Act069_001 implements Specification {
    private long customer_code;
    private String site_logged;
    private String statusFilter = "";
    private String partnerFilter = "";
    private String serialFilter = "";

    public Sql_Act069_001(long customer_code, String site_logged, boolean bStatusPending, boolean bStatusProcess, boolean bStatusWaitingSync, boolean bStatusDone, boolean bParterEmpty, boolean bParterProfile, long ticketProductCode, long ticketSerialCode) {
        this.customer_code = customer_code;
        this.site_logged = site_logged;
        //
        if (bStatusDone) {
            statusFilter = "    and t.ticket_status in('" + ConstantBaseApp.SYS_STATUS_DONE + "'" +
                ",'" + ConstantBaseApp.SYS_STATUS_NOT_EXECUTED + "'" +
                ",'" + ConstantBaseApp.SYS_STATUS_CANCELLED + "'" +
                ",'" + ConstantBaseApp.SYS_STATUS_REJECTED + "') \n";
        } else {
            if (bStatusPending || bStatusProcess || bStatusWaitingSync) {
                /*statusFilter = "   and t.ticket_status in(";
                statusFilter += bStatusPending ? "'"+ConstantBaseApp.SYS_STATUS_PENDING +"' ":"";
                statusFilter += bStatusPending && bStatusProcess ? " , ":"";
                statusFilter += bStatusProcess ? "'"+ConstantBaseApp.SYS_STATUS_PROCESS +"' ":"";
                statusFilter += " )\n";*/
                //
                statusFilter = "   and t.ticket_status in(";
                statusFilter += bStatusPending ? "'" + ConstantBaseApp.SYS_STATUS_PENDING + "', " : "";
                statusFilter += bStatusProcess ? "'" + ConstantBaseApp.SYS_STATUS_PROCESS + "', " : "";
                statusFilter += bStatusWaitingSync ? "'" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC + "', " : "";
                statusFilter = statusFilter.substring(0, statusFilter.length() - ", ".length());
                statusFilter += " )\n";

            } else {
                statusFilter = "   and t.ticket_status <> '" + ConstantBaseApp.SYS_STATUS_DONE + "'\n";
            }
            //LUCHE - 04/02/2020
            //Após implementação da busca de ticket por serial, foi necessario criar o filtro de prod/serial
            //para que na lista pós download sejam exibidos somente os ticket relacionados ao serial.
            if (ticketProductCode > 0 && ticketSerialCode > 0) {
                serialFilter = "     and exists (SELECT 1\n" +
                    "                 FROM " + TK_Ticket_CtrlDao.TABLE + " c\n" +
                    "                 WHERE c.customer_code = t.customer_code\n" +
                    "                       and c.ticket_prefix = t.ticket_prefix\n" +
                    "                       and c.ticket_code = t.ticket_code\n" +
                    "                       and c.product_code = '" + ticketProductCode + "'\n" +
                    "                       and c.serial_code = '" + ticketSerialCode + "'\n" +
                    "                )\n";

            }
            //
            String partnerCondition = "";
            //Filtro de null sempre existe, variando entre is null e is NOT null
            partnerCondition += bParterEmpty ? " c.partner_code is null " : " c.partner_code is NOT null ";
            //Filtro de somente meus parceiros só existe se true e varia entre OR se for junto com o filtro is null ou AND se filtro is not null
            partnerCondition += bParterProfile ? (bParterEmpty ? " or m.partner_code is not null " : " and m.partner_code is not null ") : "";
            //
            partnerFilter += "  and (     \n" +
                "         (SELECT\n" +
                "            SUM(\n" +
                "              CASE WHEN " + partnerCondition + " \n" +
                "                   THEN 1\n" +
                "                   ELSE 0\n" +
                "              END\n" +
                "            ) PARTNER_FILTER\n" +
                "          FROM\n" +
                "            " + TK_Ticket_CtrlDao.TABLE + " c\n" +
                "          \n" +
                "          LEFT JOIN\n" +
                "                md_partners m on m.customer_code = c.customer_code \n" +
                "                                 and m.partner_code = c.partner_code\n" +
                "            \n" +
                "          WHERE     \n" +
                "            c.customer_code = t.customer_code\n" +
                "            and c.ticket_prefix = t.ticket_prefix\n" +
                "            and c.ticket_code = t.ticket_code\n" +
                "          GROUP BY  \n" +
                "            c.customer_code,\n" +
                "            c.ticket_prefix,\n" +
                "            c.ticket_code) > 0\n" +
                "        )\n";
        }
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
                "       t.forecast_date,\n" +
                "       CASE WHEN t.current_site_code <> '" + site_logged + "' \n" +
                "            THEN t.current_site_desc\n" +
                "            ELSE null\n" +
                "       END current_site_desc ,\n" +
                "       t.current_product_desc,\n" +
                "       t.current_serial_id,\n" +
                "       t.sync_required\n," +
                "       CASE WHEN t.schedule_prefix IS NOT NULL AND t.schedule_code IS NOT NULL AND t.schedule_exec IS NOT NULL \n" +
                "            THEN t.schedule_prefix||'.'||t.schedule_code||'.'||t.schedule_exec \n" +
                "            ELSE null \n" +
                "       END " + MD_Schedule_ExecDao.SCHEDULE_PK + " \n," +
                "       t.schedule_prefix,\n" +
                "       t.schedule_code,\n" +
                "       t.schedule_exec\n" +
                " FROM\n" +
                "     " + TK_TicketDao.TABLE + " t \n" +
                " WHERE\n" +
                " t.customer_code = '" + customer_code + "'\n" +
                statusFilter +
                serialFilter +
                partnerFilter +
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
