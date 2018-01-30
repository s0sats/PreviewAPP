package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Room_Sql_007 implements Specification {

    public CH_Room_Sql_007() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " + CH_RoomDao.TABLE + " SET\n" +
                        "    status_update = '0' \n" +
                        " WHERE\n" +
                        "  status_update  = '1' \n ")
                .toString();
    }
}
