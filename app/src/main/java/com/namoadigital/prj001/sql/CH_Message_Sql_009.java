package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 18/12/2017.
 *
 * Seleciona msgs offiline.
 */

public class CH_Message_Sql_009 implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   m.*\n" +
                        " FROM\n" +
                        "     "+CH_MessageDao.TABLE+" m\n" +
                        " WHERE\n" +
                        "   m.msg_code = 0\n" +
                        "   and m.msg_pk is null\n" +
                        " ORDER BY \n" +
                        "  m.tmp asc\n")
                .toString();
    }
}
