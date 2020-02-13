package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 *Luche 13/02/2020
 * - Modificado query colocando *  para selecionar todos so campos
 */

public class EV_User_Customer_Sql_001 implements Specification {

    private String s_user_code;

    public EV_User_Customer_Sql_001(String s_user_code) {
        this.s_user_code = s_user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT " +
                        /*"      customer_code,\n " +
                        "      customer_name,\n " +
                        "      translate_code,\n " +
                        "      blocked,\n " +
                        "      session_app,\n " +
                        "      pending ,\n " +
                        "      translate_code ,\n " +
                        "      nls_date_format,\n " +
                        "      tracking" +*/
                        "   *   \n"+
                        " FROM \n ")
                .append(EV_User_CustomerDao.TABLE)
                .append(" WHERE \n " +
                             EV_User_CustomerDao.USER_CODE +" = '"+s_user_code+"' \n " +
                        " ORDER by customer_name;")
                /*.append("customer_code#" +
                        "customer_name#" +
                        "translate_code#" +
                        "blocked#" +
                        "session_app#" +
                        "pending#" +
                        "translate_code#" +
                        "nls_date_format#" +
                        "tracking"
                )*/
                .toString();
    }
}
