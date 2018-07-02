package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 11/04/2017.
 */

public class Sql_Act017_001 implements Specification {

    private long s_customer_code;
    private String sqlite_date_format;
    private String selected_date;

    public Sql_Act017_001(Context context, long s_customer_code ,String selected_date) {
        this.s_customer_code = s_customer_code;
        this.sqlite_date_format = ToolBox_Inf.nlsDate2SqliteDate(context);
        this.selected_date = selected_date;

    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "'"+Constant.MODULE_CHECKLIST +"' "+ Act017_Main.ACT017_MODULE_KEY +" ,\n" +
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
                        "  l.site_code,\n" +
                        "  l.site_id,\n" +
                        "  l.site_desc,\n" +
                        //"  l.serial_id,\n" +
                        "  CASE WHEN LENGTH(l.serial_id) <> 0 \n" +
                        "       THEN L.serial_id\n" +
                        "       ELSE d.serial_id\n" +
                        "  END  serial_id,\n" +
                        "  l.custom_form_data_serv,\n" +
                        "  strftime('"+sqlite_date_format+" %H:%M',d.date_start,'localtime') date_start,\n" +
                        "  strftime('"+sqlite_date_format+" %H:%M',d.date_end,'localtime') date_end,\n" +
                        "  strftime('"+sqlite_date_format+" %H:%M',l.schedule_date_start_format,'localtime') schedule_date_start_format,\n"+
                        "  strftime('"+sqlite_date_format+" %H:%M',l.schedule_date_end_format,'localtime') schedule_date_end_format,\n"+
                        "  strftime('%Y-%m-%d',l.schedule_date_start_format,'localtime') "+Act017_Main.ACT017_ADAPTER_DATE_REF+",\n"+
                        "  l.require_serial,\n"+
                        "  l.allow_new_serial_cl\n"+
                        " \n" +
                        "  FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE+ " l\n" +
                        "LEFT JOIN " + GE_Custom_Form_DataDao.TABLE+ " d ON " +
                        "      l.customer_code = d.customer_code  " +
                        "      AND l.custom_form_type = d.custom_form_type\n" +
                        "      AND l.custom_form_code = d.custom_form_code\n" +
                        "      AND l.custom_form_version = d.custom_form_version\n" +
                        "      AND l.custom_form_data = d.custom_form_data\n" +
                        "  WHERE\n" +
                        "      l."+GE_Custom_Form_LocalDao.CUSTOMER_CODE+" = '"+s_customer_code+"' " +
                       // "      AND l.custom_form_status <> '" + Constant.SYS_STATUS_SENT+"'" +
                        "      AND l.custom_form_data_serv is not null \n" +
                        "      AND ('"+selected_date+"' is null or strftime('%Y-%m-%d',l.schedule_date_start_format,'localtime') = '"+selected_date+"') \n" +
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
                        "      l.custom_form_data" +
                        ";")
                .append( Act017_Main.ACT017_MODULE_KEY+"#")
                .append("customer_code#custom_form_type#custom_form_type_desc#" +
                        "custom_form_code#custom_form_version#custom_form_desc#" +
                        "custom_product_code#custom_product_desc#custom_product_id#custom_form_data#" +
                        "custom_form_status#serial_id#custom_form_data_serv#date_start#date_end#" +
                        "schedule_date_start_format#schedule_date_end_format#site_code#site_id#site_desc#require_serial#allow_new_serial_cl")
                .toString()
                .replace("'null'","null");


    }
}
