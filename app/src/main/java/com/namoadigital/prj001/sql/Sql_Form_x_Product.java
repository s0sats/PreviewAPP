package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 12/05/2017.
 */

public class Sql_Form_x_Product implements Specification {

    public static final String FORM_PRODUCT_PROFILE = "form_operation_profile";

    private long customer_code;
    private long product_code;

    public Sql_Form_x_Product(long customer_code, long product_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append(   "   SELECT\n" +
                            "         count(1) "+ FORM_PRODUCT_PROFILE +" \n" +
                            "   FROM\n" +
                                GE_Custom_FormDao.TABLE +" f\n " +
                            "   WHERE\n" +
                            "       f.customer_code = '"+customer_code+"' \n" +
                            "       and (f.all_product = 1\n" +
                            "             or EXISTS(   SELECT\n" +
                            "                               1\n" +
                            "                          FROM\n" +
                                                            GE_Custom_Form_ProductDao.TABLE +" p\n " +
                            "                           WHERE\n" +
                            "                             p.customer_code = f.customer_code\n" +
                            "                             and p.custom_form_type = f.custom_form_type\n" +
                            "                             and p.custom_form_code = f.custom_form_code\n" +
                            "                             and p.custom_form_version = f.custom_form_version\n" +
                            "                             and p.product_code = '"+product_code+"')\n" +
                            "   ) \n")
                .append(";")
                //.append(FORM_PRODUCT_PROFILE)
                .toString();
    }
}
