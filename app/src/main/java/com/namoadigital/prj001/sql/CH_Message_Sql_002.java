package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_002 implements Specification {

    public static final String NEXT_TMP = "next_tmp";

    private int msg_prefix;

    public CH_Message_Sql_002(int msg_prefix) {
        this.msg_prefix = msg_prefix;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                .append(" SELECT\n" +
                        "   IFNULL(MAX(m.tmp),100) + 1 "+NEXT_TMP+"\n" +
                        " FROM\n" +
                        CH_MessageDao.TABLE +" m\n" +
                        " WHERE\n" +
                        "   m.msg_prefix = '"+msg_prefix+"' \n")
                .append(";")
                //.append(NEXT_TMP)
                .toString();
    }
}
