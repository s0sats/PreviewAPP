package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 06/07/17.
 */

public class SO_Pack_Express_Local_Sql_005 implements Specification {

    private String status;

    public SO_Pack_Express_Local_Sql_005() {
        this.status = "NEW";
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      S.*\n" +
                        " FROM\n" +
                        SO_Pack_Express_LocalDao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.status =       '" + status + "' ")
                .toString();
    }
}
