package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

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
    private String s_filter;
    private String sqlite_date_format;

    public Sql_Act013_001(long s_customer_code, boolean filter_in_processing , boolean filter_finalized , boolean filter_scheduled, Context context) {
        this.s_customer_code = s_customer_code;
        this.s_filter = "";
        this.sqlite_date_format = ToolBox_Inf.nlsDate2SqliteDate(context);

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
            //LUCHE - 17/02/2020
            //Se somente filtro por agendendamento, então além do status, filtra por quem tem pk
            if(!filter_in_processing && !filter_finalized && filter_scheduled ){
                s_filter += "  AND schedule_prefix is not null \n" +
                            "  AND schedule_code is not null \n" +
                            "  AND schedule_exec is not null \n";
            }
        }else{
            //Se todos os filtros falsos, não filtra nada.
            s_filter += " AND l.custom_form_status NOT in(" +
                    "'"+ Constant.SYS_STATUS_IN_PROCESSING+"',"+
                    "'"+Constant.SYS_STATUS_FINALIZED+"'," +
                    "'"+Constant.SYS_STATUS_SCHEDULE+"' "+
                    ")\n";

        }

    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
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
                        //"  l.site_code" +
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
                        s_filter +
                        "  ORDER BY\n" +
                        "      CASE WHEN l.custom_form_status = '"+Constant.SYS_STATUS_IN_PROCESSING+"' THEN 0\n" +
                        "           WHEN l.custom_form_status = '"+Constant.SYS_STATUS_FINALIZED+"' THEN 1\n" +
                        "           WHEN l.custom_form_status = '"+Constant.SYS_STATUS_SCHEDULE+"' THEN 2\n" +
                        "           ELSE 3\n" +
                        "      END ," +
                        "      (CASE WHEN l.custom_form_status = '"+Constant.SYS_STATUS_IN_PROCESSING+"' THEN d.date_start\n" +
                        "            ELSE  '31/12/9999 23:59'\n" +
                        "       END) ASC,\n" +
                        "      (CASE WHEN l.custom_form_status = '"+Constant.SYS_STATUS_FINALIZED+"' THEN d.date_end\n" +
                        "            ELSE  '01/01/1900 00:00'\n" +
                        "       END) DESC , \n" +
                        "      l.custom_form_type, \n" +
                        "      l.custom_product_code, \n" +
                        "      l.serial_id, \n" +
                        "      l.custom_form_data \n" +
                        "\n;")

                .toString();
    }

}
