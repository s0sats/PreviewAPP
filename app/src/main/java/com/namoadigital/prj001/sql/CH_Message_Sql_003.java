package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 06/12/2017.
 *
 * Seleciona uma unica msg usando o tmp
 */

public class CH_Message_Sql_003 implements Specification {

    private int msg_prefix;
    private long tmp;

    public CH_Message_Sql_003(int msg_prefix, long tmp) {
        this.msg_prefix = msg_prefix;
        this.tmp = tmp;
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
                        "   and m.tmp = '"+tmp+"' \n")
                .append(";\n")
                .toString();
    }
}
