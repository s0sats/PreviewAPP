package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public class Sql_Act015_001 implements Specification {

    private long s_customer_code;
    private String sqlite_date_format;

    public Sql_Act015_001(long s_customer_code, Context context) {
        this.s_customer_code = s_customer_code;
        this.sqlite_date_format = ToolBox_Inf.nlsDate2SqliteDate(context);
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
                        "  l.custom_form_status," +

                        "  d.so_prefix,\n" +
                        "  d.so_code,\n" +

                        "    CASE WHEN LENGTH(l.serial_id) <> 0 \n" +
                        "       THEN L.serial_id\n" +
                        "       ELSE d.serial_id\n" +
                        "  END  serial_id,"+
                        "  l.custom_form_data_serv,"+
                        "  strftime('"+sqlite_date_format+" %H:%M',d.date_start,'localtime') date_start,\n" +
                        "  strftime('"+sqlite_date_format+" %H:%M',d.date_end,'localtime') date_end, "+
                        "  strftime('"+sqlite_date_format+" %H:%M',l.schedule_date_start_format,'localtime') schedule_date_start_format,\n"+
                        "  strftime('"+sqlite_date_format+" %H:%M',l.schedule_date_end_format,'localtime') schedule_date_end_format\n"+
                        " \n" +
                        "  FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE+ " l\n," +
                        GE_Custom_Form_DataDao.TABLE+ " d\n " +
                        "  WHERE\n" +
                        "      l.customer_code = d.customer_code\n" +
                        "      AND l.custom_form_type = d.custom_form_type\n" +
                        "      AND l.custom_form_code = d.custom_form_code\n" +
                        "      AND l.custom_form_version = d.custom_form_version\n" +
                        "      AND l.custom_form_data = d.custom_form_data\n" +
                        "      AND l."+GE_Custom_Form_LocalDao.CUSTOMER_CODE+" = '"+s_customer_code+"' " +
                        "      AND l.custom_form_status = '"+ Constant.SYS_STATUS_SENT+"'" +
                        "  ORDER BY" +
                        "    d.date_end desc," +
                        "    l.custom_form_type, " +
                        "    l.custom_product_code, " +
                        "    l.serial_id, \n" +
                        "    l.custom_form_data \n" +
                        ";")
                .append("customer_code#custom_form_type#custom_form_type_desc#" +
                        "custom_form_code#custom_form_version#custom_form_desc#" +
                        "custom_product_code#custom_product_desc#custom_product_id#custom_form_data#" +
                        "custom_form_status#serial_id#custom_form_data_serv#date_start#date_end#" +
                        "schedule_date_start_format#schedule_date_end_format#so_prefix#so_code")
                .toString();
    }
}
