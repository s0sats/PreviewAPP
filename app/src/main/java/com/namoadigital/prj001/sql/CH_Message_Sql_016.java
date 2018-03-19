package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

import java.util.ArrayList;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_016 implements Specification {

    ArrayList<HMAux> hmAuxes;
    private String msg_read;
    private String msg_read_date;

    public CH_Message_Sql_016(ArrayList<HMAux> hmAuxes, String msg_read, String msg_read_date) {
        this.hmAuxes = hmAuxes;
        this.msg_read = msg_read;
        this.msg_read_date = msg_read_date;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(" UPDATE " + CH_MessageDao.TABLE + " SET\n" +
                        "    " + CH_MessageDao.READ + " = '" + msg_read + "', \n" +
                        "    " + CH_MessageDao.READ_DATE + " = '" + msg_read_date + "' \n" +
                        " WHERE\n" +
                        " (1 != 1) \n");

        for (HMAux message : hmAuxes) {
            sb
                    .append(" or ( (msg_prefix = '" + message.get("msg_prefix") + "') and (tmp = '" + message.get("tmp") + "')  )");
        }

        return sb.toString();
    }
}
