package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by DANIEL.LUCHE on 24/03/2017.
 *
 * LUCHE - 19/03/2020
 * Seleciona qtd de itens agendados para a proxima 1 horas e os atrasados.
 *
 */

public class GE_Custom_Form_Local_Sql_013 implements Specification {
    private String customerGMT;
    private long customerCode;
    private long dt_start;
    private long dt_end;

    public GE_Custom_Form_Local_Sql_013(Context context, long customerCode, long dt_start) {
        this.customerCode = customerCode;
        this.dt_start = dt_start;
        //Define data fim como 1 hr no futuro
        this.dt_end = dt_start + (60 * 60 * 1000);
        this.customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   '"+ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE+"' as "+ConstantBaseApp.MD_SCHEDULE_KEY_TYPE+",\n" +
                        "   count (1) as "+ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL+"\n" +
                        " FROM\n" +
                        "   " + MD_Schedule_ExecDao.TABLE + " s \n" +
                        " WHERE\n" +
                        "   s.customer_code = '"+ customerCode +"' \n" +
                        "   and s.status = '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE +"' \n" +
                        "   and (strftime('%s',s.date_start,'"+customerGMT+"') * 1000) >= '" + dt_start + "'\n"+
                        "   and (strftime('%s',s.date_start,'"+customerGMT+"') * 1000) <= '" + dt_end + "'\n" +
                        " union\n" +
                        "  SELECT\n" +
                        "   '"+ConstantBaseApp.MD_SCHEDULE_KEY_LATE+"' as "+ConstantBaseApp.MD_SCHEDULE_KEY_TYPE+",\n" +
                        " count (1) as "+ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL+"\n" +
                        " FROM\n" +
                        "   " + MD_Schedule_ExecDao.TABLE + " s \n" +
                        " WHERE\n" +
                        "   s.customer_code = '"+ customerCode +"' \n" +
                        "   and s.status = '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE +"' \n" +
                        "   and (strftime('%s',s.date_start,'"+customerGMT+"') * 1000) < '" +dt_start +"'\n")
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
