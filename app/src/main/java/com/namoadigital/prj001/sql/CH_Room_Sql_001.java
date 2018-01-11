package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Room_Sql_001 implements Specification {

    private String room_code;

    public CH_Room_Sql_001(String room_code) {
        this.room_code = room_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                .append(" SELECT\n" +
                        "   r.*" +
                        " FROM\n" +
                        "   "+ CH_RoomDao.TABLE+" r \n" +
                        " WHERE" +
                        "   r.room_code = '"+room_code+"'\n;"
                ).toString();


    }
}
