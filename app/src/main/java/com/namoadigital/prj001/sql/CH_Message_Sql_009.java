package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_009 implements Specification {

    private HMAux message;

    public CH_Message_Sql_009(HMAux message) {
        this.message = message;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(" UPDATE " + CH_MessageDao.TABLE + " SET\n" +
                        "    status_update = '0' \n" +
                        " WHERE \n" );
        sb
                .append(" ( (tmp = '" + message.get("tmp") + "') and (msg_token = '" + message.get("msg_token") + "') )");


        sb.append(";");

        return sb.toString();
    }
}
