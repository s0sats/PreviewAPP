package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_019 implements Specification {

    private HMAux hmAux;
    private String msg_read;
    private String msg_read_date;

    public CH_Message_Sql_019(HMAux hmAuxe, String msg_read, String msg_read_date) {
        hmAuxe = hmAuxe;
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
                        " WHERE\n")
                .append(" ( (msg_prefix = '" + hmAux.get("msg_prefix") + "') and (tmp = '" + hmAux.get("tmp") + "')  )");


        return sb.toString();
    }
}
