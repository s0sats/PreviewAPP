package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_002 implements Specification {

    private int msg_prefix;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_MessageDao.columns);

    public CH_Message_Sql_002(int msg_prefix) {
        this.msg_prefix = msg_prefix;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                .append(" SELECT\n" +
                        "   IFNULL(MAX(m.tmp),100) + 1 tmp\n" +
                        " FROM\n" +
                        CH_MessageDao.TABLE +" m\n" +
                        " WHERE\n" +
                        "   m.msg_prefix = '"+msg_prefix+"' \n")
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
