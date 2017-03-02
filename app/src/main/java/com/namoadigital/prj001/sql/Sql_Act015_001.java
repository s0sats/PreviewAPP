package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public class Sql_Act015_001 implements Specification {

    private long s_customer_code;

    public Sql_Act015_001(long s_customer_code) {
        this.s_customer_code = s_customer_code;
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
                        "  l.custom_form_status," +
                        "  l.serial_id "+
                        " \n" +
                        "  FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE+ " l\n" +
                        "  WHERE\n" +
                        "    l."+GE_Custom_Form_LocalDao.CUSTOMER_CODE+" = '"+s_customer_code+"' " +
                        "    AND l.custom_form_status = '"+Constant.CUSTOM_FORM_STATUS_SENT+"'" +
                        "  ORDER BY" +
                        "    l.custom_form_type, " +
                        "    l.custom_product_code, " +
                        "    l.serial_id, \n" +
                        "    l.custom_form_data \n" +
                        ";")
                .append("customer_code#custom_form_type#custom_form_type_desc#" +
                        "custom_form_code#custom_form_version#custom_form_desc#" +
                        "custom_product_code#custom_product_desc#custom_form_data#" +
                        "custom_form_status#serial_id")
                .toString();
    }
}
