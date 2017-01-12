package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_Product_HMAux_Lista_SqlSpecification implements Specification {

    private String s_customer_code;
    private String s_formtype_code;
    private String s_form_code;
    private String s_formversion_code;

    public GE_Custom_Form_Product_HMAux_Lista_SqlSpecification(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(GE_Custom_Form_ProductDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(GE_Custom_Form_ProductDao.CUSTOMER_CODE)
                .append(" ='")
                .append(s_customer_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(GE_Custom_Form_ProductDao.CUSTOM_FORM_TYPE)
                .append(" ='")
                .append(s_formtype_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(GE_Custom_Form_ProductDao.CUSTOM_FORM_CODE)
                .append(" ='")
                .append(s_form_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(GE_Custom_Form_ProductDao.CUSTOM_FORM_VERSION)
                .append(" ='")
                .append(s_formversion_code)
                .append("'")
                .append(" )")
                .append(" )")
                .append(" order by ")
                .append(" customer_code, custom_form_type, custom_form_code, custom_form_version, product_code ")
                .append(";")
                .append("customer_code#custom_form_type#custom_form_code#custom_form_version#product_code")
                .toString();
    }


}
