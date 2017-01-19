package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class EV_User_Customer_Sql_001 implements Specification {

    private String s_user_code;

    public EV_User_Customer_Sql_001(String s_user_code) {
        this.s_user_code = s_user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT " +
                        "      customer_code, " +
                        "      customer_name, " +
                        "      translate_code, " +
                        "      blocked, " +
                        "      session_app," +
                        "      pending " +
                        " FROM ")
                .append(EV_User_CustomerDao.TABLE)
                .append(" WHERE " +
                             EV_User_CustomerDao.USER_CODE +" = '"+s_user_code+"' " +
                        " ORDER by customer_name;")
                .append("customer_code#customer_name#translate_code#blocked#session_app#pending")
                .toString();
    }
}
