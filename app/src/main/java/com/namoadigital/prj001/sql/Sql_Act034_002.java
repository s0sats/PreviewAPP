package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 13/12/2017.
 * Query do drawer de customers.
 * Retorna lista de customer com ccustomer code e customer name
 */

public class Sql_Act034_002 implements Specification {
    private String user_code;

    public Sql_Act034_002(String user_code) {
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "    e.customer_code,\n" +
                        "    e.customer_name\n" +
                        " FROM    \n" +
                        "     "+ EV_User_CustomerDao.TABLE+" e\n" +
                        " WHERE\n" +
                        "   e.user_code = '"+user_code+"' \n" +
                        " ORDER BY\n" +
                        "  e.customer_code   \n")
                .append(";")
                .append(EV_User_CustomerDao.CUSTOMER_CODE+"#"+EV_User_CustomerDao.CUSTOMER_NAME)
                .toString();

    }
}
