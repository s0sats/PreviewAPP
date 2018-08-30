package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Room_Sql_011 implements Specification {

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_RoomDao.columns);

    private String room_code;

    public CH_Room_Sql_011(String room_code) {
        this.room_code = room_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT room_image_local" +
                        " FROM " + CH_RoomDao.TABLE +
                        " WHERE room_code = '" + room_code + "' and room_image_local != '' and room_image_local notnull and room_image_local  not in \n" +
                        " (\n" +
                        "  SELECT distinct room_image_local  FROM ch_rooms WHERE room_code != '" + room_code + "' and room_image_local != '' and room_image_local notnull\n" +
                        " )")
                .append(";")
                //.append(HmAuxFields)
                .toString();
    }
}
