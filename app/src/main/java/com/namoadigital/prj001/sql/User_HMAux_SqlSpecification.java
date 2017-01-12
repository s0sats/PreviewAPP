package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.UserDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class User_HMAux_SqlSpecification implements Specification {

    public User_HMAux_SqlSpecification() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select user_code, user_nick, email_p from ")
                .append(UserDao.TABLE)
                .append(";")
                .append("user_code#user_nick#email_p")
                .toString();
    }
}
