package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 18/12/2017.
 *
 * Seleciona maior msg code recebido
 *
 */

public class CH_Message_Sql_013 implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   m.msg_prefix,\n" +
                        "   m.msg_code\n" +
                        " FROM\n" +
                        "   "+ CH_MessageDao.TABLE+" m\n" +
                        " WHERE\n" +
                        "   m.msg_pk = (  \n" +
                        "              SELECT\n" +
                        "                    MAX(t.msg_pk) msg_pk      \n" +
                        "               FROM\n" +
                        "                   "+ CH_MessageDao.TABLE+" t)\n")
                .append(";")
                .append(CH_MessageDao.MSG_PREFIX+"#"+CH_MessageDao.MSG_CODE)
                .toString();
    }
}
