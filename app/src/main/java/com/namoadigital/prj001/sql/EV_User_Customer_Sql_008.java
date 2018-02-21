package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on21/02/2018.
 *
 * Query que seleciona sessão de um customer especifico
 *
 *
 */

public class EV_User_Customer_Sql_008 implements Specification {

    private String user_code;
    private long customer_code;

    public EV_User_Customer_Sql_008(String user_code, long customer_code) {
        this.user_code = user_code;
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "     e.customer_code,\n" +
                        "     e.session_app\n" +
                        " FROM    \n" +
                        "     "+ EV_User_CustomerDao.TABLE+" e\n" +
                        " WHERE\n" +
                        "   e.user_code = '"+user_code+"'\n" +
                        "   and e.customer_code = '"+customer_code+"' \n")
                .append(";")
                .append(EV_User_CustomerDao.CUSTOMER_CODE+"#"+EV_User_CustomerDao.SESSION_APP)
                .toString();
    }
}
