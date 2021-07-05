package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 30/06/2021
 * Criado query que seleciona todos os ticket em condições de serem possiveis de atualizar, para serem
 * enviados no processo de login.
 * LUCHE - 02/07/2021
 * Corrigido query, removendo os campos de ticket_seq e ticket_seq_tmp da query do not existis.
 * Agora a query checa corretanemente se existem form com pendencia de GPS independente de ser o obj planejado ou não.
 */

public class Sql_WS_TK_Ticket_Download_001 implements Specification {

    public static final String TICKET_PK = "TICKET_PK";
    private long customer_code;

    public Sql_WS_TK_Ticket_Download_001(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "      distinct t.customer_code ||'|'|| t.ticket_prefix ||'|'|| t.ticket_code ||'|'|| t.scn "+TICKET_PK+",\n" +
                        "       t.ticket_prefix,\n" +
                        "       t.ticket_code,\n" +
                        "       t.scn\n" +
                        " FROM\n" +
                        "     "+ TK_TicketDao.TABLE +" t,\n" +
                        "     "+ TK_Ticket_StepDao.TABLE +" s,\n" +
                        "     "+ TK_Ticket_CtrlDao.TABLE +" c\n" +
                        "WHERE\n" +
                        "  t.customer_code = '" + customer_code + "'\n" +
                        "   and t.ticket_prefix = s.ticket_prefix \n" +
                        "  and t.ticket_code = s.ticket_code\n" +
                        "  and s.ticket_prefix = c.ticket_prefix\n" +
                        "  and s.ticket_code = c.ticket_code\n" +
                        "  \n" +
                        "  and t.update_required_product = 0\n" +
                        "  and t.update_required = 0\n" +
                        "  and s.update_required = 0\n" +
                        "  and c.update_required = 0\n" +
                        "  and NOT EXISTS(SELECT 1\n" +
                        "                     FROM ge_custom_form_datas d\n" +
                        "                     WHERE d.customer_code = c.customer_code\n" +
                        "                           and d.ticket_prefix = c.ticket_prefix\n" +
                        "                           and d.ticket_code = c.ticket_code\n" +
                        "                           and d.custom_form_status = '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC +"'\n" +
                        "                           and d.location_pendency = 1)\n" +
                        "  ;"
                ).toString();
    }
}
