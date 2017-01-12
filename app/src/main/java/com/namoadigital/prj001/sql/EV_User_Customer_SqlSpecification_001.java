package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 11/01/17.
 */

public class EV_User_Customer_SqlSpecification_001 implements Specification {

    private String s_user_code;
    private String s_customer_code;


    public EV_User_Customer_SqlSpecification_001(String s_user_code, String s_customer_code) {
        this.s_user_code = s_user_code;
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(EV_User_CustomerDao.TABLE)
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
                .append(EV_User_CustomerDao.CUSTOMER_CODE)
                .append(" ='")
                .append(s_customer_code)
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
                .toString();
    }
}
