package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Room_Sql_003 implements Specification {

    public static final String FILE_LOCAL_NAME = "FILE_LOCAL_NAME";
    //
    private String room_code;
    private String image_local_name;

    public CH_Room_Sql_003(String room_code, String image_local_name) {
        this.room_code = room_code;
        this.image_local_name = image_local_name;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                .append(" UPDATE "+CH_RoomDao.TABLE +" SET\n" +
                        "    room_image_local = '"+image_local_name+"' \n" +
                        " WHERE\n" +
                        "  room_code  = '"+room_code+"' \n " )
                .toString();
    }
}
