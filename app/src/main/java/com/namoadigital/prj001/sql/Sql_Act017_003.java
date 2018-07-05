package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by DANIEL.LUCHE on 04/07/2018.
 *
 * Query que seleciona total de agendados + form_ap para todos os dias ou
 * um dia especifico.
 *
 */

public class Sql_Act017_003 implements Specification {
    public static final String TOTAL_QTY = "total_qty";

    private long s_customer_code;
    private String selected_date;

    public Sql_Act017_003(long s_customer_code, String selected_date) {
        this.s_customer_code = s_customer_code;
        this.selected_date = selected_date;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "    SUM(T.QTY) "+TOTAL_QTY+"\n" +
                        " FROM(\n" +
                        "     SELECT\n" +
                        "       count(1) qty\n" +
                        "     FROM\n" +
                        "       "+ GE_Custom_Form_LocalDao.TABLE+" l\n" +
                        "     LEFT JOIN \n" +
                        "       "+ GE_Custom_Form_DataDao.TABLE+" d ON \n" +
                        "                 l.customer_code = d.customer_code\n" +
                        "                 AND l.custom_form_type = d.custom_form_type\n" +
                        "                 AND l.custom_form_code = d.custom_form_code\n" +
                        "                 AND l.custom_form_version = d.custom_form_version\n" +
                        "                 AND l.custom_form_data = d.custom_form_data  \n" +
                        "     WHERE\n" +
                        "       l.customer_code = '"+s_customer_code+"'\n" +
//                        "       AND "+filter_form+" = 1\n" +
                        "       AND l.custom_form_data_serv is not null\n" +
                        "       AND ( '"+selected_date+"' is null or strftime('%Y-%m-%d',l.schedule_date_start_format,'localtime') = '"+selected_date+"' )\n" +
//                        "       AND ( '"+site_logged+"' is null or l.site_code = '"+site_logged+"') \n" +
//                        "       AND ( '"+filter_only_delay+"' is null or (l.schedule_date_start_format_ms < (strftime('%s', 'now')  * 1000 ) and l.custom_form_status = '"+ Constant.SYS_STATUS_SCHEDULE+"')) \n" +
                        "       \n" +
                        "     UNION  ALL \n" +
                        "        \n" +
                        "     SELECT\n" +
                        "       count(1) qty\n" +
                        "     FROM\n" +
                        "      "+ GE_Custom_Form_ApDao.TABLE+" A\n" +
                        "     WHERE\n" +
                        "       A.customer_code = '"+s_customer_code+"'\n" +
//                        "       AND "+filter_form_ap+" = 1\n" +
                        "       AND a.ap_when is not null \n" +
                        "       AND ( '"+selected_date+"' is null or strftime('%Y-%m-%d',a.ap_when,'localtime') = '"+selected_date+"')\n" +
//                        "       AND ( '"+filter_only_delay+"' is null or ((strftime('%s',a.ap_when) * 1000) < (strftime('%s', 'now')  * 1000 ) and a.ap_status not in('"+Constant.SYS_STATUS_DONE+"','"+Constant.SYS_STATUS_CANCELLED+"') )) \n" +
                        " ) T\n;")
                .append(TOTAL_QTY)
                .toString()
                .replace("'null'","null");


    }
}
