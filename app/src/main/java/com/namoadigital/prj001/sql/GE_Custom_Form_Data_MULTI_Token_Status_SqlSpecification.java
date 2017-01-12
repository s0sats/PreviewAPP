package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_Data_MULTI_Token_Status_SqlSpecification implements Specification {

    private String token;
    private String s_user_code;
    private String s_customer_code;

    public GE_Custom_Form_Data_MULTI_Token_Status_SqlSpecification(String token, String s_user_code, String s_customer_code) {
        this.token = token;
        this.s_user_code = s_user_code;
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();


        return sb
                .append(" update ")
                .append(GE_Custom_Form_DataDao.TABLE)
                .append(" set ")
                .append(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS)
                .append(" ='")
                .append("1")
                .append("'")
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
                .append(GE_Custom_Form_DataDao.TOKEN)
                .append(" ='")
                .append(token)
                .append("'")
                .append(" )")
                //
                .append(" )")
                .append(";")
                .toString();
    }
}
