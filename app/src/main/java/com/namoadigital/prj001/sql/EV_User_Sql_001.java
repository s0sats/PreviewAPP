package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 09/05/2017.
 */

public class EV_User_Sql_001 implements Specification {

    private String user_code;

    public EV_User_Sql_001(String user_code) {
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT\n" +
                        "  U.*\n" +
                        "FROM\n" +
                        EV_UserDao.TABLE + " U\n" +
                        "WHERE\n" +
                        "  U.user_code = '"+user_code+"'")
                .toString();
    }
}
