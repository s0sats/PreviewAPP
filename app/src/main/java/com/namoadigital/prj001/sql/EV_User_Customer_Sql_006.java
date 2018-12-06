package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 24/03/2017.
 */

public class EV_User_Customer_Sql_006 implements Specification {

    public EV_User_Customer_Sql_006() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   customer_code, customer_name, translate_code, session_app\n" +
                        " FROM\n" +
                        EV_User_CustomerDao.TABLE + "\n" +
                        " WHERE\n" +
                        "   session_app  <> ''")
                .append(";")
                //.append("customer_code#customer_name#translate_code#session_app")
                .toString();
    }
}
