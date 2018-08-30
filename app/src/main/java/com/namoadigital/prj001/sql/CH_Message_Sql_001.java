package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 *
 * Selecion todas as mensagens de uma sala
 */

public class CH_Message_Sql_001 implements Specification {

    private String room_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_MessageDao.columns);

    public CH_Message_Sql_001(String room_code) {
        this.room_code = room_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                .append(" SELECT\n" +
                        "   m.*\n" +
                        " FROM\n" +
                        CH_MessageDao.TABLE +" m\n" +
                        " WHERE\n" +
                        "   m.room_code = '"+room_code+"' \n" +
                        " ORDER BY\n" +
                        "   m.msg_pk asc\n")
                .append(";")
                //.append(HmAuxFields)
                .toString();
    }
}
