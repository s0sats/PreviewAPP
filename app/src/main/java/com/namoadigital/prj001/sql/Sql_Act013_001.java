package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 09/02/2017.
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
                status += "'"+ Constant.CUSTOM_FORM_STATUS_IN_PROCESSING+"',";
            }
            if(filter_finalized){
                status += "'"+Constant.CUSTOM_FORM_STATUS_FINALIZED+"',";
            }
            if(filter_scheduled){
                status += "'"+Constant.CUSTOM_FORM_STATUS_SCHEDULED+"',";
            }
            status = status.substring(0,status.length() -1);
            s_filter += "AND l.custom_form_status in(" +status+")";
        }else{
            //Se todos os filtros falsos, não filtra nada.
            s_filter += "AND l.custom_form_status NOT in(" +
                    "'"+ Constant.CUSTOM_FORM_STATUS_IN_PROCESSING+"',"+
                    "'"+Constant.CUSTOM_FORM_STATUS_FINALIZED+"'," +
                    "'"+Constant.CUSTOM_FORM_STATUS_SCHEDULED+"' "+
                    ")";

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
                        "  l.custom_form_data,\n" +
                        "  l.custom_form_status,\n" +
                        "  l.serial_id,\n" +
                        "  strftime('"+sqlite_date_format+" %H:%M',d.date_start) date_start,\n" +
                        "  strftime('"+sqlite_date_format+" %H:%M',d.date_end) date_end," +
                        "  strftime('"+sqlite_date_format+" %H:%M',l.schedule_date_start_format) schedule_date_start_format, "+
                        "  strftime('"+sqlite_date_format+" %H:%M',l.schedule_date_end_format) schedule_date_end_format "+
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
                        "      AND l.custom_form_status <> '" + Constant.CUSTOM_FORM_STATUS_SENT+"'" +
                        s_filter +
                        "  ORDER BY\n" +
                        "      CASE WHEN l.custom_form_status = '"+Constant.CUSTOM_FORM_STATUS_IN_PROCESSING+"' THEN 0\n" +
                        "           WHEN l.custom_form_status = '"+Constant.CUSTOM_FORM_STATUS_FINALIZED+"' THEN 1\n" +
                        "           WHEN l.custom_form_status = '"+Constant.CUSTOM_FORM_STATUS_SCHEDULED+"' THEN 2\n" +
                        "           ELSE 3\n" +
                        "      END ," +
                        "      (CASE WHEN l.custom_form_status = '"+Constant.CUSTOM_FORM_STATUS_IN_PROCESSING+"' THEN d.date_start\n" +
                        "            ELSE  '31/12/9999 23:59'\n" +
                        "       END) ASC,\n" +
                        "      (CASE WHEN l.custom_form_status = '"+Constant.CUSTOM_FORM_STATUS_FINALIZED+"' THEN d.date_end\n" +
                        "            ELSE  '01/01/1900 00:00'\n" +
                        "       END) DESC , \n" +
                        "      l.custom_form_type, \n" +
                        "      l.custom_product_code, \n" +
                        "      l.serial_id, \n" +
                        "      l.custom_form_data" +
                        ";")
                .append("customer_code#custom_form_type#custom_form_type_desc#" +
                        "custom_form_code#custom_form_version#custom_form_desc#" +
                        "custom_product_code#custom_product_desc#custom_form_data#" +
                        "custom_form_status#serial_id#date_start#date_end#schedule_date_start_format#schedule_date_end_format")
                .toString();
    }

}
