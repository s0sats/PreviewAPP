package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_Field_HMAux_SqlSpecification implements Specification {

    private String s_customer_code;
    private String s_formtype_code;
    private String s_form_code;
    private String s_formversion_code;

    public GE_Custom_Form_Field_HMAux_SqlSpecification(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select custom_form_seq, custom_form_data_type, custom_form_data_size, custom_form_mask, custom_form_data_content, custom_form_order_seq, required from ")
                .append(GE_Custom_Form_FieldDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(GE_Custom_Form_FieldDao.CUSTOMER_CODE)
                .append(" ='")
                .append(s_customer_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(GE_Custom_Form_FieldDao.CUSTOM_FORM_TYPE)
                .append(" ='")
                .append(s_formtype_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(GE_Custom_Form_FieldDao.CUSTOM_FORM_CODE)
                .append(" ='")
                .append(s_form_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(GE_Custom_Form_FieldDao.CUSTOM_FORM_VERSION)
                .append(" ='")
                .append(s_formversion_code)
                .append("'")
                .append(" )")
                .append(" )")
                .append(" order by ")
                .append(" custom_form_order_seq ")
                .append(";")
                .append("custom_form_seq#custom_form_data_type#custom_form_data_size#custom_form_mask#custom_form_data_content#custom_form_order_seq#required")
                .toString();
    }
}
