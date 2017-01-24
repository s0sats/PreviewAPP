package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 *
 * Retorna o session_app
 */

public class EV_User_Customer_Sql_003 implements Specification {

    private String customer_code;
    private String user_code;

    public EV_User_Customer_Sql_003(String customer_code,String user_code) {
        this.customer_code = customer_code;
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append("SELECT " +
                         "  session_app " +
                         "FROM ")
                  .append(EV_User_CustomerDao.TABLE)
                  .append(" WHERE ")
                  .append(EV_User_CustomerDao.CUSTOMER_CODE +" = '"+ customer_code+"'")
                  .append(" AND ")
                  .append(EV_User_CustomerDao.USER_CODE +" = '"+ user_code+"'")
                  .append(";session_app")
                .toString();
    }
}
