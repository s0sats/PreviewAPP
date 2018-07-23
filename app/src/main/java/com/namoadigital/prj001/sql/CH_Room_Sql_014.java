package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 30/11/2017.
 *
 * Seleciona room baseado no room_code ou pela "pk" contida no room_obj
 *
 */

public class CH_Room_Sql_014 implements Specification {

    private String room_code;
    private String pk_ap;

    public CH_Room_Sql_014(String room_code, String pk_ap) {
        this.room_code = room_code;
        this.pk_ap = pk_ap;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "   r.*\n" +
                        " FROM " + CH_RoomDao.TABLE +" r\n"+
                        " WHERE \n" +
                        "   r.room_type = '"+ Constant.CHAT_MESSAGE_TYPE_FORM_AP+"' \n" +
                        "   and ( '"+room_code+"' is null or r.room_code = '"+room_code+"') \n" +
                        "   and ( room_obj like '%\"pk\":\"" + pk_ap + "\"%') \n"
                )
                .toString().replace("'null'","null");
    }
}
