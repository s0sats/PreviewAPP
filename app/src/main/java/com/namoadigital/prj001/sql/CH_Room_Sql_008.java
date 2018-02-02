package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Room_Sql_008 implements Specification {

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_RoomDao.columns);

    public CH_Room_Sql_008() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   r.*" +
                        " FROM\n" +
                        "   " + CH_RoomDao.TABLE + " r \n" +
                        " WHERE" +
                        "   status_update = '0'\n")
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
