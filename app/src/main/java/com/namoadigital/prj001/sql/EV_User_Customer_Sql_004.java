package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 24/03/2017.
 */

public class EV_User_Customer_Sql_004 implements Specification {

    public static final String KEY_LOGOUT = "key_logout";

    private String s_user;

    public EV_User_Customer_Sql_004(String s_user) {
        this.s_user = s_user;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   e.*," +
                        "   '0' "+ KEY_LOGOUT +"\n" +
                        " FROM\n" +
                        EV_User_CustomerDao.TABLE +" e\n" +
                        " WHERE\n" +
                        "   e.user_code = '"+s_user+"' " +
                        "   AND e.session_app <> '';" +
                        "customer_code#customer_name#"+KEY_LOGOUT)
                .toString();
    }
}
