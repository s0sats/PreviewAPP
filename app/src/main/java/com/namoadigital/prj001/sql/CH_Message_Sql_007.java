package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_007 implements Specification {

    private int msg_prefix;
    private long msg_code;
    private String message_image_local;

    public CH_Message_Sql_007(int msg_prefix, long msg_code, String message_image_local) {
        this.msg_prefix = msg_prefix;
        this.msg_code = msg_code;
        this.message_image_local = message_image_local;

    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " + CH_MessageDao.TABLE + " SET\n" +
                        "    message_image_local = '" + message_image_local + "' \n" +
                        " WHERE\n" +
                        "  msg_prefix  = '" + msg_prefix + "' \n " +
                        "  and msg_code  = '" + msg_code + "' \n ")
                .toString();
    }
}
