package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 09/02/2017.
 */

public class Sql_Act013_001 implements Specification {

    private long s_customer_code;
    private String s_filter;

    public Sql_Act013_001(long s_customer_code,boolean filter_in_processing ,boolean filter_finalized , boolean filter_scheduled) {
        this.s_customer_code = s_customer_code;
        this.s_filter = "";

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
                        "  l.serial_id "+
                        " \n" +
                        "  FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE+ " l\n" +
                        "  WHERE\n" +
                        "   l."+GE_Custom_Form_LocalDao.CUSTOMER_CODE+" = '"+s_customer_code+"' " +
                        "   AND l.custom_form_status <> '" + Constant.CUSTOM_FORM_STATUS_SENT+"'" +
                        s_filter +
                        "  ORDER BY\n" +
                        "      CASE WHEN l.custom_form_status = '"+Constant.CUSTOM_FORM_STATUS_IN_PROCESSING+"' THEN 0\n" +
                        "           WHEN l.custom_form_status = '"+Constant.CUSTOM_FORM_STATUS_FINALIZED+"' THEN 1\n" +
                        "           WHEN l.custom_form_status = '"+Constant.CUSTOM_FORM_STATUS_SCHEDULED+"' THEN 2\n" +
                        "           ELSE 3\n" +
                        "      END , \n" +
                        "      l.custom_form_type, \n" +
                        "      l.custom_product_code, \n" +
                        "      l.serial_id, \n" +
                        "      l.custom_form_data " +
                        ";")
                .append("customer_code#custom_form_type#custom_form_type_desc#" +
                        "custom_form_code#custom_form_version#custom_form_desc#" +
                        "custom_product_code#custom_product_desc#custom_form_data#" +
                        "custom_form_status#serial_id")
                .toString();
    }
}
