package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 27/03/2017.
 */

/**
 *  Query que zera a sessão da lista de cusomer na tabela de customer local
 *
 */

public class EV_User_Customer_Sql_005 implements Specification {

    private String user_code;
    private String customer_list;

    public EV_User_Customer_Sql_005(String user_code, String customer_list) {
        this.user_code = user_code;
        this.customer_list = customer_list;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" update ev_user_customers \n" +
                        "      set session_app = '' " +
                       // " , pending = 0\n" +
                        " where\n " +
                        "    user_code = '"+user_code+"'\n " +
                        "    and customer_code in ('"+customer_list+"') ")
                .toString() ;
    }
}
