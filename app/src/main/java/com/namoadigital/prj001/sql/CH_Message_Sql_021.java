package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_021 implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb
            .append(" SELECT\n" +
                    "   m.*\n" +
                    " FROM\n" +
                    "   "+ CH_MessageDao.TABLE+" m\n" +
                    " WHERE\n" +
                    "   m.delivered = 0;");


        return sb.toString();
    }
}
