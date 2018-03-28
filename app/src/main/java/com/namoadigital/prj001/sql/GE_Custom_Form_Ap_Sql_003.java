package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 * <p>
 * Selecion todos ap.
 */

public class GE_Custom_Form_Ap_Sql_003 implements Specification {

    private long customer_code;
    private String status_filter;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(GE_Custom_Form_ApDao.columns);

    public GE_Custom_Form_Ap_Sql_003(long customer_code, boolean filter_edit, boolean filter_process,boolean filter_waiting_action,boolean filter_done,boolean filter_cancelled) {
        this.customer_code = customer_code;
            String inFilter = "";
                inFilter += filter_edit ? "'" + Constant.SYS_STATUS_EDIT + "'," : "";
                inFilter += filter_process ? "'" + Constant.SYS_STATUS_PROCESS + "',"  : "";
                inFilter += filter_waiting_action ? "'" + Constant.SYS_STATUS_WAITING_ACTION + "'," : "";
                /*inFilter += filter_done ? "'" + Constant.SYS_STATUS_DONE + "'," : "";
                inFilter += filter_cancelled ? "'" + Constant.SYS_STATUS_CANCELLED + "'," : "";*/
                inFilter =  inFilter.length() > 0 ? inFilter.substring(0,inFilter.length()-1) : "";
                //
                status_filter = inFilter.length() > 0 ? "  and a.ap_status in ("+inFilter+") \n" : "";
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n " +
                        "   a.*\n" +
                        " FROM\n   " +
                        GE_Custom_Form_ApDao.TABLE + " a\n" +
                        " WHERE \n" +
                        "   a.customer_code = '"+customer_code+"'\n" +
                            status_filter +
                        "   and a.ap_status not in('" + Constant.SYS_STATUS_CANCELLED + "','" + Constant.SYS_STATUS_DONE + "')\n"+
                        " ORDER BY \n" +
                        "   CASE WHEN a.ap_when IS NULL THEN 0\n" +
                        "        ELSE a.ap_when\n" +
                        "   END,\n" +
                        "   CASE WHEN a.ap_status = '"+Constant.SYS_STATUS_EDIT+"' THEN 0\n" +
                        "        WHEN a.ap_status = '"+Constant.SYS_STATUS_PROCESS+"' THEN 1\n" +
                        "        WHEN a.ap_status = '"+Constant.SYS_STATUS_WAITING_ACTION+"' THEN 2\n" +
                        "        WHEN a.ap_status = '"+Constant.SYS_STATUS_DONE+"' THEN 3\n" +
                        "        ELSE 4\n" +
                        "   END\n"
                )
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
