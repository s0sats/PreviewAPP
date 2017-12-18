package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

import java.util.ArrayList;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_009 implements Specification {

    private ArrayList<HMAux> messages;

    public CH_Message_Sql_009(ArrayList<HMAux> messages) {
        this.messages = messages;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(" UPDATE " + CH_MessageDao.TABLE + " SET\n" +
                        "    status_update = '0' \n" +
                        " WHERE\n" +
                        " (1 != 1)\n");

        for (HMAux message : messages) {
            sb
                    .append(" or ( (msg_prefix = '" + message.get("msg_prefix") + "') and (tmp = '" + message.get("tmp") + "') )");
        }

        sb.append(";");

        return sb.toString();
    }
}
