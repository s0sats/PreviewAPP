package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoa_digital.namoa_library.ctls.CalendarView;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by DANIEL.LUCHE on 13/04/2017.
 *
 * Modificado by DANIEL.LUCHE on em 05/07/2018
 * Adicionado filtro de site_logged na query de seleção dos form agendados.
 *
 * Modificado by DANIEL.LUCHE on 06/07/2018.
 *
 * Em 06/07/2018 houve uma mudança no conceito de atrasadpo no badge e no contador da act046,
 * onde o agendamento é considerado atrasado sua data dia/mes/ano for menor ou igual a de hoje.
 *
 * ESSA REGRA NÃO DEVE SER APLICADA AQUI, CONFORME ACORDADO COM J.YUDICE.
 *
 * 27/11/18 - LUCHE
 * Modificado parametro no metodo de formação de data, strftime(), que indica para qual time zone
 * a data deve ser convertido.
 * Antes era usado o localtime, porem como ele apresentou problemas quando o device esta em horario de verão,
 * assim como a propria classe Calendar do Java, o parametro foi substituido pelo novo retorno do novo
 * metodo getDeviceGMT().
 *
 * LUCHE - 12/02/2020
 *
 * Modificado query de form para usar a nova tabela de agendamento dm_schedule_exec
 *
 * LUCHE - 13/02/2020
 * Como os agendamentos não possuem timezone, será usado a nova preferencia Customer_TMZ,
 *
 * LUCHE - 30/03/2020
 * Substituido o status FINALIZED pelo WAITING_SYNC e  SENT por DONE, na query de form.
 * Substituido filtro que comparava pks do form <> de null pelo campo schedule_type = form
 * Modificado a query substituindo as antigas constantes o CalendarView pelas novas e adicionando o contador de not_executed
 *
 * LUCHE - 01/04/2020
 * Modificado query, corrigindo os contadores do Form Ap. Os concluidos estava sendo considerado como waiting_sync
 *
 * LUCHE - 03/04/2020
 * Modificado query add filtro dos status que devem aparecer no calendario, corrindo bug de clicar me item sem nada abrir a adata
 */

public class Sql_Act016_001 implements Specification {
    private final String UNION_ALL = " UNION ALL";

    private String customer_code;
    private String sql_sub_query = "";
    private String sql_form = "";
    private String sql_form_ap = "";
    private String sql_ticket = "";
    private String site_logged;
    private String deviceGMT = ToolBox.getDeviceGMT(false);
    private String customerGMT;

    public Sql_Act016_001(Context context, String customer_code, boolean filter_form, boolean filter_form_ap, boolean filter_site, boolean filter_ticket) {
        this.customer_code = customer_code;
        this.site_logged = filter_site ? ToolBox_Con.getPreference_Site_Code(context) : null;
        this.customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
        //
        buildFinalSql(filter_form,filter_form_ap,filter_ticket);
    }

