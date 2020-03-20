package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by DANIEL.LUCHE on 24/03/2017.
 *
 * LUCHE - 19/03/2020
 * Seleciona qtd de itens agendados para a proxima 1 horas e os atrasados.
 *
 */

public class Sql_Notification_Schedule_001 implements Specification {
    private String customerGMT;
    private long customerCode;
    private String dt_start;
    private String dt_end;
    private String scheduleSql;
    private String formApSql;
    private String deviceGMT;

    public Sql_Notification_Schedule_001(Context context, long customerCode) {
        this.customerCode = customerCode;
        this.deviceGMT = ToolBox.getDeviceGMT(false);
        this.dt_start = " (strftime('%s','now','"+deviceGMT+"') * 1000) ";
        this.dt_end = " (strftime('%s','now','"+deviceGMT+"','+1 hour') * 1000) ";
        this.customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
        this.scheduleSql = getScheduleSql();
        this.formApSql = getFormApSql();
    }

    private String getFormApSql() {
        return  " SELECT\n" +
                "   '"+ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE+"' as "+ConstantBaseApp.MD_SCHEDULE_KEY_TYPE+",\n" +
                "   count (1) as "+ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL+" \n" +
                " FROM\n" +
                "   " +  GE_Custom_Form_ApDao.TABLE +  " a \n" +
                " WHERE\n" +
                "   a.customer_code = '"+ customerCode +"' \n" +
                "   and a.ap_status not in('" + Constant.SYS_STATUS_DONE + "','" + Constant.SYS_STATUS_CANCELLED + "')\n"+
                "   and a.ap_when is not null\n"+
                "   and (strftime('%s',a.ap_when,'"+deviceGMT+"') * 1000) >= " +dt_start + "\n" +
                "   and (strftime('%s',a.ap_when,'"+deviceGMT+"') * 1000) <= " +dt_end + "\n" +
                " union\n" +
                " SELECT\n" +
                "   '"+ConstantBaseApp.MD_SCHEDULE_KEY_LATE+"' as "+ConstantBaseApp.MD_SCHEDULE_KEY_TYPE+",\n" +
                "   count (1) as "+ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL+"\n" +
                " FROM\n" +
                "   " +  GE_Custom_Form_ApDao.TABLE +  " a \n" +
                " WHERE\n" +
                "   a.customer_code = '"+ customerCode +"' \n" +
                "   and a.ap_status not in('" + Constant.SYS_STATUS_DONE + "','" + Constant.SYS_STATUS_CANCELLED + "')\n"+
                "   and a.ap_when is not null\n"+
                "   and (strftime('%s',a.ap_when,'"+customerGMT+"') * 1000) < " +dt_start +"\n";
    }

    private String getScheduleSql() {
        return " SELECT\n" +
                "   '"+ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE+"' as "+ConstantBaseApp.MD_SCHEDULE_KEY_TYPE+",\n" +
                "   count (1) as "+ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL+"\n" +
                " FROM\n" +
                "   " + MD_Schedule_ExecDao.TABLE + " s \n" +
                " WHERE\n" +
                "   s.customer_code = '"+ customerCode +"' \n" +
                "   and s.status = '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE +"' \n" +
                "   and (strftime('%s',s.date_start,'"+customerGMT+"') * 1000) >= " + dt_start + "\n"+
                "   and (strftime('%s',s.date_start,'"+customerGMT+"') * 1000) <= " + dt_end + "\n" +
                " union\n" +
                "  SELECT\n" +
                "   '"+ConstantBaseApp.MD_SCHEDULE_KEY_LATE+"' as "+ConstantBaseApp.MD_SCHEDULE_KEY_TYPE+",\n" +
                " count (1) as "+ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL+"\n" +
                " FROM\n" +
                "   " + MD_Schedule_ExecDao.TABLE + " s \n" +
                " WHERE\n" +
                "   s.customer_code = '"+ customerCode +"' \n" +
                "   and s.status = '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE +"' \n" +
                "   and (strftime('%s',s.date_start,'"+customerGMT+"') * 1000) < "+dt_start +"\n";
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "   temp_s."+ConstantBaseApp.MD_SCHEDULE_KEY_TYPE+" \n," +
                        "   temp_s."+ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL+" + temp_a."+ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL+" "+ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL+" \n" +
                        " FROM  (\n" +
                            scheduleSql+
                        "   ) temp_s\n," +
                        "(\n"+
                            formApSql +
                        "   ) temp_a \n"+
                        " WHERE \n" +
                        "   temp_s.type = temp_a.type \n " +
                        " GROUP BY \n" +
                        "   temp_s."+ConstantBaseApp.MD_SCHEDULE_KEY_TYPE+"\n" )
                .append(";")
                .toString();
    }

    /**
     *
     *  select 'FUTURE',  count (1) as total from ge_custom_forms_local where custom_form_status = 'SCHEDULE' and schedule_date_start_format_ms >=  '1492225200000' and schedule_date_start_format_ms <= 1492225200000 + ( 60 * 60 * 1000)

     union

     select 'LATE',  count (1) as total from ge_custom_forms_local where custom_form_status = 'SCHEDULE' and schedule_date_start_format_ms <  '1493050061729';
     *
     *
     */
}
