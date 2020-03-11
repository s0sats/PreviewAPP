package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoa_digital.namoa_library.ctls.CalendarView;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
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
 */

public class Sql_Act016_001 implements Specification {
    private final String UNION_ALL = " UNION ALL";

    private String customer_code;
    private String sql_sub_query = "";
    private String sql_form = "";
    private String sql_form_ap = "";
    private String site_logged;
    private String deviceGMT = ToolBox.getDeviceGMT(false);
    private String customerGMT;

    public Sql_Act016_001(Context context, String customer_code, boolean filter_form, boolean filter_form_ap, boolean filter_site) {
        this.customer_code = customer_code;
        this.site_logged = filter_site ? ToolBox_Con.getPreference_Site_Code(context) : null;
        this.customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
        //
        buildFinalSql(filter_form,filter_form_ap);
    }

    private void buildFinalSql(boolean filter_form, boolean filter_form_ap) {
        sql_form =  UNION_ALL +
                    "   \nSELECT\n" +
                    "      strftime('%Y-%m-%d',s.date_start,'"+customerGMT+"') schedule_date_start,\n" +
                    "      ((strftime('%s',s.date_start,'"+customerGMT+"') *1000) < (strftime('%s', 'now')  * 1000 ) and s.status = '"+ Constant.SYS_STATUS_SCHEDULE+"' ) delayed_count,\n" +
                    "      (s.status = '"+ Constant.SYS_STATUS_IN_PROCESSING+"') inprocessing_count,\n" +
                    "      ((strftime('%s',s.date_start,'"+customerGMT+"') *1000) >= (strftime('%s', 'now')  * 1000 ) AND s.status = '"+ Constant.SYS_STATUS_SCHEDULE+"') scheduled_count,    \n" +
                    "      (s.status = '"+ Constant.SYS_STATUS_FINALIZED+"') finalized_count,\n" +
                    "      (s.status = '"+ Constant.SYS_STATUS_SENT+"') sent_count\n" +
                    "     \n" +
                    "  FROM "+ MD_Schedule_ExecDao.TABLE+" s\n" +
                    "  \n" +
                    "  WHERE \n" +
                    "        s.customer_code= '"+customer_code+"'     \n" +
                    "        AND s.custom_form_type is not null \n" +
                    "        AND s.custom_form_code is not null \n" +
                    "        AND s.custom_form_version is not null \n" +
                    "        AND ('"+site_logged+"' is null or s.site_code = '"+site_logged+"') ";
        //Remove , caso exista, o text 'null'
        sql_form = sql_form.replace("'null'","null");
        //
        sql_form_ap =
                    UNION_ALL +
                    "\nSELECT\n" +
                    "      strftime('%Y-%m-%d',a.ap_when,'"+deviceGMT+"') schedule_date_start,\n" +
                    "      ((strftime('%s',a.ap_when) * 1000) < (strftime('%s', 'now')  * 1000 ) and a.ap_status not in('"+Constant.SYS_STATUS_DONE+"','"+Constant.SYS_STATUS_CANCELLED+"') ) delayed_count,\n" +
                    "      0 inprocessing_count,\n" +
                    "      ((strftime('%s',a.ap_when) * 1000)  >= (strftime('%s', 'now')  * 1000 ) and a.ap_status not in('"+Constant.SYS_STATUS_DONE+"','"+Constant.SYS_STATUS_CANCELLED+"')) scheduled_count,\n" +
                    "      (a.ap_status = '"+ Constant.SYS_STATUS_DONE+"') finalized_count,\n" +
                    "      0 sent_count\n" +
                    "  FROM "+ GE_Custom_Form_ApDao.TABLE+" a  \n" +
                    "  WHERE \n" +
                    "        a.customer_code= '"+customer_code+"'     \n" +
                    "        AND a.ap_when is not null \n ";

        sql_sub_query += filter_form ? sql_form : "";
        sql_sub_query += filter_form_ap ? sql_form_ap :"";
        //
        sql_sub_query = sql_sub_query.substring(UNION_ALL.length(), sql_sub_query.length());

    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "  t.schedule_date_start "+CalendarView.DT+" ,\n" +
                        "  sum(t.delayed_count) "+CalendarView.DELAYED_COUNT+ ",\n" +
                        "  sum(t.inprocessing_count) "+CalendarView.INPROCESSING_COUNT+ ",\n" +
                        "  sum(t.scheduled_count) "+CalendarView.SCHEDULED_COUNT+ ",\n" +
                        "  sum(t.finalized_count) "+CalendarView.FINALIZED_COUNT+ ",\n" +
                        "  sum(t.sent_count) "+CalendarView.SENT_COUNT+ "\n " +
                        " FROM(\n" +
                        sql_sub_query +
                        "   ) T\n" +
                        "      \n" +
                        " GROUP BY\n" +
                        "  schedule_date_start;"
//                        CalendarView.DT
//                        +"#"+ CalendarView.DELAYED_COUNT
//                        +"#"+ CalendarView.INPROCESSING_COUNT
//                        +"#"+ CalendarView.SCHEDULED_COUNT
//                        +"#"+ CalendarView.FINALIZED_COUNT
//                        +"#"+ CalendarView.SENT_COUNT
//                        +"#cur_data#cur_mili"
                )
                .toString();

    }
}
