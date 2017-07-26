package com.namoadigital.prj001.sql;


import com.namoadigital.prj001.dao.EV_ProfileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 26/05/2017.
 */

public class EV_Profile_Sql_001 implements Specification {

    private String customer_code;
    private String menu_code;
    private String parameter_code;

    public EV_Profile_Sql_001(String customer_code, String menu_code, String parameter_code) {
        this.customer_code = customer_code;
        this.menu_code = menu_code;
        this.parameter_code = parameter_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n " +
                        "   p.*\n " +
                        " FROM\n " +
                        EV_ProfileDao.TABLE +" p\n " +
                        " WHERE\n " +
                        "      p.customer_code = '"+customer_code+"'\n " +
                        "      and p.menu_code = '"+menu_code+"'\n " +
                        "      and p.parameter_code = '"+parameter_code+"'")
                .toString();
    }
}
