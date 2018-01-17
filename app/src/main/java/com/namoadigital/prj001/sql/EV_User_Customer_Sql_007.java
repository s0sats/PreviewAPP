package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.Ev_User_Customer_ParameterDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 17/01/2018.
 *
 * Seleciona os customers com sessão ativa e que tem acesso ao chat
 *
 *
 */

public class EV_User_Customer_Sql_007 implements Specification {

    private String user_code;

    public EV_User_Customer_Sql_007(String user_code) {
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "     e.customer_code,\n" +
                        "     e.session_app\n" +
                        " FROM    \n" +
                        "     "+ EV_User_CustomerDao.TABLE+" e,\n" +
                        "     "+ Ev_User_Customer_ParameterDao.TABLE+" p \n" +
                        " WHERE\n" +
                        "   e.customer_code = p.customer_code   \n" +
                        "   and e.user_code = '"+user_code+"'\n" +
                        "   and e.session_app <> ''\n" +
                        "   and p.parameter_code = '"+ Constant.PARAM_CHAT+"'\n" +
                        " ORDER BY\n" +
                        "  e.customer_code   \n")
                .append(";")
                .append(EV_User_CustomerDao.CUSTOMER_CODE+"#"+EV_User_CustomerDao.SESSION_APP)
                .toString();
    }
}
