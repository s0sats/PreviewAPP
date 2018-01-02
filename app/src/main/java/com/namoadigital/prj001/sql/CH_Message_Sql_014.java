package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 06/12/2017.
 *
 * Seleciona uma unica msg usando o tmp
 */

public class CH_Message_Sql_014 implements Specification {

    private int msg_prefix;
    private long tmp;
    private String user_code;

    public CH_Message_Sql_014(int msg_prefix, long tmp, String user_code) {
        this.msg_prefix = msg_prefix;
        this.tmp = tmp;
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                .append(" SELECT\n" +
                        "    m.*\n" +
                        " FROM\n" +
                        CH_MessageDao.TABLE +" m\n" +
                        " WHERE\n" +
                        "   m.msg_prefix = '"+msg_prefix+"' \n"+
                        "   and m.tmp = '"+tmp+"' \n" +
                        "   and m.user_code = '"+user_code+"'")
                .append(";\n")
                .toString();
    }
}
