package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_UserDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 23/02/2018.
 */

public class MD_User_Sql_002 implements Specification {

    private String customer_code;
    private String user_code;

    public MD_User_Sql_002(String customer_code, String user_code) {
        this.customer_code = customer_code;
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT \n" +
                                "   *,  \n" +
                                " FROM \n" +
                                MD_UserDao.TABLE + "\n" +
                                " WHERE " +
                                MD_UserDao.CUSTOMER_CODE + " = '" + customer_code + "' " +
                                " AND " +
                                MD_UserDao.USER_CODE + " = '" + user_code + "' " +
                                " ORDER BY " +
                                "      user_nick")
                .append(";")
                .toString();
    }
}
