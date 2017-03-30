package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.Ev_User_Customer_ParameterDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 30/03/2017.
 */

/**
 * Busca parametro no tabela
 * Se retorno != null , tem permissão.
 */

public class Ev_User_Customer_Parameter_Sql_002 implements Specification {

    private String customer_code;

    public Ev_User_Customer_Parameter_Sql_002(String customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();
        return sb
                .append(" SELECT\n " +
                        "   cp.* \n" +
                        " FROM \n " +
                        Ev_User_Customer_ParameterDao.TABLE +" cp \n " +
                        " WHERE \n" +
                        "  cp.customer_code = '"+customer_code+"' \n " +
                        "  AND cp.parameter_code = '"+customer_code+"'")
                .toString();
    }
}
