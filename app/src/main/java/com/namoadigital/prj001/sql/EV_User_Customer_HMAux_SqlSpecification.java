package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.UserDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class EV_User_Customer_HMAux_SqlSpecification implements Specification {

    private String s_user_code;

    public EV_User_Customer_HMAux_SqlSpecification(String s_user_code) {
        this.s_user_code = s_user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select user_code, customer_code, customer_name, translate_code from ")
                .append(UserDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(EV_User_CustomerDao.USER_CODE)
                .append(" ='")
                .append(s_user_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(EV_User_CustomerDao.ACTIVE)
                .append(" ='")
                .append(1)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(EV_User_CustomerDao.BLOCKED)
                .append(" ='")
                .append(0)
                .append("'")
                .append(" )")
                .append(" )")
                .append(";")
                .append("user_code#customer_code#customer_name#translate_code")
                .toString();
    }
}
