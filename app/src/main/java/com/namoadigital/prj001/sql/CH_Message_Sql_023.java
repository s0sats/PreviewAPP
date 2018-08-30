package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 * <p>
 * Selecion todas as mensagens de uma sala
 */

public class CH_Message_Sql_023 implements Specification {

    public CH_Message_Sql_023() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("select  msg.room_code, msg.message_image_local from ch_messages msg where  msg.message_image_local != '' and \n" +
                        "\n" +
                        "msg.room_code in (\n" +
                        "\n" +
                        "select room_code from ch_rooms where ch_rooms.status_update = '0'\n" +
                        "\n" +
                        ")")
                .append(";")
                //.append(CH_MessageDao.ROOM_CODE)
                //.append("#")
                //.append(CH_MessageDao.MESSAGE_IMAGE_LOCAL)
                .toString();
    }
}
