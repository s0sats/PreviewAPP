package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by neomatrix on 12/20/17.
 */

public class CH_Message_Sql_012 implements Specification {

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_MessageDao.columns);

    private ArrayList<HMAux> messages;

    public CH_Message_Sql_012(ArrayList<HMAux> messages) {
        this.messages = messages;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(" SELECT \n" +
                        "    m.* \n" +
                        " FROM \n" +
                        CH_MessageDao.TABLE + " m \n" +
                        " WHERE\n" +
                        " (1 != 1) \n");

        for (HMAux message : messages) {
            sb
                    .append(" or ( (msg_prefix = '" + message.get("msg_prefix") + "') and (tmp = '" + message.get("tmp") + "') and (tmp = '" + message.get("tmp") + "') )");
        }

        sb
                .append(";")
                .append(HmAuxFields);

        return sb.toString();
    }
}
