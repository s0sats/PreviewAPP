package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sql_Act069_001 implements Specification {
    private long customer_code;
    private String site_logged;
    private String statusFilter ="";
    private String partnerFilter ="";

    public Sql_Act069_001(long customer_code, String site_logged, boolean bStatusPending, boolean bStatusProcess, boolean bStatusWaitingSync, boolean bStatusDone, boolean bParterEmpty, boolean bParterProfile) {
        this.customer_code = customer_code;
        this.site_logged = site_logged;
        //
        if(bStatusDone){
            statusFilter = "    and t.ticket_status = '"+ ConstantBaseApp.SYS_STATUS_DONE +"'\n";
        }else{
            if(bStatusPending || bStatusProcess || bStatusWaitingSync){
                /*statusFilter = "   and t.ticket_status in(";
                statusFilter += bStatusPending ? "'"+ConstantBaseApp.SYS_STATUS_PENDING +"' ":"";
                statusFilter += bStatusPending && bStatusProcess ? " , ":"";
                statusFilter += bStatusProcess ? "'"+ConstantBaseApp.SYS_STATUS_PROCESS +"' ":"";
                statusFilter += " )\n";*/
                //
                statusFilter = "   and t.ticket_status in(";
                statusFilter += bStatusPending ? "'"+ConstantBaseApp.SYS_STATUS_PENDING +"', ":"";
                statusFilter += bStatusProcess ? "'"+ConstantBaseApp.SYS_STATUS_PROCESS +"', ":"";
                statusFilter += bStatusWaitingSync ? "'"+ConstantBaseApp.SYS_STATUS_WAITING_SYNC +"', ":"";
                statusFilter = statusFilter.substring(0,statusFilter.length() - ", ".length());
                statusFilter += " )\n";


            }else{
                statusFilter = "   and t.ticket_status <> '"+ ConstantBaseApp.SYS_STATUS_DONE +"'\n";
            }
            //
            partnerFilter += "    and (c.partner_code is "+ (bParterEmpty ? " null " : " not null");
            partnerFilter += bParterProfile ?   " or EXISTS (SELECT 1\n" +
                                                "             FROM "+ MD_PartnerDao.TABLE +" m\n" +
                                                "             WHERE m.customer_code = t.customer_code \n" +
                                                "                   and m.partner_code = c.partner_code ) \n" : "";
            partnerFilter += "        )\n";
            //
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
                    "       CASE WHEN t.current_site_code <> '"+site_logged+"' \n" +
                    "            THEN t.current_site_desc\n" +
                    "            ELSE null\n" +
                    "       END site_desc ,\n" +
                    "       t.current_product_desc,\n" +
                    "       t.current_serial_id           \n" +
                    " FROM\n" +
                    "     "+ TK_TicketDao.TABLE +" t,\n" +
                    "     "+ TK_Ticket_CtrlDao.TABLE +" c\n" +
                    " WHERE\n" +
                    "     t.customer_code = c.customer_code\n" +
                    "     and t.ticket_prefix = c.ticket_prefix\n" +
                    "     and t.ticket_code = c.ticket_code\n" +
                    "      \n" +
                    "     and t.customer_code = '"+customer_code+"'\n" +
                    statusFilter +
                    partnerFilter +
                    " ORDER BY \n" +
                    "  t.forecast_date desc,\n" +
                    "  t.ticket_prefix,\n" +
                    "  t.ticket_code\n"
                    )
            .toString();
    }
}
