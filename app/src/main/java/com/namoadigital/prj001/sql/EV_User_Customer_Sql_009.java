package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 22/02/2018.
 *
 * Query que limpa a sessão de todos os customers do user
 *
 */

public class EV_User_Customer_Sql_009 implements Specification {

    private String user_code;

    public EV_User_Customer_Sql_009(String user_code) {
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE "+EV_User_CustomerDao.TABLE+" SET \n " +
                        "     session_app = ''\n" +
                        " WHERE\n" +
                        "   user_code = '"+user_code+"'\n" +
                        "   and session_app <> ''\n")
                .append(";")
                .toString();
    }
}
