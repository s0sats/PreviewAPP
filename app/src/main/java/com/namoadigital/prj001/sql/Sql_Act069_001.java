package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 25/03/2020
 * Modificado query do para retornar tb as informações de fcm e error_msg
 * LUCHE - 31/03/2020
 * Modificado query para identificar fluxo de historico baseado no isHistoricalShown e possibilitar
 * o filtros dos status de historico.
 *
 * LUCHE - 02/04/2020
 * Modificado query criando novo construtor e alterando logica dos filtros e adicionando o terceiro filtro de parceiro.
 * Regras:
 *  Sem Parceiro: Somente se todos os controles tiver sem parceiro - 100% sem parceiro
 *  Com meu parceiro: Mostrar se ao menos um dos ctrls for de um parceiro.
 *  Sem meu parceiro: Mostrar somente se 100% dos controles não for o meu.
 *
 *  LUCHE - 22/07/2020
 *  Modificado query pois os campos current_ foram renomeados para open_
 */

public class Sql_Act069_001 implements Specification {
    private long customer_code;
    private String site_logged;
    private String statusFilter = "";
    private String partnerFilter = "";
    private String serialFilter = "";
    //
    public Sql_Act069_001(long customer_code, String site_logged, boolean isHistoricalShown, boolean bStatusPending, boolean bStatusProcess, boolean bStatusWaitingSync, boolean bStatusDone, long ticketProductCode, long ticketSerialCode, boolean bStatusNotExecuted, boolean bStatusIgnored, boolean bStatusCanceled, boolean bStatusRejected, boolean bParterEmpty, boolean bParterProfile, boolean bParterNoProfile) {
        this.customer_code = customer_code;
        this.site_logged = site_logged;
        //LUCHE - 31/03/2020
        //Agora o historico é definido pela var isHistoricalShown, pois haverá filtro de status no historico.
        if (isHistoricalShown) {
            if (bStatusDone || bStatusNotExecuted || bStatusIgnored || bStatusCanceled || bStatusRejected) {
                statusFilter = "   and t.ticket_status in(";
                statusFilter += bStatusDone ? "'" + ConstantBaseApp.SYS_STATUS_DONE + "', " : "";
                statusFilter += bStatusNotExecuted ? "'" + ConstantBaseApp.SYS_STATUS_NOT_EXECUTED + "', " : "";
                statusFilter += bStatusIgnored ? "'" + ConstantBaseApp.SYS_STATUS_IGNORED + "', " : "";
                statusFilter += bStatusCanceled ? "'" + ConstantBaseApp.SYS_STATUS_CANCELLED + "', " : "";
                statusFilter += bStatusRejected ? "'" + ConstantBaseApp.SYS_STATUS_REJECTED + "', " : "";
                statusFilter = statusFilter.substring(0, statusFilter.length() - ", ".length());
                statusFilter += " )\n";

            } else {
                statusFilter = "   and t.ticket_status in (\n" +
                    "                           '" + ConstantBaseApp.SYS_STATUS_DONE + "',\n" +
                    "                           '" + ConstantBaseApp.SYS_STATUS_NOT_EXECUTED + "',\n" +
                    "                           '" + ConstantBaseApp.SYS_STATUS_IGNORED +"',\n" +
                    "                           '" + ConstantBaseApp.SYS_STATUS_CANCELLED +"',\n" +
                    "                           '" + ConstantBaseApp.SYS_STATUS_REJECTED +"'\n" +
                    "                           )\n";
            }
        } else {
            if (bStatusPending || bStatusProcess || bStatusWaitingSync) {
                statusFilter = "   and t.ticket_status in(";
                statusFilter += bStatusPending ? "'" + ConstantBaseApp.SYS_STATUS_PENDING + "', " : "";
                statusFilter += bStatusProcess ? "'" + ConstantBaseApp.SYS_STATUS_PROCESS + "', " : "";
                statusFilter += bStatusWaitingSync ? "'" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC + "', " : "";
                statusFilter = statusFilter.substring(0, statusFilter.length() - ", ".length());
                statusFilter += " )\n";

            } else {
                statusFilter = "   and t.ticket_status in (\n" +
                               "                           '" + ConstantBaseApp.SYS_STATUS_PENDING + "',\n" +
                               "                           '" + ConstantBaseApp.SYS_STATUS_PROCESS + "',\n" +
                               "                           '" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC +"'\n" +
                               "                           )\n";
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
            if(bParterEmpty || bParterProfile || bParterNoProfile){
                String partnerCondition = "";
                if(bParterEmpty){
                    partnerCondition +=
                        "       (SELECT\n" +
                        "            sum (CASE WHEN c.partner_code is not null \n" +
                        "                   THEN 1\n" +
                        "                   ELSE 0\n" +
                        "                 END\n" +
                        "              ) PARTNER_FILTER\n" +
                        "       FROM " + TK_Ticket_CtrlDao.TABLE + " c\n" +
                        "       WHERE c.customer_code = t.customer_code\n" +
                        "             and c.ticket_prefix = t.ticket_prefix\n" +
                        "             and c.ticket_code = t.ticket_code" +
                        "             and c.ctrl_status in (\n" +
                        "                 '" + ConstantBaseApp.SYS_STATUS_PENDING + "',\n" +
                        "                 '" + ConstantBaseApp.SYS_STATUS_PROCESS + "',\n" +
                        "                 '" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC +"'\n" +
                        "                 )\n"+
                        "           ) = 0\n";
                }
                if(bParterProfile){
                    partnerCondition += partnerCondition.length() == 0 ? partnerCondition : "   OR  ";
                    partnerCondition +=
                        "     (SELECT\n" +
                        "           count(1)\n" +
                        "      FROM " +
                        "       "+ TK_Ticket_CtrlDao.TABLE +" c,\n" +
                        "       "+ MD_PartnerDao.TABLE +" m \n" +
                        "  \n" +
                        "      WHERE c.customer_code = t.customer_code\n" +
                        "           and c.ticket_prefix = t.ticket_prefix\n" +
                        "           and c.ticket_code = t.ticket_code\n" +
                        "           and c.ctrl_status in (\n" +
                        "                 '" + ConstantBaseApp.SYS_STATUS_PENDING + "',\n" +
                        "                 '" + ConstantBaseApp.SYS_STATUS_PROCESS + "',\n" +
                        "                 '" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC +"'\n" +
                        "                 )\n"+
                        " \n" +
                        "           and m.customer_code = c.customer_code \n" +
                        "           and m.partner_code = c.partner_code) >= 1\n";
                }
                if(bParterNoProfile) {
                    partnerCondition += partnerCondition.length() == 0 ? partnerCondition : "   OR  ";
                    partnerCondition +=
                         "      (SELECT\n" +
                         "             CASE WHEN OTHER_PARTNER > 0 AND MY_PARTNER = 0\n" +
                         "                  THEN 1\n" +
                         "                  ELSE 0\n" +
                         "              END\n" +
                         "       FROM\n" +
                         "         (SELECT\n" +
                         "          sum (CASE WHEN m.partner_code is null\n" +
                         "                    THEN 1\n" +
                         "                    ELSE 0\n" +
                         "               END) OTHER_PARTNER,\n" +
                         "           sum (CASE WHEN m.partner_code is not null\n" +
                         "                     THEN 1\n" +
                         "                     ELSE 0\n" +
                         "                END) MY_PARTNER \n" +
                         "          FROM "+ TK_Ticket_CtrlDao.TABLE +" c\n" +
                         "          LEFT JOIN\n" +
                         "              "+ MD_PartnerDao.TABLE +" m on m.customer_code = c.customer_code \n" +
                         "              and m.partner_code = c.partner_code\n" +
                         "          WHERE c.customer_code = t.customer_code\n" +
                         "                and c.ticket_prefix = t.ticket_prefix\n" +
                         "                and c.ticket_code = t.ticket_code\n" +
                         "                and c.ctrl_status in (\n" +
                         "                   '" + ConstantBaseApp.SYS_STATUS_PENDING + "',\n" +
                         "                   '" + ConstantBaseApp.SYS_STATUS_PROCESS + "',\n" +
                         "                   '" + ConstantBaseApp.SYS_STATUS_WAITING_SYNC +"'\n" +
                         "                 )\n"+
                         "                and c.partner_code is not null)\n" +
                         "          ) = 1\n";
                }
                //
                partnerFilter += " AND ("+partnerCondition+")\n";
            }
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
                "       CASE WHEN t.open_site_code <> '" + site_logged + "' \n" +
                "            THEN t.open_site_desc\n" +
                "            ELSE null\n" +
                "       END open_site_desc ,\n" +
                "       t.open_product_desc,\n" +
                "       t.open_serial_id,\n" +
                "       t.sync_required\n," +
                "       t.close_date\n," +
                "       CASE WHEN t.schedule_prefix IS NOT NULL AND t.schedule_code IS NOT NULL AND t.schedule_exec IS NOT NULL \n" +
                "            THEN t.schedule_prefix||'.'||t.schedule_code||'.'||t.schedule_exec \n" +
                "            ELSE null \n" +
                "       END " + MD_Schedule_ExecDao.SCHEDULE_PK + " \n," +
                "       t.schedule_prefix,\n" +
                "       t.schedule_code,\n" +
                "       t.schedule_exec\n," +
                "       s.fcm_new_status\n," +
                "       s.fcm_user_nick\n," +
                "       s.schedule_erro_msg,\n" +
                "       1 local_ticket, \n" +
                "       t.client_code,\n" +
                "       t.client_name,\n" +
                "       t.contract_code,\n" +
                "       t.contract_desc\n" +
                " FROM\n" +
                "     " + TK_TicketDao.TABLE + " t \n" +
                " LEFT JOIN\n" +
                "     " + MD_Schedule_ExecDao.TABLE + " s ON \n" +
                "       t.schedule_prefix = s.schedule_prefix \n" +
                "       AND t.schedule_code = s.schedule_code\n" +
                "       AND t.schedule_exec = s.schedule_exec \n" +
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
