package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_017 implements Specification {

    private String room_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_MessageDao.columns);

    public CH_Message_Sql_017(String room_code) {
        this.room_code = room_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      *\n" +
                        " FROM\n" +
                        CH_MessageDao.TABLE + " \n" +
                        " WHERE\n" +
                        "   room_code =  '" + room_code + "' \n" +
                        "   and read = '0'")
                .append(";")
                //.append(HmAuxFields)
                .toString();
    }
}
