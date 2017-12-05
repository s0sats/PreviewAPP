package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Room_Sql_002 implements Specification {

    public static final String FILE_LOCAL_NAME = "FILE_LOCAL_NAME";
    //
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_RoomDao.columns);

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                .append(" SELECT\n" +
                        "   r.*,\n" +
                        "  'ch_room_'||r.room_code " + FILE_LOCAL_NAME+"\n"+
                        " FROM\n" +
                            CH_RoomDao.TABLE +" r\n" +
                        " WHERE \n" +
                        "   r.room_image <> ''\n" +
                        "   and r.room_image_local is null\n" +
                        " ORDER BY\n" +
                        "  r.room_code\n")

                .append(";")
                .append(HmAuxFields+"#"+FILE_LOCAL_NAME)
                .toString();
    }
}
