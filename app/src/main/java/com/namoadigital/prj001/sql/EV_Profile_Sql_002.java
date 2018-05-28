package com.namoadigital.prj001.sql;


import com.namoadigital.prj001.dao.EV_ProfileDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 28/05/2018.
 * Retorna todos os profiles de um menu.
 */

public class EV_Profile_Sql_002 implements Specification {

    private String customer_code;
    private String menu_code;
    private String hmAuxFields = ToolBox_Inf.getColumnsToHmAux(EV_ProfileDao.columns);

    public EV_Profile_Sql_002(String customer_code, String menu_code) {
        this.customer_code = customer_code;
        this.menu_code = menu_code;
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
                        "      and p.menu_code = '"+menu_code+"'\n "
                )
                .append(hmAuxFields)
                .toString()
                ;
    }
}