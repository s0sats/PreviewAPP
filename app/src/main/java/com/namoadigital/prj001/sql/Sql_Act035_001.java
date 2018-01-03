package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 23/02/17.
 */

public class Sql_Act035_001 implements Specification {

    private String room_code;
    private String offSet;

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_MessageDao.columns);


    public Sql_Act035_001(String room_code, String offSet) {
        this.room_code = room_code;
        this.offSet = offSet;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();



        /*

select msg_pk, msg_prefix, msg_code, tmp from ch_messages
where room_code = '2017.J'
order by case when msg_pk is null then 1 else 0 end, msg_pk, msg_prefix , tmp limit 50 offset (select count(*) from ch_messages where (room_code = '2017.J' ))-50


         */

        return sb
                .append(" SELECT\n" +
                        "      *\n" +
                        " FROM\n" +
                        CH_MessageDao.TABLE + " \n" +
                        " WHERE\n" +
                        "    room_code =  '" + room_code + "'\n" +
                        "    order by case when msg_pk is null then 1 else 0 end, msg_pk, msg_prefix, tmp limit " + offSet + " offset (select count(*) from ch_messages where (room_code = '" + room_code + "' ))-" + offSet)
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
