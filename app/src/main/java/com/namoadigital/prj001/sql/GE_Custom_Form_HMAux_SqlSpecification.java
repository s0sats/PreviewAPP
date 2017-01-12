package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_HMAux_SqlSpecification implements Specification {

    private String s_customer_code;
    private String s_formtype_code;

    public GE_Custom_Form_HMAux_SqlSpecification(String s_customer_code, String s_formtype_code) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select customer_code, custom_form_type, custom_form_code, custom_form_version, custom_form_status, 'FORM DESC' as custom_form_desc from ")
                .append(GE_Custom_FormDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(GE_Custom_FormDao.CUSTOMER_CODE)
                .append(" ='")
                .append(s_customer_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(GE_Custom_FormDao.CUSTOM_FORM_TYPE)
                .append(" ='")
                .append(s_formtype_code)
                .append("'")
                .append(" )")
                .append(" )")
                .append(" order by ")
                .append(" custom_form_desc, custom_form_code, custom_form_version ")
                .append(";")
                .append("customer_code#custom_form_type#custom_form_code#custom_form_version#custom_form_status#custom_form_desc")
                .toString();
    }
}