    private void buildFinalSql(boolean filter_form, boolean filter_form_ap, boolean filter_ticket) {
        sql_form =  UNION_ALL +
                    "   \nSELECT\n" +
                    "      strftime('%Y-%m-%d',s.date_start||' "+customerGMT+"','"+customerGMT+"') schedule_date_start,\n" +
                    "      ((strftime('%s',s.date_start||' "+customerGMT+"','"+customerGMT+"') *1000) < (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) and s.status = '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE+"' ) "+CalendarView.DELAYED_COUNT+ ",\n" +
                    "      (s.status = '"+ ConstantBaseApp.SYS_STATUS_IN_PROCESSING+"') "+CalendarView.INPROCESSING_COUNT+ ",\n" +
                    "      ((strftime('%s',s.date_start||' "+customerGMT+"','"+customerGMT+"') *1000) >= (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) AND s.status = '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE+"') "+CalendarView.SCHEDULED_COUNT+ ",    \n" +
                    "      (s.status = '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"') "+CalendarView.WAITING_SYNC_COUNT+ ",\n" +
                    "      (s.status = '"+ ConstantBaseApp.SYS_STATUS_DONE+"') "+CalendarView.DONE_COUNT+", \n" +
                    "      (s.status = '"+ ConstantBaseApp.SYS_STATUS_NOT_EXECUTED+"') "+CalendarView.NOT_EXECUTED_COUNT+"\n" +
                    "     \n" +
                    "  FROM "+ MD_Schedule_ExecDao.TABLE+" s\n" +
                    "  \n" +
                    "  WHERE \n" +
                    "        s.customer_code= '"+customer_code+"'     \n" +
                    "        AND s.schedule_type = '"+ConstantBaseApp.MD_SCHEDULE_TYPE_FORM+"'\n" +
                    "        AND s.status in(" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_IN_PROCESSING+"',\n" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_PROCESS+"',\n" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE+"',\n" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"',\n" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_DONE+"',\n" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_NOT_EXECUTED+"'\n" +
                    "                      )\n " +
                    "        AND ('"+site_logged+"' is null or s.site_code = '"+site_logged+"') ";
        //Remove , caso exista, o text 'null'
        sql_form = sql_form.replace("'null'","null");
        //
        sql_form_ap =
                    UNION_ALL +
                    "\nSELECT\n" +
                    "      strftime('%Y-%m-%d',a.ap_when,'"+deviceGMT+"') schedule_date_start,\n" +
                    "      ((strftime('%s',a.ap_when) * 1000) < (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) and a.ap_status not in('"+ConstantBaseApp.SYS_STATUS_DONE+"','"+ConstantBaseApp.SYS_STATUS_CANCELLED+"') ) "+CalendarView.DELAYED_COUNT+ ",\n" +
                    "      0 "+CalendarView.INPROCESSING_COUNT+ ",\n" +
                    "      ((strftime('%s',a.ap_when) * 1000)  >= (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) and a.ap_status not in('"+ConstantBaseApp.SYS_STATUS_DONE+"','"+ConstantBaseApp.SYS_STATUS_CANCELLED+"')) "+CalendarView.SCHEDULED_COUNT+ ",\n" +
                    "      0 "+CalendarView.WAITING_SYNC_COUNT+ ",\n" +
                    "      (a.ap_status = '"+ ConstantBaseApp.SYS_STATUS_DONE+"') "+CalendarView.DONE_COUNT+ ",\n" +
                    "      0 "+CalendarView.NOT_EXECUTED_COUNT+"\n" +
                    "  FROM "+ GE_Custom_Form_ApDao.TABLE+" a  \n" +
                    "  WHERE \n" +
                    "        a.customer_code= '"+customer_code+"'     \n" +
                    "        AND a.ap_when is not null \n ";
        //
        sql_ticket =
                UNION_ALL +
                "   \nSELECT\n" +
                "      strftime('%Y-%m-%d',s.date_start ||' "+customerGMT+"','"+customerGMT+"') schedule_date_start,\n" +
                "      ((strftime('%s',s.date_start ||' "+customerGMT+"','"+customerGMT+"') *1000) < (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) and s.status = '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE+"' ) "+CalendarView.DELAYED_COUNT+ ",\n" +
                "      (s.status = '"+ ConstantBaseApp.SYS_STATUS_PROCESS+"') "+CalendarView.INPROCESSING_COUNT+ ",\n" +
                "      ((strftime('%s',s.date_start ||' "+customerGMT+"','"+customerGMT+"') *1000) >= (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) AND s.status = '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE+"') "+CalendarView.SCHEDULED_COUNT+ ",    \n" +
                "      (s.status = '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"') "+CalendarView.WAITING_SYNC_COUNT+ ",\n" +
                "      (s.status = '"+ ConstantBaseApp.SYS_STATUS_DONE+"') "+CalendarView.DONE_COUNT+", \n" +
                "      (s.status = '"+ ConstantBaseApp.SYS_STATUS_NOT_EXECUTED+"') "+CalendarView.NOT_EXECUTED_COUNT+"\n" +
                "     \n" +
                "  FROM "+ MD_Schedule_ExecDao.TABLE+" s\n" +
                "  \n" +
                "  WHERE \n" +
                "        s.customer_code= '"+customer_code+"'     \n" +
                "        AND s.schedule_type = '"+ ConstantBaseApp.MD_SCHEDULE_TYPE_TICKET +"' \n" +
                "        AND s.status in(" +
                "                       '"+ ConstantBaseApp.SYS_STATUS_IN_PROCESSING+"',\n" +
                "                       '"+ ConstantBaseApp.SYS_STATUS_PROCESS+"',\n" +
                "                       '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE+"',\n" +
                "                       '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"',\n" +
                "                       '"+ ConstantBaseApp.SYS_STATUS_DONE+"',\n" +
                "                       '"+ ConstantBaseApp.SYS_STATUS_NOT_EXECUTED+"'\n" +
                "                      )\n " +
                "        AND ('"+site_logged+"' is null or s.site_code = '"+site_logged+"') ";
        //Remove , caso exista, o text 'null'
        sql_ticket = sql_ticket.replace("'null'","null");
        //
        sql_sub_query += filter_form ? sql_form : "";
        sql_sub_query += filter_form_ap ? sql_form_ap :"";
        sql_sub_query += filter_ticket ? sql_ticket :"";
        //
        sql_sub_query = sql_sub_query.substring(UNION_ALL.length(), sql_sub_query.length());

    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "  t.schedule_date_start "+CalendarView.DT+" ,\n" +
                        "  sum(t."+CalendarView.DELAYED_COUNT+ ") "+CalendarView.DELAYED_COUNT+ ",\n" +
                        "  sum(t."+CalendarView.INPROCESSING_COUNT+ ") "+CalendarView.INPROCESSING_COUNT+ ",\n" +
                        "  sum(t."+CalendarView.SCHEDULED_COUNT+ ") "+CalendarView.SCHEDULED_COUNT+ ",\n" +
                        "  sum(t."+CalendarView.WAITING_SYNC_COUNT+ ") "+CalendarView.WAITING_SYNC_COUNT+ ",\n" +
                        "  sum(t."+CalendarView.DONE_COUNT+") "+CalendarView.DONE_COUNT+",\n " +
                        "  sum(t."+CalendarView.NOT_EXECUTED_COUNT+") "+CalendarView.NOT_EXECUTED_COUNT+ "\n " +
                        " FROM(\n" +
                        sql_sub_query +
                        "   ) T\n" +
                        "      \n" +
                        " GROUP BY\n" +
                        "  schedule_date_start;"
                )
                .toString();

    }
}
