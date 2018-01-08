package com.namoadigital.prj001.sql;


import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 09/01/17.
 */

public class Sql_Chat_Notification_001 implements Specification {

    public static final String QTY_ROOM = "qty_room";
    public static final String QTY_MSG = "qty_msg";
    public static final String LAST_ROOM = "last_room";
    public static final String LAST_MSG = "last_msg";

    private String user_code;

    public Sql_Chat_Notification_001(String user_code) {
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count( distinct(r.room_code)) qty_room,\n" +
                        "   count(m.msg_code) qty_msg,\n" +
                        "   (SELECT r.room_desc\n" +
                        "    FROM "+ CH_MessageDao.TABLE+" m\n," +
                        "         "+ CH_RoomDao.TABLE+" r\n" +
                        "   WHERE\n" +
                        "         m.room_code = r.room_code" +
                        "         AND m.read = 0\n" +
                        "         AND m.user_code <> '"+user_code+"'\n" +
                        "    LIMIT 1 ) last_room\n," +
                        "   (SELECT m.msg_obj\n" +
                        "    FROM "+ CH_MessageDao.TABLE+" m\n," +
                        "         "+ CH_RoomDao.TABLE+" r\n" +
                        "   WHERE\n" +
                        "         m.room_code = r.room_code" +
                        "         AND m.read = 0\n" +
                        "         AND m.user_code <> '"+user_code+"'\n" +
                        "    LIMIT 1 ) last_msg\n" +
                        " FROM    \n" +
                        "     "+ CH_RoomDao.TABLE+" r\n" +
                        " LEFT JOIN\n" +
                        "    "+ CH_MessageDao.TABLE+" m ON r.room_code = m.room_code\n" +
                        " WHERE\n" +
                        "    m.read = 0\n" +
                        "    and m.user_code <> '"+user_code+"'\n")
                .append(";"+QTY_ROOM+"#"+QTY_MSG+"#"+LAST_ROOM+"#"+LAST_MSG)
                .toString();
    }
}
