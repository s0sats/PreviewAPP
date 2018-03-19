package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_024 implements Specification {

    private int msg_prefix;
    private long msg_code;

    public CH_Message_Sql_024(int msg_prefix, long msg_code) {
        this.msg_prefix = msg_prefix;
        this.msg_code = msg_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " + CH_MessageDao.TABLE + " SET\n" +
                        "    file_status = '" + Constant.SYS_STATUS_SENT + "' \n" +
                        " WHERE\n" +
                        "  msg_prefix  = '" + msg_prefix + "' \n " +
                        "  and msg_code  = '" + msg_code + "' \n ")
                .toString();
    }
}
