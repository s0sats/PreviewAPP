package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by DANIEL.LUCHE on 09/02/2017.
 *
 * LUCHE 17/02/2020
 *
 * Modificado query, removimendo o campos custom_form_data_serv e adiconando os campos da pk do novo
 * agendamento (md_schedule_exec)
 *
 * LUCHE - 25/03/2020
 * Modificado query do getScheduleQuery e getFormQuery, para retornar tb as informações de fcm e error_msg
 *
 * LUCHE - 30/03/2020
 * Modificado query substituindo o status de finalized pelo waiting_sync e status sent pelo done
 *
 * LUCHE - 29/04/2020
 * Modificado queries para usar os novos campos de produto do MD_Schedule_Exec.
 */

public class Sql_Act013_001 implements Specification {

    private long s_customer_code;
    private String subQuery;
    private String customerGMT;


    public Sql_Act013_001(long s_customer_code, boolean filter_in_processing , boolean filter_waiting_sync , boolean filter_scheduled, Context context) {
        this.s_customer_code = s_customer_code;
        this.customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
        String s_filter = "";
        String scheduleQuery = "";
        String formQuery = "";

        if(filter_in_processing || filter_waiting_sync || filter_scheduled){
            String status =  "";
            if(filter_in_processing){
                status += "'"+ ConstantBaseApp.SYS_STATUS_IN_PROCESSING+"',";
            }
            if(filter_waiting_sync){
                status += "'"+ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"',";
            }
            if(filter_scheduled){
                status += "'"+ConstantBaseApp.SYS_STATUS_SCHEDULE+"',";
            }
            status = status.substring(0,status.length() -1);
            s_filter += "   AND l.custom_form_status in(" +status+")\n";
        }else{
            //Se todos os filtros falsos, não filtra nada.
            s_filter += "   AND 1 = 0";
            /*
            //LUCHE - 31/03/2020 - Quando nada for filtrado, filtrar tudo...
            s_filter += " AND l.custom_form_status in(" +
                    "'"+ ConstantBaseApp.SYS_STATUS_IN_PROCESSING+"',"+
                    "'"+ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"'," +
                    "'"+ConstantBaseApp.SYS_STATUS_SCHEDULE+"' "+
                    ")\n";*/

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
            "    e.require_location,\n" +
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
            "    e.schedule_exec,\n" +
            "    e.fcm_new_status,\n" +
            "    e.fcm_user_nick,\n" +
            "    e.schedule_erro_msg\n," +
            "    e.serial_rule\n," +
            "    e.serial_max_length\n," +
            "    e.serial_min_length\n," +
            "    e.local_control\n," +
            "    e.io_control\n," +
            "    e.site_restriction\n," +
            "    e.product_icon_name\n," +
            "    e.product_icon_url\n" +
            "  FROM\n" +
            "   "+ MD_Schedule_ExecDao.TABLE +" e     \n" +
            "  WHERE\n" +
            "    e.customer_code = '"+s_customer_code+"'       \n" +
            "    AND e.schedule_type = '"+ConstantBaseApp.MD_SCHEDULE_TYPE_FORM+"' \n" +
            "    AND NOT EXISTS (SELECT 1\n" +
            "                    FROM ge_custom_forms_local l\n" +
            "                    WHERE l.customer_code = e.customer_code\n" +
            "                          AND l.schedule_prefix = e.schedule_prefix\n" +
            "                          AND l.schedule_code = e.schedule_code \n" +
            "                          AND l.schedule_exec =  e.schedule_exec\n" +
            //"                          AND l.custom_form_status <> '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE +"'\n" +
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
            "  l.require_location,\n" +
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
            "  CASE WHEN LENGTH(l.site_code) <> 0 AND l.site_code > 0\n" +
            "       THEN l.site_code\n" +
            "       ELSE d.site_code\n" +
            "  END "+MD_SiteDao.SITE_CODE +",\n" +
            "  CASE WHEN LENGTH(l.site_id) <> 0 AND l.site_code > 0\n" +
            "       THEN l.site_id\n" +
            "       ELSE s.site_id\n" +
            "  END "+MD_SiteDao.SITE_ID +",\n" +
            "  CASE WHEN LENGTH(l.site_desc) <> 0 AND l.site_code > 0\n" +
            "       THEN l.site_desc\n" +
            "       ELSE s.site_desc\n" +
            "  END "+MD_SiteDao.SITE_DESC +",\n"+
            "  l.schedule_prefix,\n"+
            "  l.schedule_code,\n"+
            "  l.schedule_exec,\n"+
            "  sc.fcm_new_status,\n" +
            "  sc.fcm_user_nick,\n" +
            "  sc.schedule_erro_msg,\n" +
            "  l.serial_rule,\n" +
            "  l.serial_max_length,\n" +
            "  l.serial_min_length,\n" +
            "  l.local_control,\n" +
            //O Alias no campo abaixo é apenas pois na tabela de agendamento não há prefixo product
            "  l.product_io_control "+GE_Custom_Form_LocalDao.IO_CONTROL+",\n" +
            "  l.site_restriction,\n" +
            "  l.custom_product_icon_name "+MD_Schedule_ExecDao.PRODUCT_ICON_NAME+",\n" +
            "  l.custom_product_icon_url "+MD_Schedule_ExecDao.PRODUCT_ICON_URL+"\n" +
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
            "  LEFT JOIN\n" +
            "       " + MD_Schedule_ExecDao.TABLE+ " sc ON\n " +
            "      l.schedule_prefix = sc.schedule_prefix\n" +
            "      AND l.schedule_code = sc.schedule_code\n" +
            "      AND l.schedule_exec = sc.schedule_exec\n" +
            "  WHERE\n" +
            "      l."+GE_Custom_Form_LocalDao.CUSTOMER_CODE+" = '"+s_customer_code+"' \n" +
            "      AND l.custom_form_status <> '" + ConstantBaseApp.SYS_STATUS_DONE+"'\n" +
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
                        "      CASE WHEN t.custom_form_status = '"+ConstantBaseApp.SYS_STATUS_IN_PROCESSING+"' THEN 0\n" +
                        "           WHEN t.custom_form_status = '"+ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"' THEN 1\n" +
                        "           WHEN t.custom_form_status = '"+ConstantBaseApp.SYS_STATUS_SCHEDULE+"' THEN 2\n" +
                        "           ELSE 3\n" +
                        "      END ," +
                        "      (CASE WHEN t.custom_form_status = '"+ConstantBaseApp.SYS_STATUS_IN_PROCESSING+"' THEN t.date_start\n" +
                        "            ELSE  '31/12/9999 23:59'\n" +
                        "       END) ASC,\n" +
                        "      (CASE WHEN t.custom_form_status = '"+ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"' THEN t.date_end\n" +
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
