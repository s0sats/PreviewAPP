package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.CalendarView;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 13/04/2017.
 */

public class Sql_Act046_001 implements Specification {
    private final String UNION_ALL = " UNION ALL";

    private String customer_code;
    private String sql_sub_query = "";
    private String sql_form = "";
    private String sql_form_ap = "";

    public Sql_Act046_001(String customer_code, boolean filter_form, boolean filter_form_ap) {
        this.customer_code = customer_code;
        //
        buildFinalSql(filter_form, filter_form_ap);
    }

    private void buildFinalSql(boolean filter_form, boolean filter_form_ap) {
        sql_form = UNION_ALL +
                "   \nSELECT\n" +
                "      strftime('%Y-%m-%d',l.schedule_date_start_format,'localtime') schedule_date_start,\n" +
                //"      (l.schedule_date_start_format_ms < (strftime('%s', 'now')  * 1000 ) and l.custom_form_status = '" + Constant.SYS_STATUS_SCHEDULE + "' ) delayed_count\n" +
                "      ((strftime('%Y-%m-%d',l.schedule_date_start_format ,'localtime' ) <= strftime('%Y-%m-%d','now','localtime')) and l.custom_form_status = '" + Constant.SYS_STATUS_SCHEDULE + "' ) delayed_count\n" +
                "     \n" +
                "  FROM " + GE_Custom_Form_LocalDao.TABLE + " l\n" +
                "  \n" +
                "  WHERE \n" +
                "        l.customer_code= '" + customer_code + "'     \n" +
                "        AND l.custom_form_data_serv is not null\n";
        //
        sql_form_ap =
                UNION_ALL +
                        "\nSELECT\n" +
                        "      strftime('%Y-%m-%d',a.ap_when,'localtime') schedule_date_start,\n" +
                        //"      ((strftime('%s',a.ap_when) * 1000) < (strftime('%s', 'now')  * 1000 ) and a.ap_status not in('" + Constant.SYS_STATUS_DONE + "','" + Constant.SYS_STATUS_CANCELLED + "') ) delayed_count\n" +
                        "      ((strftime('%Y-%m-%d',a.ap_when ,'localtime' ) <= strftime('%Y-%m-%d','now','localtime')) and a.ap_status not in('" + Constant.SYS_STATUS_DONE + "','" + Constant.SYS_STATUS_CANCELLED + "') ) delayed_count\n" +
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

                                ";" + CalendarView.DELAYED_COUNT

                )
                .toString();

    }
}
