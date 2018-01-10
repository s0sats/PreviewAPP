package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 18/12/2017.
 *
 * Seta flags de all_delivered ou all_read para 1
 */

public class CH_Message_Sql_010 implements Specification {
    public static final String FLAG_ALL_DELIVERED = "ALL_DELIVERED";
    public static final String FLAG_ALL_READ = "ALL_READ";

    private int msg_prefix;
    private long msg_code;
    private long msg_token;
    private String flag;
    private String updateContent = "";

    public CH_Message_Sql_010(int msg_prefix, long msg_code,long msg_token, String flag) {
        this.msg_prefix = msg_prefix;
        this.msg_code = msg_code;
        this.msg_token = msg_token;
        this.flag = flag;
        if (flag.equalsIgnoreCase(FLAG_ALL_READ)) {
            updateContent = " all_delivered = 1,\n" +
                            " all_read = 1 ,\n" +
                            " status_update = 1\n," +
                            " msg_token = '"+msg_token+"'\n";
        }else{
            updateContent = " all_delivered = 1,\n" +
                            " status_update = 1\n," +
                            " msg_token = '"+msg_token+"'\n";
        }
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " + CH_MessageDao.TABLE + " SET\n" +
                        "  "+updateContent+"  \n" +
                        " WHERE\n" +
                        "  msg_prefix  = '" + msg_prefix + "' \n " +
                        "  and msg_code  = '" + msg_code + "' \n ")
                .toString();
    }
}