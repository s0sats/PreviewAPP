package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_015 implements Specification {

    private String msg_prefix;
    private String msg_code;
    private String msg_read;
    private String msg_read_date;

    public CH_Message_Sql_015(String msg_prefix, String msg_code, String msg_read, String msg_read_date) {
        this.msg_prefix = msg_prefix;
        this.msg_code = msg_code;
        this.msg_read = msg_read;
        this.msg_read_date = msg_read_date;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " + CH_MessageDao.TABLE + " SET\n" +
                        "    " + CH_MessageDao.READ + " = '" + msg_read + "', \n" +
                        "    " + CH_MessageDao.READ_DATE + " = '" + msg_read_date + "' \n" +
                        " WHERE\n" +
                        "  msg_prefix  = '" + msg_prefix + "' \n " +
                        "  and msg_code  = '" + msg_code + "' \n ")
                .toString();
    }
}
