package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_SiteDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 12/05/2017.
 */

public class Sql_Form_x_Site implements Specification {

    public static final String FORM_SITE_PROFILE = "form_operation_profile";

    private long customer_code;
    private String site_code;

    public Sql_Form_x_Site(long customer_code, String site_code) {
        this.customer_code = customer_code;
        this.site_code = site_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append(   "   SELECT\n" +
                            "         count(1) "+ FORM_SITE_PROFILE +" \n" +
                            "   FROM\n" +
                                GE_Custom_FormDao.TABLE +" f\n " +
                            "   WHERE\n" +
                            "       f.customer_code = '"+customer_code+"' \n" +
                            "       and (f.all_site = 1\n" +
                            "             or EXISTS(   SELECT\n" +
                            "                               1\n" +
                            "                          FROM\n" +
                                                            GE_Custom_Form_SiteDao.TABLE +" s\n " +
                            "                           WHERE\n" +
                            "                             s.customer_code = f.customer_code\n" +
                            "                             and s.custom_form_type = f.custom_form_type\n" +
                            "                             and s.custom_form_code = f.custom_form_code\n" +
                            "                             and s.custom_form_version = f.custom_form_version\n" +
                            "                             and s.site_code = '"+site_code+"')\n" +
                            "   ) \n")
                .append(";")
                .append(FORM_SITE_PROFILE)
                .toString();
    }
}
