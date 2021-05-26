package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoa_digital.namoa_library.ctls.CalendarView;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.dao.TkTicketCacheDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by DANIEL.LUCHE on 25/05/2021.
 *
 * Query que faz a contagem das ações sumarizando por:
 *     * Atrasado: Data final, menor que a data atual
 *     * Em Aberto: Data inicio menor que a atual, porem data fim, maior
 *     * Planejado: Data inicio maior que data atual.
 *     * Não focado: Independente da data, se não tem foco(user_focus 0 para tickets e status waiting_sync para os demais).
 */

public class Sql_Act016_002 implements Specification {
    private final String UNION_ALL = " UNION ALL";

    private String customer_code;
    private String sql_sub_query = "";
    private String sqlSchedule = "";
    private String sql_form_ap = "";
    private String sql_ticket = "";
    private String sql_ticket_cache = "";
    private String sql_form = "";
    private String deviceGMT = ToolBox.getDeviceGMT(false);
    private String customerGMT;

    public Sql_Act016_002(Context context, String customer_code) {
        this.customer_code = customer_code;
        this.customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
        buildFinalSql();
    }

    private void buildFinalSql() {
        sqlSchedule =  "   \nSELECT\n" +
                    "      strftime('%Y-%m-%d',s.date_start||' "+customerGMT+"','"+deviceGMT+"') schedule_date_start,\n" +
                    "      ((strftime('%s',s.date_start||' "+customerGMT+"','"+deviceGMT+"') *1000) <= (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) AND (strftime('%s',s.date_end||' "+customerGMT+"','"+deviceGMT+"') *1000) >= (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) and s.status <> '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"' ) "+CalendarView.OPENED_COUNT+",\n" +
                    "      ((strftime('%s',s.date_end||' "+customerGMT+"','"+deviceGMT+"') * 1000) < (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) and s.status <> '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"') "+CalendarView.DELAYED_COUNT+ ",                      \n" +
                    "      ((strftime('%s',s.date_start||' "+customerGMT+"','"+deviceGMT+"') *1000) > (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) AND (strftime('%s',s.date_end||' "+customerGMT+"','"+deviceGMT+"') *1000) > (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) and s.status <> '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"') "+CalendarView.PLANNED_COUNT+ ", \n" +
                    "      (s.status = '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"') "+CalendarView.NOT_FOCUS_COUNT+"\n" +
                    "     \n" +
                    "  FROM  "+MD_Schedule_ExecDao.TABLE+" s\n" +
                    "  \n" +
                    "  WHERE \n" +
                    "        s.customer_code= '"+customer_code+"' \n" +
                    "        AND s.status in(" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_IN_PROCESSING+"',\n" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_PROCESS+"',\n" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE+"',\n" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"'\n" +
                    "                       )";
        //
        sql_form_ap =
                    UNION_ALL +
                    "\nSELECT\n" +
                    "      strftime('%Y-%m-%d',IFNULL(a.ap_when,a.create_date),'"+deviceGMT+"') schedule_date_start,\n" +
                    "      0 "+CalendarView.OPENED_COUNT+","+
                    "      ((strftime('%s',IFNULL(a.ap_when,a.create_date),'"+deviceGMT+"') * 1000) <=(strftime('%s', 'now','"+deviceGMT+"')  * 1000 )) "+CalendarView.DELAYED_COUNT+ ",\n" +
                    "      ((strftime('%s',IFNULL(a.ap_when,a.create_date),'"+deviceGMT+"') * 1000)  > (strftime('%s', 'now','"+deviceGMT+"')  * 1000 )) "+CalendarView.PLANNED_COUNT+ ",\n " +
                    "       0 "+CalendarView.NOT_FOCUS_COUNT+"\n" +
                    "  FROM "+ GE_Custom_Form_ApDao.TABLE+" a  \n" +
                    "  WHERE \n" +
                    "        a.customer_code= '"+customer_code+"'" +
                    "        and a.ap_status not in('"+ConstantBaseApp.SYS_STATUS_DONE+"','"+ConstantBaseApp.SYS_STATUS_CANCELLED+"')" +
                        " \n";
        //
        sql_ticket =
                UNION_ALL +
                " \nSELECT\n" +
                "      strftime('%Y-%m-%d',t.date_start,'"+deviceGMT+"') schedule_date_start,\n" +
                "      ((strftime('%s',t.date_start,'"+deviceGMT+"') *1000) <= (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) AND (strftime('%s',t.date_end,'"+deviceGMT+"') *1000) > (strftime('%s', 'now','"+deviceGMT+"')  * 1000 )  AND t.ticket_status <> '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"' and t.user_focus = 1) "+CalendarView.OPENED_COUNT+",  \n" +
                "      ((strftime('%s',t.date_end,'"+deviceGMT+"') *1000) < (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) AND t.ticket_status <> '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"' and t.user_focus = 1) "+CalendarView.DELAYED_COUNT+ ",      \n" +
                "      ((strftime('%s',t.date_start,'"+deviceGMT+"') *1000) >= (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) AND t.ticket_status <> '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"'  and t.user_focus = 1) "+CalendarView.PLANNED_COUNT+ ",          \n" +
                "      (t.ticket_status = '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"' OR t.user_focus = 0) "+CalendarView.NOT_FOCUS_COUNT+"      \n" +
                "     \n" +
                "  FROM (\n" +
                "        SELECT\n" +
                "         t.ticket_status,  \n" +
                "         ifnull(ts.forecast_start,t.forecast_date) date_start,\n" +
                "         ifnull(ts.forecast_end,t.forecast_date) date_end,\n" +
                "         t.user_focus                      \n" +
                "      FROM\n" +
                "          "+ TK_TicketDao.TABLE +" t\n" +
                "      LEFT JOIN    \n" +
                "          (SELECT\n" +
                "             s.customer_code,\n" +
                "             s.ticket_prefix,\n" +
                "             s.ticket_code,\n" +
                "             s.step_order,\n" +
                "             s.user_focus,\n" +
                "             min(s.forecast_start) forecast_start,\n" +
                "             max(s.forecast_end) forecast_end \n" +
                "           FROM\n" +
                "             "+ TK_Ticket_StepDao.TABLE +" s\n" +
                "           WHERE  \n" +
                "              s.customer_code = "+customer_code+"\n" +
                "              and s.user_focus = 1\n" +
                "           GROUP BY  \n" +
                "             s.customer_code,\n" +
                "             s.ticket_prefix,\n" +
                "             s.ticket_code,\n" +
                "             s.step_order     \n" +
                "          ) ts ON  ts.customer_code = t.customer_code\n" +
                "                  and ts.ticket_prefix = t.ticket_prefix\n" +
                "                  and ts.ticket_code = t.ticket_code\n" +
                "                  and ts.step_order = t.current_step_order\n" +
                "                  and ts.user_focus = 1\n" +
                "      WHERE                     \n" +
                "            t.customer_code = '"+customer_code+"'\n" +
                "            and t.ticket_prefix > 0 \n" +
                "            and ( (t.ticket_status in('"+ ConstantBaseApp.SYS_STATUS_PENDING+"','"+ ConstantBaseApp.SYS_STATUS_PROCESS+"') and t.user_focus = 1) \n" +
                "                  or (t.user_focus = 0 or t.ticket_status = '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"')\n" +
                "                )                            \n" +
                "  ) t ";

        sql_ticket_cache =
            UNION_ALL +
                    "\nSELECT\n" +
                    "      strftime('%Y-%m-%d',tc.forecast_start,'"+deviceGMT+"') schedule_date_start,    \n" +
                    "      ((strftime('%s',tc.forecast_start,'"+deviceGMT+"') *1000) <= (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) AND (strftime('%s',tc.forecast_end,'"+deviceGMT+"') *1000) > (strftime('%s', 'now','"+deviceGMT+"')  * 1000) AND tc.user_focus = 1) "+CalendarView.OPENED_COUNT+",\n" +
                    "      ((strftime('%s',tc.forecast_end,'"+deviceGMT+"') *1000) < (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) AND tc.user_focus = 1) "+CalendarView.DELAYED_COUNT+ ", \n" +
                    "      ((strftime('%s',tc.forecast_start,'"+deviceGMT+"') *1000) >= (strftime('%s', 'now','"+deviceGMT+"')  * 1000 ) AND tc.user_focus = 1) "+CalendarView.PLANNED_COUNT+ ", \n" +
                    "      (tc.user_focus = 0) "+CalendarView.NOT_FOCUS_COUNT+" \n" +
                    "     \n" +
                    "  FROM\n" +
                    "       "+ TkTicketCacheDao.TABLE +" tc\n" +
                    "  LEFT JOIN\n" +
                    "       "+ TK_TicketDao.TABLE +" t on t.customer_code = tc.customer_code\n" +
                    "                  and t.ticket_prefix = tc.ticket_prefix\n" +
                    "                  and t.ticket_code = tc.ticket_code                        \n" +
                    "  WHERE \n" +
                    "        tc.customer_code = '"+customer_code+"'\n" +
                    "        and t.ticket_prefix is null  ";
        sql_form =  UNION_ALL +
                    "\nSELECT\n" +
                    "   strftime('%Y-%m-%d',d.date_start,'"+deviceGMT+"') schedule_date_start,\n" +
                    "   0 "+CalendarView.OPENED_COUNT+",\n" +
                    "   (d.custom_form_status = '"+ ConstantBaseApp.SYS_STATUS_IN_PROCESSING+"') "+CalendarView.DELAYED_COUNT+ ",\n" +
                    "   0 "+CalendarView.PLANNED_COUNT+ ",\n" +
                    "   (d.custom_form_status = '"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"') "+CalendarView.NOT_FOCUS_COUNT+"  \n" +
                    " FROM\n" +
                    "  "+ GE_Custom_Form_DataDao.TABLE +" d\n" +
                    " WHERE\n" +
                    "  d.customer_code = '"+customer_code+"'\n" +
                    "  and d.custom_form_status in ('"+ ConstantBaseApp.SYS_STATUS_IN_PROCESSING+"','"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"')\n" +
                    "  and d.ticket_prefix is null\n" +
                    "  and d.ticket_code is null\n" +
                    "  and d.ticket_seq is null\n" +
                    "  and d.ticket_seq_tmp is null\n" +
                    "  and d.step_code is null\n" +
                    "  and d.schedule_prefix is null\n" +
                    "  and d.schedule_code is null\n" +
                    "  and d.schedule_exec is null" ;
        //
        sql_sub_query = sqlSchedule + sql_form_ap + sql_ticket + sql_ticket_cache + sql_form;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "  t.schedule_date_start "+CalendarView.DT+" ,\n" +
                        "  sum(t."+CalendarView.DELAYED_COUNT+ ") "+CalendarView.DELAYED_COUNT+ ",\n" +
                        "  sum(t."+CalendarView.OPENED_COUNT+ ") "+CalendarView.OPENED_COUNT+ ",\n" +
                        "  sum(t."+CalendarView.PLANNED_COUNT+ ") "+CalendarView.PLANNED_COUNT+ ",\n" +
                        "  sum(t."+CalendarView.NOT_FOCUS_COUNT+ ") "+CalendarView.NOT_FOCUS_COUNT+ "\n"+
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
