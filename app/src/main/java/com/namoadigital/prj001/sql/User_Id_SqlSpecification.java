package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.UserDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class User_Id_SqlSpecification implements Specification {

    private String s_User_Code;

    public User_Id_SqlSpecification(String s_User_Code) {
        this.s_User_Code = s_User_Code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(UserDao.TABLE)
                .append(" where ")
                .append(UserDao.USER_CODE)
                .append(" ='")
                .append(s_User_Code)
                .append("'")
                .append(";")
                .toString();
    }
}
