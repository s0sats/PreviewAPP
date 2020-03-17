package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

public class Sql_Act017_004 implements Specification {
    private long customer_code;
    private String selected_date;
    private String serial_id;
    private String filter_only_delay;
    private String site_logged;
    private String deviceGMT = ToolBox.getDeviceGMT(false);
    private String customerGMT;

    public Sql_Act017_004(Context context, long customer_code, String selected_date, String serial_id, boolean late, boolean site_logged) {
        this.customer_code = customer_code;
        this.selected_date = selected_date;
        this.serial_id = serial_id.trim().length() > 0 ?serial_id : null;
        this.filter_only_delay = late ? "filter" : null;
        this.site_logged = site_logged ? ToolBox_Con.getPreference_Site_Code(context) : null;
        this.customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                "'"+ConstantBaseApp.PROFILE_MENU_TICKET +"' "+ Act017_Main.ACT017_MODULE_KEY +" ,\n" +
                "  s.customer_code,\n" +
                "  s.ticket_type,\n" +
                "  s.ticket_type_id,\n" +
                "  s.ticket_type_desc,\n" +
                "  s.product_code,\n" +
                "  s.product_desc,\n" +
                "  s.product_id,\n" +
                "  s.status,\n" +
                "  s.site_code,\n" +
                "  s.site_id,\n" +
                "  s.site_desc,\n" +
                "  s.serial_code,\n" +
                "  s.serial_id,\n" +
                "  s.date_start, \n" +
                "  s.date_end ,\n" +
                "  s.date_start ||' "+customerGMT+"' "+ MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT+",\n"+
                "  s.date_end ||' "+customerGMT+"' "+MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT+",\n"+
                "  strftime('%Y-%m-%d',s.date_start,'"+customerGMT+"') "+Act017_Main.ACT017_ADAPTER_DATE_REF+",\n"+
                "  (strftime('%s',s.date_start,'"+customerGMT+"') * 1000)  "+Act017_Main.ACT017_ADAPTER_DATE_REF_MS+",\n"+
                "  s.comments\n,"+
                "  s.schedule_prefix||'.'||s.schedule_code||'.'||s.schedule_exec "+ MD_Schedule_ExecDao.SCHEDULE_PK +", \n "+
                "  s.schedule_prefix,\n" +
                "  s.schedule_code,\n" +
                "  s.schedule_desc,\n" +
                "  s.schedule_exec,\n "+
                "  t.ticket_prefix,\n "+
                "  t.ticket_code,\n "+
                "  t.type_path\n "+
                " \n" +
                "  FROM\n" +
                "   " + MD_Schedule_ExecDao.TABLE+ " s\n" +
                "   LEFT JOIN " + TK_TicketDao.TABLE+ " t ON \n " +
                "      s.customer_code = t.customer_code \n " +
                "      AND s.schedule_prefix = t.schedule_prefix\n" +
                "      AND s.schedule_code = t.schedule_code\n" +
                "      AND s.schedule_exec = t.schedule_exec\n" +
                "  WHERE\n" +
                "      s."+ MD_Schedule_ExecDao.CUSTOMER_CODE+" = '"+customer_code+"' \n" +
                "      AND s.schedule_type = '"+ ConstantBaseApp.MD_SCHEDULE_TYPE_TICKET +"' \n" +
                "      AND ('"+selected_date+"' is null or strftime('%Y-%m-%d',s.date_start,'"+customerGMT+"') = '"+selected_date+"') \n" +
                "      AND ('"+serial_id+"' is null or s.serial_id like '%"+serial_id+"%' or s.serial_id like '%"+serial_id+"%' ) \n" +
                "      AND ('"+site_logged+"' is null or s.site_code = '"+site_logged+"') \n" +
                "      AND ('"+filter_only_delay+"' is null or ( (strftime('%Y-%m-%d',s.date_start ,'"+customerGMT+"' ) <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))  and s.status = '"+ Constant.SYS_STATUS_SCHEDULE+"')) \n" +
                "  ORDER BY\n" +
                "      strftime('%Y-%m-%d %H:%M',s.date_start,'"+customerGMT+"'), \n" +
                "      CASE WHEN s.status = '"+Constant.SYS_STATUS_PROCESS+"' THEN 0\n" +
                "           WHEN s.status = '"+Constant.SYS_STATUS_WAITING_SYNC+"' THEN 1\n" +
                "           WHEN s.status = '"+Constant.SYS_STATUS_SCHEDULE+"' THEN 2\n" +
                "           ELSE 3\n" +
                "      END \n," +
                "      (CASE WHEN s.status = '"+Constant.SYS_STATUS_PROCESS+"' THEN s.date_start\n" +
                "            ELSE  '31/12/9999 23:59'\n" +
                "       END) ASC,\n" +
                "      (CASE WHEN s.status = '"+Constant.SYS_STATUS_WAITING_SYNC+"' THEN s.date_end\n" +
                "            ELSE  '01/01/1900 00:00'\n" +
                "       END) DESC , \n" +
                "      s.custom_form_type, \n" +
                "      s.product_code, \n" +
                "      s.serial_id, \n" +
                "      s.schedule_prefix||s.schedule_code||s.schedule_exec\n" +
                ";")

            .toString()
            .replace("'null'","null");
    }
}
