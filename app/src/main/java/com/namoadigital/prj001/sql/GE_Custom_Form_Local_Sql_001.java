package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_Local_Sql_001 implements Specification {

    private String s_customer_code;
    private String s_form_type;
    private String s_form_code;
    private String s_form_version;
    private String s_form_status;

    public GE_Custom_Form_Local_Sql_001(String s_customer_code, String s_form_type, String s_form_code, String s_form_version, String s_form_status) {
        this.s_customer_code = s_customer_code;
        this.s_form_type = s_form_type;
        this.s_form_code = s_form_code;
        this.s_form_version = s_form_version;
        this.s_form_status = s_form_status;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(GE_Custom_Form_LocalDao.TABLE)
                .append(" where ")
                .append(" (")

                .append(" (")
                .append(GE_Custom_Form_LocalDao.CUSTOMER_CODE)
                .append(" ='")
                .append(s_customer_code)
                .append("'")
                .append(" )")
                .append(" and ")

                .append(" (")
                .append(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE)
                .append(" ='")
                .append(s_form_type)
                .append("'")
                .append(" )")
                .append(" and ")

                .append(" (")
                .append(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE)
                .append(" ='")
                .append(s_form_code)
                .append("'")
                .append(" )")
                .append(" and ")

                .append(" (")
                .append(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION)
                .append(" ='")
                .append(s_form_version)
                .append("'")
                .append(" )")
                .append(" and ")

                .append(" (")
                .append(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)
                .append(" ='")
                .append(s_form_status)
                .append("'")
                .append(" )")

                .append(" )")
                .append(";")
                .toString();
    }
}
