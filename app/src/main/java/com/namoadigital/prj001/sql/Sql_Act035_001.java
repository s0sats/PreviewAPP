package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 23/02/17.
 */

public class Sql_Act035_001 implements Specification {

    private String room_code;

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_MessageDao.columns);


    public Sql_Act035_001(String room_code) {
        this.room_code = room_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      S.*\n" +
                        " FROM\n" +
                        CH_MessageDao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.room_code =  '" + room_code + "'\n" +
                        //"    order by case when msg_pk is null then 'ZZZZZZZZZZZZZZZZZZZZZZZZZZZ' else msg_pk end, msg_prefix, tmp")
                        "      order by case when msg_pk is null then 1 else 0 end, msg_pk, msg_prefix, tmp")
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
