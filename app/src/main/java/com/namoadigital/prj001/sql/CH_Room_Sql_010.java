package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Room_Sql_010 implements Specification {

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_RoomDao.columns);

    public CH_Room_Sql_010() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT distinct room_image_local \n" +
                        " FROM " + CH_RoomDao.TABLE +
                        " WHERE status_update = '0' and room_image_local != '' and room_image_local notnull and room_image_local  not in \n" +
                        "  (\n" +
                        "   SELECT distinct room_image_local " +
                        "   FROM " + CH_RoomDao.TABLE +
                        "   WHERE status_update = '1' and room_image_local != '' and room_image_local notnull\n" +
                        "  )")
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
