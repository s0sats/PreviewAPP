package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by DANIEL.LUCHE on 09/02/2017.
 *
 * LUCHE 17/02/2020
 *
 * Modificado query, removimendo o campos custom_form_data_serv e adiconando os campos da pk do novo
 * agendamento (md_schedule_exec)
 *
 */

public class Sql_Act013_001 implements Specification {

    private long s_customer_code;
    private String subQuery;
    private String customerGMT;


    public Sql_Act013_001(long s_customer_code, boolean filter_in_processing , boolean filter_finalized , boolean filter_scheduled, Context context) {
        this.s_customer_code = s_customer_code;
        this.customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
        String s_filter = "";
        String scheduleQuery = "";
        String formQuery = "";

        if(filter_in_processing || filter_finalized || filter_scheduled){

            String status =  "";

            if(filter_in_processing){
                status += "'"+ Constant.SYS_STATUS_IN_PROCESSING+"',";
            }
            if(filter_finalized){
                status += "'"+Constant.SYS_STATUS_FINALIZED+"',";
            }
            if(filter_scheduled){
                status += "'"+Constant.SYS_STATUS_SCHEDULE+"',";
            }
            status = status.substring(0,status.length() -1);
            s_filter += "   AND l.custom_form_status in(" +status+")\n";
        }else{
            //Se todos os filtros falsos, não filtra nada.
            s_filter += " AND l.custom_form_status NOT in(" +
                    "'"+ Constant.SYS_STATUS_IN_PROCESSING+"',"+
                    "'"+Constant.SYS_STATUS_FINALIZED+"'," +
                    "'"+Constant.SYS_STATUS_SCHEDULE+"' "+
                    ")\n";
        }
        //
        scheduleQuery = getScheduleQuery(filter_scheduled);
        formQuery = getFormQuery(s_filter);
        subQuery = !scheduleQuery.isEmpty() ? scheduleQuery +"\n UNION\n"+ formQuery : formQuery;
    }


    private String getScheduleQuery(boolean filter_scheduled) {
        return !filter_scheduled
            ? ""
            : "  SELECT\n" +
            "    e.customer_code,\n" +
            "    e.custom_form_type,\n" +
            "    e.custom_form_type_desc,\n" +
            "    e.custom_form_code,\n" +
            "    e.custom_form_version,\n" +
            "    e.custom_form_desc,\n" +
            "    e.product_code custom_product_code,\n" +
            "    e.product_desc custom_product_desc,\n" +
            "    e.product_id custom_product_id, \n" +
            "    null custom_form_data,\n" +
            "    e.status custom_form_status, \n" +
            "    e.serial_id,\n" +
            "    null so_prefix,\n" +
            "    null so_code,\n" +
            "    null date_start,\n" +
            "    null date_end,\n" +
            "    e.date_start||' "+customerGMT+"' schedule_date_start_format,\n" +
            "    e.date_end||' "+customerGMT+"' schedule_date_end_format,\n" +
            "    e.comments schedule_comments,\n" +
            "    e.require_serial_done,\n" +
            "    e.require_serial,\n" +
            "    e.allow_new_serial_cl,\n" +
            "    e.site_code,\n" +
            "    e.site_id,\n" +
            "    e.site_desc,\n" +
            "    e.schedule_prefix,\n" +
            "    e.schedule_code,\n" +
            "    e.schedule_exec\n" +
            "  FROM\n" +
            "   "+ MD_Schedule_ExecDao.TABLE +" e     \n" +
            "  WHERE\n" +
            "    e.customer_code = '"+s_customer_code+"'       \n" +
            "    AND NOT EXISTS (SELECT 1\n" +
            "                    FROM ge_custom_forms_local l\n" +
            "                    WHERE l.customer_code = e.customer_code\n" +
            "                          AND l.schedule_prefix = e.schedule_prefix\n" +
            "                          AND l.schedule_code = e.schedule_code \n" +
            "                          AND l.schedule_exec =  e.schedule_exec\n" +
            "                       )";
    }


