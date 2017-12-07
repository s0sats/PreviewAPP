package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 06/12/2017.
 *
 * Seleciona uma unica msg usando o code
 */

public class CH_Message_Sql_005 implements Specification {

    private int msg_prefix;
    private int msg_code;

    public CH_Message_Sql_005(int msg_prefix, int msg_code) {
        this.msg_prefix = msg_prefix;
        this.msg_code = msg_code;
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
                        "   and m.msg_code = '"+msg_code+"' \n")
                .append(";\n")
                .toString();
    }
}
