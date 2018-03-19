package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 06/12/2017.
 *
 * Selecionar maior tmp e token para alimentar preferencias
 */

public class CH_Message_Sql_004 implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                .append(" SELECT\n" +
                        "   IFNULL(max(m.tmp),100) "+CH_MessageDao.TMP+",\n" +
                        "   IFNULL(max(m.msg_token),0) "+CH_MessageDao.MSG_TOKEN+"\n" +
                        " FROM\n" +
                        "   ch_messages m\n")
                .append(";")
                .append(CH_MessageDao.TMP+"#"+CH_MessageDao.MSG_TOKEN)
                .toString();
    }
}
