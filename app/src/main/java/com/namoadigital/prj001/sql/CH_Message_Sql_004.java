package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 06/12/2017.
 *
 * Seleciona ultimo msg_prefix e ultimo tmp
 * para serem armazenados.
 */

public class CH_Message_Sql_004 implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                .append(" SELECT\n" +
                        "   m.msg_prefix msg_prefix,\n" +
                        "   max(m.tmp) tmp\n" +
                        " FROM\n" +
                        "   "+ CH_MessageDao.TABLE+" m\n" +
                        " WHERE\n" +
                        "  m.msg_prefix = (\n" +
                        "                   SELECT\n" +
                        "                         MAX(m.msg_prefix)\n" +
                        "                   FROM\n" +
                        "                       "+ CH_MessageDao.TABLE+" m\n" +
                        "                   )\n")
                .append(";")
                .append(CH_MessageDao.MSG_PREFIX+"#"+CH_MessageDao.TMP)
                .toString();
    }
}
