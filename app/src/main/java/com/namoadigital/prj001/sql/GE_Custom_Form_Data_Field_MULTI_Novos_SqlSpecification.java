package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_Data_Field_MULTI_Novos_SqlSpecification implements Specification {

    private String s_user_code;
    private String s_customer_code;

    //and cab.token != ''

    public GE_Custom_Form_Data_Field_MULTI_Novos_SqlSpecification(String s_user_code, String s_customer_code) {
        this.s_user_code = s_user_code;
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ge_custom_form_data_fields as items where items.custom_form_data in (")
                //
                .append(" select custom_form_data from ")
                .append(GE_Custom_Form_DataDao.TABLE)
                .append(" as cab ")
                .append(" where ")
                .append(" (")
                //
                .append(" (")
                .append(GE_Custom_Form_DataDao.USER_CODE_END)
                .append(" ='")
                .append(s_user_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(GE_Custom_Form_DataDao.CUSTOMER_CODE)
                .append(" ='")
                .append(s_customer_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS)
                .append(" ='")
                .append(0)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(GE_Custom_Form_DataDao.TOKEN)
                .append(" ='")
                .append("")
                .append("'")
                .append(" )")
                //
                .append(" )")
                //
                .append(" )")
                .append(" order by customer_code, custom_form_type, custom_form_code, custom_form_version, custom_form_data, custom_form_seq ")
                .append(";")
                .toString();
    }
}