    private String getFormQuery(String s_filter) {
            return
            " SELECT\n" +
            "  l.customer_code,\n" +
            "  l.custom_form_type,\n" +
            "  l.custom_form_type_desc,\n" +
            "  l.custom_form_code,\n" +
            "  l.custom_form_version,\n" +
            "  l.custom_form_desc,\n" +
            "  l.custom_product_code,\n" +
            "  l.custom_product_desc,\n" +
            "  l.custom_product_id,\n" +
            "  l.custom_form_data,\n" +
            "  l.custom_form_status,\n" +
            // "  l.serial_id,\n" +
            "  CASE WHEN LENGTH(l.serial_id) <> 0 \n" +
            "       THEN L.serial_id\n" +
            "       ELSE d.serial_id\n" +
            "  END  serial_id,\n" +
            "  d.so_prefix,\n" +
            "  d.so_code,\n" +
            "  d.date_start,\n" +
            "  d.date_end,\n" +
            "  l.schedule_date_start_format,\n "+
            "  l.schedule_date_end_format,\n"+
            "  l.schedule_comments,\n" +
            "  l.require_serial_done,\n" +
            "  l.require_serial,\n" +
            "  l.allow_new_serial_cl,\n" +
            "  CASE WHEN LENGTH(l.site_code) <> 0\n" +
            "       THEN l.site_code\n" +
            "       ELSE d.site_code\n" +
            "  END "+MD_SiteDao.SITE_CODE +",\n" +
            "  CASE WHEN LENGTH(l.site_id) <> 0\n" +
            "       THEN l.site_id\n" +
            "       ELSE s.site_id\n" +
            "  END "+MD_SiteDao.SITE_ID +",\n" +
            "  CASE WHEN LENGTH(l.site_desc) <> 0\n" +
            "       THEN l.site_desc\n" +
            "       ELSE s.site_desc\n" +
            "  END "+MD_SiteDao.SITE_DESC +",\n"+
            "  l.schedule_prefix,\n"+
            "  l.schedule_code,\n"+
            "  l.schedule_exec\n"+
            "  FROM\n" +
            "   " + GE_Custom_Form_LocalDao.TABLE+ " l\n" +
            "  LEFT JOIN " + GE_Custom_Form_DataDao.TABLE+ " d ON \n" +
            "        l.customer_code = d.customer_code  \n" +
            "        AND l.custom_form_type = d.custom_form_type\n" +
            "        AND l.custom_form_code = d.custom_form_code\n" +
            "        AND l.custom_form_version = d.custom_form_version\n" +
            "        AND l.custom_form_data = d.custom_form_data\n" +
            "  LEFT JOIN "+MD_SiteDao.TABLE+" s ON \n" +
            "        d.customer_code = s.customer_code\n" +
            "      AND d.site_code = s.site_code\n " +
            "  WHERE\n" +
            "      l."+GE_Custom_Form_LocalDao.CUSTOMER_CODE+" = '"+s_customer_code+"' \n" +
            "      AND l.custom_form_status <> '" + Constant.SYS_STATUS_SENT+"'\n" +
            s_filter;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append("  SELECT\n" +
                        "       t.*" +
                        "  FROM ( \n" +
                        subQuery +"\n" +
                        "  ) t\n" +
                        "  ORDER BY\n" +
                        "      CASE WHEN t.custom_form_status = '"+Constant.SYS_STATUS_IN_PROCESSING+"' THEN 0\n" +
                        "           WHEN t.custom_form_status = '"+Constant.SYS_STATUS_FINALIZED+"' THEN 1\n" +
                        "           WHEN t.custom_form_status = '"+Constant.SYS_STATUS_SCHEDULE+"' THEN 2\n" +
                        "           ELSE 3\n" +
                        "      END ," +
                        "      (CASE WHEN t.custom_form_status = '"+Constant.SYS_STATUS_IN_PROCESSING+"' THEN t.date_start\n" +
                        "            ELSE  '31/12/9999 23:59'\n" +
                        "       END) ASC,\n" +
                        "      (CASE WHEN t.custom_form_status = '"+Constant.SYS_STATUS_FINALIZED+"' THEN t.date_end\n" +
                        "            ELSE  '01/01/1900 00:00'\n" +
                        "       END) DESC , \n" +
                        "      t.custom_form_type, \n" +
                        "      t.custom_product_code, \n" +
                        "      t.serial_id, \n" +
                        "      t.custom_form_data \n" +
                        "\n;")

                .toString();
    }

}
