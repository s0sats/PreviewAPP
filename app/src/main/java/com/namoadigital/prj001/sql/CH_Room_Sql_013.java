package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Room_Sql_013 implements Specification {

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_RoomDao.columns);

    private String pk_ap;

    public CH_Room_Sql_013(String pk_ap) {
        this.pk_ap = pk_ap;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT room_code" +
                        " FROM " + CH_RoomDao.TABLE +
                //       " WHERE room_type = 'FORM_AP' and room_obj like '%\"pk\":\"" + pk_ap + "\"%'  \n"
                        " WHERE room_type = '"+ Constant.CHAT_MESSAGE_TYPE_FORM_AP+"' and room_obj like '%\"pk\":\"" + pk_ap + "\"%'  \n"
                )
                .append(";")
                //.append("room_code")
                .toString();
    }
}
