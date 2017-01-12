package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.UserDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class User_EMail_PSql_Specification implements Specification {

    private String s_EMail_P;

    public User_EMail_PSql_Specification(String s_EMail_P) {
        this.s_EMail_P = s_EMail_P;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(UserDao.TABLE)
                .append(" where ")
                .append(UserDao.EMAIL_P)
                .append(" ='")
                .append(s_EMail_P)
                .append("'")
                .append(";")
                .toString();
    }
}
