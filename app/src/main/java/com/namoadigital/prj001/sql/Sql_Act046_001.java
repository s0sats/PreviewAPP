package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.CalendarView;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 13/04/2017.
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
 */

public class Sql_Act046_001 implements Specification {
    private final String UNION_ALL = " UNION ALL";

    private String customer_code;
    private String sql_sub_query = "";
    private String sql_form = "";
    private String sql_form_ap = "";
    private String deviceGMT = ToolBox.getDeviceGMT(false);

    public Sql_Act046_001(String customer_code, boolean filter_form, boolean filter_form_ap) {
        this.customer_code = customer_code;
        //
        buildFinalSql(filter_form, filter_form_ap);
    }

    private void buildFinalSql(boolean filter_form, boolean filter_form_ap) {
        sql_form =
                UNION_ALL +
                "   \nSELECT\n" +
                "      strftime('%Y-%m-%d',s.date_start,'"+deviceGMT+"') schedule_date_start,\n" +
                "      ((strftime('%Y-%m-%d',s.date_start ,'"+deviceGMT+"' ) <= strftime('%Y-%m-%d','now','"+deviceGMT+"')) and s.status = '" + Constant.SYS_STATUS_PENDING + "' ) delayed_count\n" +
                "     \n" +
                "  FROM " + MD_Schedule_ExecDao.TABLE + " s\n" +
                "  \n" +
                "  WHERE \n" +
                "        s.customer_code= '" + customer_code + "' \n" ;
        //
        sql_form_ap =
                UNION_ALL +
                        "\nSELECT\n" +
                        "      strftime('%Y-%m-%d',a.ap_when,'"+deviceGMT+"') schedule_date_start,\n" +
                        //"      ((strftime('%s',a.ap_when) * 1000) < (strftime('%s', 'now')  * 1000 ) and a.ap_status not in('" + Constant.SYS_STATUS_DONE + "','" + Constant.SYS_STATUS_CANCELLED + "') ) delayed_count\n" +
                        "      ((strftime('%Y-%m-%d',a.ap_when ,'"+deviceGMT+"' ) <= strftime('%Y-%m-%d','now','"+deviceGMT+"')) and a.ap_status not in('" + Constant.SYS_STATUS_DONE + "','" + Constant.SYS_STATUS_CANCELLED + "') ) delayed_count\n" +
                        "  FROM " + GE_Custom_Form_ApDao.TABLE + " a  \n" +
                        "  WHERE \n" +
                        "        a.customer_code= '" + customer_code + "'     \n" +
                        "        AND a.ap_when is not null \n ";

        sql_sub_query += filter_form ? sql_form : "";
        sql_sub_query += filter_form_ap ? sql_form_ap : "";
        //
        sql_sub_query = sql_sub_query.substring(UNION_ALL.length(), sql_sub_query.length());

    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "  sum(t.delayed_count) " + CalendarView.DELAYED_COUNT + "\n" +
                        " FROM(\n" +
                        sql_sub_query +
                        "   ) T\n" +

                        ";")
                //.append(CalendarView.DELAYED_COUNT)
                .toString();

    }
}
